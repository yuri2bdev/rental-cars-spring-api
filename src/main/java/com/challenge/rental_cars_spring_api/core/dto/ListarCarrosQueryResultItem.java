package com.challenge.rental_cars_spring_api.core.dto;

import java.math.BigDecimal;

public record ListarCarrosQueryResultItem(
    String modelo,
    String ano,
    Integer qtdPassageiros,
    Integer km,
    String fabricante,
    BigDecimal vlrDiaria
) {}
