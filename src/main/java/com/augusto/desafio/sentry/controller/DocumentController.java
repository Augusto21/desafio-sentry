package com.augusto.desafio.sentry.controller;

import com.augusto.desafio.sentry.config.DocumentStorageConfig;
import com.augusto.desafio.sentry.model.Document;
import com.augusto.desafio.sentry.service.DocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    String filePath = targetLocation + File.separator + file.getOriginalFilename();

    file.transferTo(targetLocation);

    return ResponseEntity.status(HttpStatus.CREATED)
        .contentType(MediaType.APPLICATION_JSON) // Define o cabeçalho Content-Type
        .body(documentoService.createDocument(name, filePath, file));
  }
}
