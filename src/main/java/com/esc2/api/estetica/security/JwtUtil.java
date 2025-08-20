package com.esc2.api.estetica.security;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    @Value("${app.security.jwt.secret}")
    private String secret;

    @Value("${app.security.jwt.expiration-seconds:3600}")
    private long expirationSeconds;

    private Key getKey() {
        // precisa ter >= 32 bytes para HS256
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(UserDetails user) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + (expirationSeconds * 1000));

        String firstRole = user.getAuthorities()
                               .stream()
                               .findFirst()
                               .map(a -> a.getAuthority())
                               .orElse(null);

        return Jwts.builder()
            .setSubject(user.getUsername())
            .claim("role", firstRole)
            .setIssuedAt(now)
            .setExpiration(exp)
            .signWith(getKey(), SignatureAlgorithm.HS256)
            .compact();
    }

    public String getUsername(String token) {
        return getClaims(token).getSubject();
    }

    public boolean validate(String token, UserDetails user) {
        Claims claims = getClaims(token);
        boolean notExpired = claims.getExpiration().after(new Date());
        return user.getUsername().equals(claims.getSubject()) && notExpired;
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(getKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
    }
}
