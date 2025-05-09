package com.challenge.rental_cars_spring_api.core.queries.dtos;

import com.challenge.rental_cars_spring_api.core.domain.Carro;
import com.challenge.rental_cars_spring_api.core.dto.CarroMapper;
import com.challenge.rental_cars_spring_api.core.dto.ListarCarrosQueryResultItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CarroMapperTest {

    private CarroMapper mapper;
    private Carro carro;

    @BeforeEach
    void setUp() {
        mapper = new CarroMapper();
        
        carro = Carro.builder()
                .id(1L)
                .modelo("Onix")
                .ano("2023")
                .qtdPassageiros(5)
                .km(10000)
                .fabricante("Chevrolet")
                .vlrDiaria(BigDecimal.valueOf(100.00))
                .build();
    }

    @Test
    void deveMapearCarroParaDTO() {
        ListarCarrosQueryResultItem dto = mapper.toListarCarrosQueryResultItem(carro);
        
        assertNotNull(dto);
        assertEquals("Onix", dto.modelo());
        assertEquals("2023", dto.ano());
        assertEquals(5, dto.qtdPassageiros());
        assertEquals(10000, dto.km());
        assertEquals("Chevrolet", dto.fabricante());
        assertEquals(BigDecimal.valueOf(100.00), dto.vlrDiaria());
    }
} 