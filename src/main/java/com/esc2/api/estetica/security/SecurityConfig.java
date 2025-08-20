package com.esc2.api.estetica.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // AuthenticationManager obtido do contexto (Boot monta usando seu UserDetailsService + PasswordEncoder)
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // paths onde COORD pode consultar (GET) mas só ADMIN gerencia
        final String[] GET_COORD_ONLY = {
            "/clientes/**",
            "/profissionais/**",
            "/servicos/**"
        };

        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // login público
                .requestMatchers("/auth/login").permitAll()

                // USUÁRIOS — COORD só vê (GET), ADMIN gerencia tudo
                .requestMatchers(HttpMethod.GET, "/usuarios/**")
                    .hasAnyRole("ADMINISTRADOR","COORDENADOR")
                .requestMatchers("/usuarios/**")
                    .hasRole("ADMINISTRADOR")

                // AGENDAMENTOS — PROF/COORD/ADMIN veem (GET); só ADMIN gerencia
                .requestMatchers(HttpMethod.GET, "/agendamentos/**")
                    .hasAnyRole("ADMINISTRADOR","COORDENADOR","PROFESSOR")
                .requestMatchers("/agendamentos/**")
                    .hasRole("ADMINISTRADOR")

                // CLIENTES / PROFISSIONAIS / SERVIÇOS — COORD/ADMIN veem (GET); só ADMIN gerencia
                .requestMatchers(HttpMethod.GET, GET_COORD_ONLY)
                    .hasAnyRole("ADMINISTRADOR","COORDENADOR")
                .requestMatchers(GET_COORD_ONLY)
                    .hasRole("ADMINISTRADOR")

                // qualquer outro endpoint exige estar logado
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


}
