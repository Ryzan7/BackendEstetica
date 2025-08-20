package com.esc2.api.estetica.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.esc2.api.estetica.security.JwtUtil;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authManager, JwtUtil jwtUtil) {
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@RequestBody LoginDTO body) {
        try {
            Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(body.email(), body.password())
            );

            // Se seu JwtUtil recebe UserDetails:
            var user = (org.springframework.security.core.userdetails.UserDetails) authentication.getPrincipal();
            String token = jwtUtil.generateToken(user);

            // Se seu JwtUtil recebe Authentication, use:
            // String token = jwtUtil.generateToken(authentication);

            return ResponseEntity.ok(new TokenDTO(token));
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(401).build();
        }
    }

    // DTOs simples (evita arquivo extra e imports)
    public record LoginDTO(String email, String password) {}
    public record TokenDTO(String token) {}
}
