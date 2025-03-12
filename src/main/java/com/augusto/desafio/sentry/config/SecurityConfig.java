package com.augusto.desafio.sentry.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Value("${security.api.key}") // Carrega a API Key do application.properties
  private String apiKey;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http.csrf(csrf -> csrf.disable()) // Desabilita CSRF para chamadas de API REST
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers(HttpMethod.GET, "/swagger-ui/**", "/v3/api-docs/**")
                    .permitAll() // Libera Swagger
                    .requestMatchers("/documentos/**")
                    .authenticated() // Protege a API de documentos
                    .anyRequest()
                    .authenticated())
        .addFilterBefore(
            new ApiKeyFilter(apiKey),
            UsernamePasswordAuthenticationFilter
                .class) // Adiciona filtro antes do authentication filter
        .build();
  }
}
