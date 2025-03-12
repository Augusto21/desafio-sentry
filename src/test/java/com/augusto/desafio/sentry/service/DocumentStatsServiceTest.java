package com.augusto.desafio.sentry.service;

import static org.mockito.Mockito.*;

import com.augusto.desafio.sentry.repository.DocumentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DocumentStatsServiceTest {
  @Mock private DocumentRepository documentRepository;

  @InjectMocks private DocumentStatsService documentStatsService;

  @BeforeEach
  void setUp() {
    when(documentRepository.count()).thenReturn(10L);
    when(documentRepository.somarTamanhoArquivos()).thenReturn(500000L);
  }

  @Test
  void testLogStorageStatistics() {
    documentStatsService.logStorageStatistics();

    verify(documentRepository, times(1)).count();
    verify(documentRepository, times(1)).somarTamanhoArquivos();
  }
}
