package com.challenge.rental_cars_spring_api.access;

import com.challenge.rental_cars_spring_api.core.queries.ListarCarrosQuery;
import com.challenge.rental_cars_spring_api.core.dto.ListarCarrosQueryResultItem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarrosRestControllerTest {

    @Mock
    private ListarCarrosQuery listarCarrosQuery;

    @InjectMocks
    private CarrosRestController controller;

    @Test
    void deveListarCarros() {
        var carro = new ListarCarrosQueryResultItem(
            "Onix",
            "2023",
            5,
            10000,
            "Chevrolet",
            BigDecimal.valueOf(100.00)
        );
        when(listarCarrosQuery.execute()).thenReturn(List.of(carro));

        var response = controller.listarCarros();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());

        var primeiroCarro = response.getBody().get(0);
        assertEquals("Onix", primeiroCarro.modelo());
        assertEquals("2023", primeiroCarro.ano());
        assertEquals(5, primeiroCarro.qtdPassageiros());
        assertEquals(10000, primeiroCarro.km());
        assertEquals("Chevrolet", primeiroCarro.fabricante());
        assertEquals(BigDecimal.valueOf(100.00), primeiroCarro.vlrDiaria());
    }

    @Test
    void deveRetornarListaVazia() {
        when(listarCarrosQuery.execute()).thenReturn(List.of());

        var response = controller.listarCarros();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(0, response.getBody().size());
    }
} 