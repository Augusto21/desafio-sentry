package com.augusto.desafio.sentry.service;

import com.augusto.desafio.sentry.execption.DocumentNotFoundException;
import com.augusto.desafio.sentry.model.Document;
import com.augusto.desafio.sentry.repository.DocumentRepository;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

  public Path getDocumentByName(String name) {
    log.info("Buscando documento pelo nome: {}", name);

    Document document =
        documentoRepository
            .findByName(name)
            .orElseThrow(
                () ->
                    new DocumentNotFoundException(
                        "Documento com nome " + name + " não encontrado"));

    Path filePath = Paths.get(document.getFilePath()).toAbsolutePath();
    log.info("Tentando carregar arquivo do caminho: {}", filePath);

    if (!Files.exists(filePath) || !Files.isReadable(filePath)) {
      log.error("Arquivo não encontrado ou sem permissão de leitura: {}", filePath);
      throw new DocumentNotFoundException("Arquivo não encontrado: " + filePath);
    }

    return filePath;
  }

  public List<Document> getAllDocuments() {
    log.info("Buscando todos os documentos...");
    return documentoRepository.findAll();
  }

  @Transactional
  public void deleteDocument(String name) {
    Document document =
        documentoRepository
            .findByName(name)
            .orElseThrow(() -> new DocumentNotFoundException("Documento não encontrado: " + name));

    Path filePath = Paths.get(document.getFilePath());

    try {
      Files.deleteIfExists(filePath);
      log.info("Arquivo deletado: {}", filePath);
    } catch (IOException e) {
      log.error("Erro ao deletar arquivo: {}", filePath, e);
      throw new RuntimeException("Erro ao deletar arquivo", e);
    }

    documentoRepository.delete(document);
    log.info("Documento deletado do banco: {}", name);
  }

  @Transactional
  public Document updateDocument(String name, String newName, MultipartFile file) {
    Document document =
        documentoRepository
            .findByName(name)
            .orElseThrow(() -> new DocumentNotFoundException("Documento não encontrado: " + name));

    if (newName != null && !newName.isEmpty()) {
      document.setName(newName);
    }

    if (file != null && !file.isEmpty()) {
      Path oldFilePath = Paths.get(document.getFilePath());
      Path newFilePath = oldFilePath.getParent().resolve(file.getOriginalFilename());

      try {
        Files.deleteIfExists(oldFilePath);
        file.transferTo(newFilePath.toFile());
        document.setFilePath(newFilePath.toString());
        document.setSize(file.getSize());
        log.info("Arquivo atualizado para: {}", newFilePath);
      } catch (IOException e) {
        log.error("Erro ao atualizar o arquivo: {}", newFilePath, e);
        throw new RuntimeException("Erro ao atualizar o arquivo", e);
      }
    }

    document.setUpdatedAt(LocalDateTime.now());
    return documentoRepository.save(document);
  }
}
