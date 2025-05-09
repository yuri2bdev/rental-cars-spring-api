package com.challenge.rental_cars_spring_api.core.port;

import com.challenge.rental_cars_spring_api.core.domain.Aluguel;

import java.math.BigDecimal;
import java.util.List;

public interface AluguelRepositoryPort {

    List<Aluguel> buscarTodos();

    List<Aluguel> salvarTodos(List<Aluguel> alugueis);

    BigDecimal somarValorNaoPago();
} 