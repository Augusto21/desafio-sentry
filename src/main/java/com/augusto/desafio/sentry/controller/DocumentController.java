package com.augusto.desafio.sentry.controller;

import com.augusto.desafio.sentry.config.DocumentStorageConfig;
import com.augusto.desafio.sentry.model.Document;
import com.augusto.desafio.sentry.service.DocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Controller
@RequestMapping("/documentos")
public class DocumentController {

  private final Path fileStorageLocation;

  private final DocumentService documentoService;

  public DocumentController(
      DocumentService documentoService, DocumentStorageConfig documentStorageConfig) {
    this.documentoService = documentoService;
    this.fileStorageLocation =
        Paths.get(documentStorageConfig.getUploadDir()).toAbsolutePath().normalize();
  }

  @PostMapping("/upload")
  @Operation(
      summary = "Cria um novo documento",
      description = "Cria um documento e armazena o arquivo")
  @ApiResponse(responseCode = "201", description = "Documento criado com sucesso")
  public ResponseEntity<Document> createDocumento(
      @RequestParam(value = "name") String name, @RequestParam(value = "file") MultipartFile file)
      throws IOException {

    String fileName = StringUtils.cleanPath(file.getOriginalFilename());

    Path targetLocation = fileStorageLocation.resolve(fileName);
    log.info("Salvando o Documento no caminho: {}", fileStorageLocation);

    file.transferTo(targetLocation);

    return ResponseEntity.status(HttpStatus.CREATED)
        .contentType(MediaType.APPLICATION_JSON)
        .body(documentoService.createDocument(name, targetLocation.toString(), file));
  }

  @GetMapping("/download")
  @Operation(
      summary = "Obtém um documento pelo name",
      description = "Retorna o arquivo do documento se encontrado")
  @ApiResponse(responseCode = "200", description = "Arquivo do documento retornado com sucesso")
  @ApiResponse(responseCode = "404", description = "Documento não encontrado")
  public ResponseEntity<Resource> createDocumento(
      @RequestParam(value = "name") String fileName, HttpServletRequest request) {
    try {
      Path filePath = documentoService.getDocumentByName(fileName);
      Resource resource = new UrlResource(filePath.toUri());

      String contentType =
          request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
      if (contentType == null) {
        contentType = "application/octet-stream";
      }

      return ResponseEntity.ok()
          .contentType(MediaType.parseMediaType(contentType))
          .header(
              HttpHeaders.CONTENT_DISPOSITION,
              "attachment; filename=\"" + resource.getFilename() + "\"")
          .body(resource);
    } catch (MalformedURLException e) {
      log.error("URL malformada para o arquivo: {}", fileName);
      return ResponseEntity.badRequest().body(null);
    } catch (Exception e) {
      log.error("Erro inesperado ao processar o download do arquivo: {}", fileName, e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
  }

  @DeleteMapping("/delete")
  @Operation(summary = "Deleta um documento", description = "Deleta um documento pelo nome")
  @ApiResponse(responseCode = "200", description = "Documento deletado com sucesso")
  @ApiResponse(responseCode = "404", description = "Documento não encontrado")
  public ResponseEntity<String> deleteDocumento(@RequestParam(value = "name") String fileName) {
    try {
      Path filePath = documentoService.getDocumentByName(fileName);
      Files.deleteIfExists(filePath);
      documentoService.deleteDocument(fileName);
      log.info("Documento deletado: {}", fileName);
      return ResponseEntity.ok("Documento deletado com sucesso");
    } catch (IOException e) {
      log.error("Erro ao deletar o documento: {}", fileName, e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao deletar o documento");
    }
  }

  @PutMapping("/update")
  @Operation(summary = "Atualiza um documento", description = "Atualiza um documento existente")
  @ApiResponse(responseCode = "200", description = "Documento atualizado com sucesso")
  @ApiResponse(responseCode = "404", description = "Documento não encontrado")
  public ResponseEntity<Document> updateDocumento(
          @RequestParam(value = "name") String fileName,
          @RequestParam(value = "newName", required = false) String newName,
          @RequestParam(value = "file", required = false) MultipartFile file) {
    try {
      Document updatedDocument = documentoService.updateDocument(fileName, newName, file);
      log.info("Documento atualizado: {}", fileName);
      return ResponseEntity.ok(updatedDocument);
    } catch (Exception e) {
      log.error("Erro ao atualizar o documento: {}", fileName, e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
  }

}
