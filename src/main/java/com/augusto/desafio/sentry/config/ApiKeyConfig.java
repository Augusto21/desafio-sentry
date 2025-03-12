package com.augusto.desafio.sentry.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class ApiKeyConfig {

  @Value("${security.api.key}")
  private String apiKey;
}
