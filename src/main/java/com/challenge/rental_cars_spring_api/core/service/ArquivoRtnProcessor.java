package com.challenge.rental_cars_spring_api.core.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.CompletableFuture;

public interface ArquivoRtnProcessor {

    CompletableFuture<Void> processarAsync(MultipartFile arquivo);

    void processar(MultipartFile arquivo);
} 