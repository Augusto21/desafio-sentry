package com.augusto.desafio.sentry.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class ApiKeyFilter extends OncePerRequestFilter {

  private final String apiKey;

  public ApiKeyFilter(String apiKey) { // 🔥 Agora a API Key é passada via construtor
    this.apiKey = apiKey;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
          throws ServletException, IOException {

    String receivedApiKey = request.getHeader(HttpHeaders.AUTHORIZATION);
    log.info("Recebida API Key: {}", receivedApiKey);
    log.info("API key do Filter: {}", this.apiKey); // ✅ Agora deve exibir corretamente a chave do application.properties

    if (receivedApiKey == null || !receivedApiKey.equals(apiKey)) {
      log.error("API Key inválida: {}", receivedApiKey);
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "API Key inválida ou ausente!");
      return;
    }

    SecurityContextHolder.getContext().setAuthentication(
            new PreAuthenticatedAuthenticationToken(receivedApiKey, null)
    );

    filterChain.doFilter(request, response);
  }
}
