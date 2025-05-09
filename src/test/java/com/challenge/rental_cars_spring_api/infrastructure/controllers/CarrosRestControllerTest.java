package com.challenge.rental_cars_spring_api.infrastructure.controllers;

import com.challenge.rental_cars_spring_api.access.CarrosRestController;
import com.challenge.rental_cars_spring_api.core.queries.ListarCarrosQuery;
import com.challenge.rental_cars_spring_api.core.dto.ListarCarrosQueryResultItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarrosRestControllerTest {

    @Mock
    private ListarCarrosQuery listarCarrosQuery;

    @InjectMocks
    private CarrosRestController controller;

    private ListarCarrosQueryResultItem carro1;
    private ListarCarrosQueryResultItem carro2;

    @BeforeEach
    void setUp() {
        carro1 = new ListarCarrosQueryResultItem(
            "Onix",
            "2024",
            5,
            10000,
            "Chevrolet",
            BigDecimal.valueOf(100.00)
        );

        carro2 = new ListarCarrosQueryResultItem(
            "HB20",
            "2024",
            5,
            15000,
            "Hyundai",
            BigDecimal.valueOf(120.00)
        );
    }

    @Test
    void deveListarCarros() {
        List<ListarCarrosQueryResultItem> carros = Arrays.asList(carro1, carro2);
        when(listarCarrosQuery.execute()).thenReturn(carros);

        ResponseEntity<List<ListarCarrosQueryResultItem>> response = controller.listarCarros();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        
        List<ListarCarrosQueryResultItem> resultado = response.getBody();
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        
        ListarCarrosQueryResultItem primeiroCarro = resultado.get(0);
        assertEquals(carro1.modelo(), primeiroCarro.modelo());
        assertEquals(carro1.ano(), primeiroCarro.ano());
        assertEquals(carro1.qtdPassageiros(), primeiroCarro.qtdPassageiros());
        assertEquals(carro1.km(), primeiroCarro.km());
        assertEquals(carro1.fabricante(), primeiroCarro.fabricante());
        assertEquals(carro1.vlrDiaria(), primeiroCarro.vlrDiaria());

        ListarCarrosQueryResultItem segundoCarro = resultado.get(1);
        assertEquals(carro2.modelo(), segundoCarro.modelo());
        assertEquals(carro2.ano(), segundoCarro.ano());
        assertEquals(carro2.qtdPassageiros(), segundoCarro.qtdPassageiros());
        assertEquals(carro2.km(), segundoCarro.km());
        assertEquals(carro2.fabricante(), segundoCarro.fabricante());
        assertEquals(carro2.vlrDiaria(), segundoCarro.vlrDiaria());

        verify(listarCarrosQuery, times(1)).execute();
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHouverCarros() {
        when(listarCarrosQuery.execute()).thenReturn(List.of());

        ResponseEntity<List<ListarCarrosQueryResultItem>> response = controller.listarCarros();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        
        List<ListarCarrosQueryResultItem> resultado = response.getBody();
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        
        verify(listarCarrosQuery, times(1)).execute();
    }
} 