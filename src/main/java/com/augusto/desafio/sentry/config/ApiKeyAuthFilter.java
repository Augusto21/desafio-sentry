package com.augusto.desafio.sentry.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class ApiKeyAuthFilter extends OncePerRequestFilter {

  private final ApiKeyConfig apiKeyConfig;

  public ApiKeyAuthFilter(ApiKeyConfig apiKeyConfig) {
    this.apiKeyConfig = apiKeyConfig;
  }

  private static final String API_KEY_HEADER = "X-API-KEY";

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
          throws ServletException, IOException {

    String path = request.getRequestURI();

    if (path.startsWith("/swagger-ui") || path.startsWith("/v3/api-docs")) {
      filterChain.doFilter(request, response);
      return;
    }

    String apiKey = request.getHeader(API_KEY_HEADER);
    if (apiKeyConfig.getApiKey().equals(apiKey)) {
      PreAuthenticatedAuthenticationToken auth =
              new PreAuthenticatedAuthenticationToken(apiKey, null, null);
      SecurityContextHolder.getContext().setAuthentication(auth);
    } else {
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid API Key");
      return;
    }

    filterChain.doFilter(request, response);
  }
}
