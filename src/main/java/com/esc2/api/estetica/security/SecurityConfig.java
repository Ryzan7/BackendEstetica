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
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> {

                auth.requestMatchers("/auth/login").permitAll();
                //Regras GET - Coordenador pode ler 'usuarios' 'clientes' 'profissionais' e 'servicos'
                auth.requestMatchers(HttpMethod.GET, "/usuarios/**", "/clientes/**", "/profissionais/**", "/servicos/**")
                        .hasAnyRole("COORDENADOR", "ADMINISTRADOR");

                // Profissionais podem LER Agendamentos
                auth.requestMatchers(HttpMethod.GET, "/agendamentos/**")
                        .hasAnyRole("ADMINISTRADOR", "COORDENADOR", "PROFISSIONAL");

                // Regras de escrita POST - PUT - DELETE
                auth.requestMatchers("/usuarios/**", "/clientes/**", "/profissionais/**", "/servicos/**", "/agendamentos/**", "/relatorio/**")
                        .hasRole("ADMINISTRADOR");


                auth.anyRequest().authenticated();
            })
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
        }
}
