package com.challenge.rental_cars_spring_api.core.service;

import com.challenge.rental_cars_spring_api.core.domain.Aluguel;
import com.challenge.rental_cars_spring_api.core.port.AluguelRepositoryPort;
import com.challenge.rental_cars_spring_api.core.service.exception.ArquivoProcessamentoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProcessarArquivoRtnService implements ArquivoRtnProcessor {

    private final AluguelRepositoryPort aluguelRepository;
    private final LinhaRtnParser linhaRtnParser;
    
    @Override
    @Async
    public CompletableFuture<Void> processarAsync(MultipartFile arquivo) {
        return CompletableFuture.runAsync(() -> processar(arquivo));
    }

    @Override
    @Transactional
    public void processar(MultipartFile arquivo) {
        if (arquivo == null || arquivo.isEmpty()) {
            throw new ArquivoProcessamentoException("Arquivo não fornecido ou vazio");
        }
        
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(arquivo.getInputStream()))) {
            List<Aluguel> alugueis = reader.lines()
                    .filter(linha -> linha != null && !linha.trim().isEmpty())
                    .map(linha -> {
                        try {
                            return linhaRtnParser.processarLinha(linha);
                        } catch (Exception e) {
                            log.warn("Erro ao processar linha: {}", e.getMessage());
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .toList();

            if (alugueis.isEmpty()) {
                throw new ArquivoProcessamentoException("Nenhum aluguel válido encontrado no arquivo");
            }

            aluguelRepository.salvarTodos(alugueis);
            log.info("Arquivo processado com sucesso. {} aluguéis salvos.", alugueis.size());
        } catch (ArquivoProcessamentoException e) {
            log.error("Erro no processamento do arquivo RTN", e);
            throw e;
        } catch (Exception e) {
            log.error("Erro ao processar arquivo RTN", e);
            throw new ArquivoProcessamentoException("Erro ao processar arquivo RTN", e);
        }
    }
} 