package com.augusto.desafio.sentry.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.*;

@Data
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Document {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull(message = "O nome não pode ser nulo")
  private String name;

  private String filePath;

  @Min(value = 0, message = "O tamanho deve ser maior ou igual a zero")
  private Long size;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;

  public String getSummary() {
    return String.format("Documento: %s, Tamanho: %d bytes", name, size);
  }
}
