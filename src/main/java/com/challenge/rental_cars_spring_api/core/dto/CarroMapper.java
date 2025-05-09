package com.challenge.rental_cars_spring_api.core.dto;

import com.challenge.rental_cars_spring_api.core.domain.Carro;
import org.springframework.stereotype.Component;

@Component
public class CarroMapper {

    public ListarCarrosQueryResultItem toListarCarrosQueryResultItem(Carro carro) {
        return new ListarCarrosQueryResultItem(
                carro.getModelo(),
                carro.getAno(),
                carro.getQtdPassageiros(),
                carro.getKm(),
                carro.getFabricante(),
                carro.getVlrDiaria()
        );
    }
} 