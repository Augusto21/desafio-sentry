package com.augusto.desafio.sentry.service;

import com.augusto.desafio.sentry.model.Document;
import com.augusto.desafio.sentry.repository.DocumentRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentStatsService {
  private final DocumentRepository documentoRepository;
  @Scheduled(cron = "0 0 0 * * *")
  public void logStorageStatistics() {
    long totalArquivos = documentoRepository.count();
    long totalBytes = documentoRepository.somarTamanhoArquivos();

    log.info("📊 Estatísticas de armazenamento:");
    log.info("📂 Total de documentos armazenados: {}", totalArquivos);
    log.info("💾 Espaço total ocupado: {} bytes ({} KB, {} MB)", totalBytes, totalBytes / 1024, totalBytes / (1024 * 1024));
  }
}
