package br.fai.lds.medlink.configuration.security;

import br.fai.lds.medlink.implementation.service.authentication.jwt.JwtRequestFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Profile("jwt")
@Configuration
public class JwtSecurityConfiguration extends BasicSecurityConfiguration {

    private final JwtRequestFilter jwtRequestFilter;

    public JwtSecurityConfiguration(JwtRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
            .cors(
                    cors -> cors.configurationSource(corsConfigurationSource())
            )
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        auth ->
                                auth.requestMatchers(
                                        "/swagger-ui/**",
                                        "/swagger-ui.html",
                                        "/v3/api-docs/**",
                                        "/authenticate"
                                ).permitAll()
                                        .requestMatchers(
                                                "/api/medic/**"
                                        ).hasAuthority("MEDIC")
                                        .requestMatchers(HttpMethod.GET, "/api/patients")
                                        .hasAnyAuthority(
                                                "MEDIC",
                                                "PATIENT")
                                        .requestMatchers(HttpMethod.GET, "/api/patients/*")
                                        .hasAnyAuthority("MEDIC",
                                                "PATIENT")
                                        .requestMatchers(HttpMethod.PUT, "/api/patients/*")
                                        .hasAuthority("PATIENT")
                                        .requestMatchers(HttpMethod.DELETE, "/api/patients/*")
                                        .hasAuthority("MEDIC")

                                .anyRequest().authenticated()
                )
                .sessionManagement(
                        session ->
                                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .headers(
                        headers ->
                                headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                )
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}

