package com.augusto.desafio.sentry.service;

import com.augusto.desafio.sentry.execption.DocumentNotFoundException;
import com.augusto.desafio.sentry.model.Document;
import com.augusto.desafio.sentry.repository.DocumentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DocumentServiceTest {

  @Mock private DocumentRepository documentRepository;

  @Mock private MultipartFile file;

  @InjectMocks private DocumentService documentService;

  private Document document;

  @BeforeEach
  void setUp() {
    document =
        Document.builder()
            .id(1L)
            .name("teste")
            .filePath("C:/uploads/teste.txt")
            .size(1024L)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
  }

  @Test
  void deveCriarDocumentoComSucesso() {
    when(file.getSize()).thenReturn(1024L);
    when(documentRepository.save(any(Document.class))).thenReturn(document);

    Document savedDocument = documentService.createDocument("teste", "C:/uploads/teste.txt", file);

    assertNotNull(savedDocument);
    assertEquals("teste", savedDocument.getName());
    assertEquals("C:/uploads/teste.txt", savedDocument.getFilePath());
  }

  @Test
  void deveLancarExcecaoQuandoDocumentoNaoForEncontrado() {
    when(documentRepository.findByName("naoExiste")).thenReturn(Optional.empty());

    assertThrows(
        DocumentNotFoundException.class, () -> documentService.getDocumentByName("naoExiste"));
  }

  @Test
  void deveBuscarDocumentoPeloNome() {
    when(documentRepository.findByName("teste")).thenReturn(Optional.of(document));

    Path result = documentService.getDocumentByName("teste");

    assertNotNull(result);
    assertEquals("C:\\uploads\\teste.txt", result.toString());
  }

  @Test
  void deveAtualizarDocumentoComSucesso() {
    when(documentRepository.findByName("teste")).thenReturn(Optional.of(document));
    when(documentRepository.save(any(Document.class))).thenReturn(document);
    when(file.getOriginalFilename()).thenReturn("novoTeste");
    when(file.getSize()).thenReturn(1024L);

    Document updatedDocument = documentService.updateDocument("teste", "novoTeste", file);

    assertNotNull(updatedDocument);
    assertEquals("novoTeste", updatedDocument.getName());
  }
  @Test
  void deveDeletarDocumentoComSucesso() throws IOException {
    when(documentRepository.findByName("teste")).thenReturn(Optional.of(document));
    doNothing().when(documentRepository).delete(any(Document.class));

    documentService.deleteDocument("teste");

    verify(documentRepository, times(1)).delete(document);
  }


}
