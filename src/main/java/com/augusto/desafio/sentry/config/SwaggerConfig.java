package com.augusto.desafio.sentry.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
    servers = {@Server(url = "http://localhost:8080", description = "Servidor local")})
@Configuration
public class SwaggerConfig {
  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .info(
            new Info()
                .title("API de Documentos")
                .version("1.0")
                .contact(
                    new Contact().name("Augusto Camolesi").email("augusto.ycamolesi@gmail.com"))
                .description("Documentação da API para gerenciamento de documentos"))
        .addSecurityItem(
            new SecurityRequirement().addList("ApiKeyAuth")) // Adiciona segurança ao Swagger
        .components(
            new io.swagger.v3.oas.models.Components()
                .addSecuritySchemes(
                    "ApiKeyAuth",
                    new SecurityScheme()
                        .name("apiKey") // Nome do cabeçalho da API Key
                        .type(SecurityScheme.Type.APIKEY)
                        .in(
                            SecurityScheme.In
                                .HEADER))); // Define que a API Key é passada no cabeçalho
  }
}
