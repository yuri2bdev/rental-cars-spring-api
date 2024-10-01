package com.challenge.rental_cars_spring_api.core.queries.dtos;

import com.challenge.rental_cars_spring_api.core.domain.Carro;

public record ListarCarrosQueryResultItem(Long id, String modelo) {

    public static ListarCarrosQueryResultItem from(Carro carro) {
        return new ListarCarrosQueryResultItem(carro.getId(), carro.getModelo());
    }
}
