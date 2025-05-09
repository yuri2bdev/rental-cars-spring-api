package com.challenge.rental_cars_spring_api.core.queries;

import com.challenge.rental_cars_spring_api.core.domain.Carro;
import com.challenge.rental_cars_spring_api.core.port.CarroRepositoryPort;
import com.challenge.rental_cars_spring_api.core.dto.CarroMapper;
import com.challenge.rental_cars_spring_api.core.dto.ListarCarrosQueryResultItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListarCarrosQueryTest {

    @Mock
    private CarroRepositoryPort carroRepository;
    
    @Mock
    private CarroMapper carroMapper;

    @InjectMocks
    private ListarCarrosQuery query;

    private Carro carro1;
    private Carro carro2;
    private ListarCarrosQueryResultItem resultItem1;
    private ListarCarrosQueryResultItem resultItem2;

    @BeforeEach
    void setUp() {
        carro1 = Carro.builder()
                .id(1L)
                .modelo("Onix")
                .ano("2023")
                .qtdPassageiros(5)
                .km(10000)
                .fabricante("Chevrolet")
                .vlrDiaria(BigDecimal.valueOf(100.00))
                .build();
                
        carro2 = Carro.builder()
                .id(2L)
                .modelo("HB20")
                .ano("2023")
                .qtdPassageiros(5)
                .km(15000)
                .fabricante("Hyundai")
                .vlrDiaria(BigDecimal.valueOf(120.00))
                .build();
                
        resultItem1 = new ListarCarrosQueryResultItem(
                "Onix",
                "2023",
                5,
                10000,
                "Chevrolet",
                BigDecimal.valueOf(100.00)
        );
        
        resultItem2 = new ListarCarrosQueryResultItem(
                "HB20",
                "2023",
                5,
                15000,
                "Hyundai",
                BigDecimal.valueOf(120.00)
        );
    }

    @Test
    void deveRetornarListaDeCarros() {
        when(carroRepository.buscarTodos()).thenReturn(Arrays.asList(carro1, carro2));
        when(carroMapper.toListarCarrosQueryResultItem(carro1)).thenReturn(resultItem1);
        when(carroMapper.toListarCarrosQueryResultItem(carro2)).thenReturn(resultItem2);

        List<ListarCarrosQueryResultItem> resultado = query.execute();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        
        assertEquals(resultItem1, resultado.get(0));
        assertEquals(resultItem2, resultado.get(1));

        verify(carroRepository, times(1)).buscarTodos();
        verify(carroMapper, times(1)).toListarCarrosQueryResultItem(carro1);
        verify(carroMapper, times(1)).toListarCarrosQueryResultItem(carro2);
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHouverCarros() {
        when(carroRepository.buscarTodos()).thenReturn(List.of());

        List<ListarCarrosQueryResultItem> resultado = query.execute();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(carroRepository, times(1)).buscarTodos();
        verifyNoInteractions(carroMapper);
    }
} 