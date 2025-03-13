package com.augusto.desafio.sentry.repository;

import com.augusto.desafio.sentry.model.Document;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DocumentRepository extends JpaRepository<Document, Long> {
  Optional<Document> findByName(String name);

  @Query("SELECT COALESCE(SUM(d.tamanho), 0) FROM Document d")
  long somarTamanhoArquivos();

}
