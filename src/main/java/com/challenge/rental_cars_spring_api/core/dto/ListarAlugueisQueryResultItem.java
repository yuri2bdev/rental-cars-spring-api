package com.challenge.rental_cars_spring_api.core.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ListarAlugueisQueryResultItem(
        LocalDate dataAluguel,
        String modeloCarro,
        Integer kmCarro,
        String nomeCliente,
        String telefoneCliente,
        LocalDate dataDevolucao,
        BigDecimal valor,
        String pago
) {}