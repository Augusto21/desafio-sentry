package com.augusto.desafio.sentry.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class ApiKeyFilter extends OncePerRequestFilter {

  private final String apiKey;

  public ApiKeyFilter(@Value("${security.api.key}") String apiKey) {
    this.apiKey = apiKey;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    String receivedApiKey = request.getHeader("X-API-KEY");

    if (receivedApiKey == null || !receivedApiKey.equals(apiKey)) {
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "API Key inválida");
      return;
    }

    filterChain.doFilter(request, response);
  }
}
