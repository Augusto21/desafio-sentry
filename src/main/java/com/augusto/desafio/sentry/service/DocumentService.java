package com.augusto.desafio.sentry.service;

import com.augusto.desafio.sentry.execption.DocumentNotFoundException;
import com.augusto.desafio.sentry.model.Document;
import com.augusto.desafio.sentry.repository.DocumentRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentService {

  private final DocumentRepository documentoRepository;

  @Transactional
  public Document createDocument(String name, String fileName, MultipartFile file) {
    log.info("Criando novo documento: {}", name);

    Document document =
        Document.builder()
            .name(name)
            .filePath(fileName)
            .size(file.getSize())
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

    Document savedDocumento = documentoRepository.save(document);
    log.info("Documento criado com sucesso: ID {}", savedDocumento.getId());
    return savedDocumento;
  }

  public Document getDocumentById(Long id) {
    log.info("Buscando documento com ID: {}", id);
    return documentoRepository
        .findById(id)
        .orElseThrow(
            () -> new DocumentNotFoundException("Documento com ID " + id + " não encontrado"));
  }

  public List<Document> getAllDocumentos() {
    log.info("Buscando todos os documentos...");
    return documentoRepository.findAll();
  }
}
