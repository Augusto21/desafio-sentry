package com.augusto.desafio.sentry.repository;

import com.augusto.desafio.sentry.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, Long> {}
