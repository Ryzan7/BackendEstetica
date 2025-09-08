package com.esc2.api.estetica.security;

import java.io.IOException; // <-- correto
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(JwtAuthFilter.class); // Adicione esta linha
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService uds;

    public JwtAuthFilter(JwtUtil jwtUtil, CustomUserDetailsService uds) {
        this.jwtUtil = jwtUtil;
        this.uds = uds;
    }

    // SUBSTITUA O MÉTODO INTEIRO EM JwtAuthFilter.java
    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {

        String header = req.getHeader("Authorization");
        String username = null;
        String token = null;

        if (header != null && header.startsWith("Bearer ")) {
            token = header.substring(7);
            username = jwtUtil.getUsername(token);
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            System.out.println("\n>>> [FILTRO] Buscando usuário '" + username + "' no banco de dados...");

            UserDetails user = uds.loadUserByUsername(username);

            System.out.println(">>> [FILTRO] Usuário '" + user.getUsername() + "' encontrado com sucesso!");

            if (jwtUtil.validate(token, user)) {
                System.out.println(">>> [FILTRO] Token validado com sucesso. Configurando segurança.");
                log.info("Usuário '{}' validado. Permissões: {}", user.getUsername(), user.getAuthorities());
                var auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                SecurityContextHolder.getContext().setAuthentication(auth);
            } else {
                System.out.println(">>> [FILTRO] A validação do token falhou (jwtUtil.validate retornou false).");
            }
        }
        chain.doFilter(req, res);
    }
}
