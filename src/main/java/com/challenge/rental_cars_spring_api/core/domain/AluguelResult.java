package com.challenge.rental_cars_spring_api.core.domain;

public sealed interface AluguelResult {
    record Sucesso(Aluguel aluguel) implements AluguelResult {}
    record Erro(String mensagem, String linha) implements AluguelResult {}
} 