package com.challenge.rental_cars_spring_api.core.queries;

import com.challenge.rental_cars_spring_api.core.domain.Aluguel;
import com.challenge.rental_cars_spring_api.core.domain.Carro;
import com.challenge.rental_cars_spring_api.core.domain.Cliente;
import com.challenge.rental_cars_spring_api.core.port.AluguelRepositoryPort;
import com.challenge.rental_cars_spring_api.core.dto.AluguelMapper;
import com.challenge.rental_cars_spring_api.core.dto.ListarAlugueisQueryResultItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListarAlugueisQueryTest {

    @Mock
    private AluguelRepositoryPort aluguelRepository;
    
    @Mock
    private AluguelMapper aluguelMapper;

    @InjectMocks
    private ListarAlugueisQuery query;

    private Aluguel aluguel1;
    private Aluguel aluguel2;
    private ListarAlugueisQueryResultItem resultItem1;
    private ListarAlugueisQueryResultItem resultItem2;

    @BeforeEach
    void setUp() {
        Carro carro = Carro.builder()
                .id(1L)
                .modelo("Onix")
                .ano("2023")
                .qtdPassageiros(5)
                .km(10000)
                .fabricante("Chevrolet")
                .vlrDiaria(BigDecimal.valueOf(100.00))
                .build();
                
        Cliente cliente = Cliente.builder()
                .id(1L)
                .nome("João Silva")
                .cpf("12345678901")
                .cnh("98765432100")
                .telefone("5199999999")
                .build();
        
        LocalDate dataAluguel1 = LocalDate.of(2024, 3, 1);
        LocalDate dataDevolucao1 = LocalDate.of(2024, 3, 5);
        aluguel1 = Aluguel.builder()
                .id(1L)
                .carro(carro)
                .cliente(cliente)
                .dataAluguel(dataAluguel1)
                .dataDevolucao(dataDevolucao1)
                .valor(BigDecimal.valueOf(400.00))
                .pago(true)
                .build();

        LocalDate dataAluguel2 = LocalDate.of(2024, 3, 10);
        LocalDate dataDevolucao2 = LocalDate.of(2024, 3, 15);
        aluguel2 = Aluguel.builder()
                .id(2L)
                .carro(carro)
                .cliente(cliente)
                .dataAluguel(dataAluguel2)
                .dataDevolucao(dataDevolucao2)
                .valor(BigDecimal.valueOf(500.00))
                .pago(false)
                .build();
                
        resultItem1 = new ListarAlugueisQueryResultItem(
                dataAluguel1,
                "Onix",
                10000,
                "João Silva",
                "(51) 99999-9999",
                dataDevolucao1,
                BigDecimal.valueOf(400.00),
                "SIM"
        );
        
        resultItem2 = new ListarAlugueisQueryResultItem(
                dataAluguel2,
                "Onix",
                10000,
                "João Silva",
                "(51) 99999-9999",
                dataDevolucao2,
                BigDecimal.valueOf(500.00),
                "NAO"
        );
    }

    @Test
    void deveRetornarListaDeAlugueis() {
        when(aluguelRepository.buscarTodos()).thenReturn(Arrays.asList(aluguel1, aluguel2));
        when(aluguelMapper.toListarAlugueisQueryResultItem(aluguel1)).thenReturn(resultItem1);
        when(aluguelMapper.toListarAlugueisQueryResultItem(aluguel2)).thenReturn(resultItem2);
        when(aluguelRepository.somarValorNaoPago()).thenReturn(BigDecimal.valueOf(500.00));

        ListarAlugueisQuery.Result resultado = query.execute();

        assertNotNull(resultado);
        assertEquals(2, resultado.alugueis().size());
        
        assertEquals(resultItem1, resultado.alugueis().get(0));
        assertEquals(resultItem2, resultado.alugueis().get(1));
        assertEquals(BigDecimal.valueOf(500.00), resultado.valorTotalNaoPago());

        verify(aluguelRepository, times(1)).buscarTodos();
        verify(aluguelRepository, times(1)).somarValorNaoPago();
        verify(aluguelMapper, times(1)).toListarAlugueisQueryResultItem(aluguel1);
        verify(aluguelMapper, times(1)).toListarAlugueisQueryResultItem(aluguel2);
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHouverAlugueis() {
        when(aluguelRepository.buscarTodos()).thenReturn(List.of());
        when(aluguelRepository.somarValorNaoPago()).thenReturn(null);

        ListarAlugueisQuery.Result resultado = query.execute();

        assertNotNull(resultado);
        assertTrue(resultado.alugueis().isEmpty());
        assertEquals(BigDecimal.ZERO, resultado.valorTotalNaoPago());
        
        verify(aluguelRepository, times(1)).buscarTodos();
        verify(aluguelRepository, times(1)).somarValorNaoPago();
        verifyNoInteractions(aluguelMapper);
    }
    
    @Test
    void deveCalcularValorTotalNaoPagoQuandoValorForZero() {
        when(aluguelRepository.buscarTodos()).thenReturn(List.of(aluguel1));
        when(aluguelMapper.toListarAlugueisQueryResultItem(aluguel1)).thenReturn(resultItem1);
        when(aluguelRepository.somarValorNaoPago()).thenReturn(BigDecimal.ZERO);

        ListarAlugueisQuery.Result resultado = query.execute();

        assertNotNull(resultado);
        assertEquals(1, resultado.alugueis().size());
        assertEquals(BigDecimal.ZERO, resultado.valorTotalNaoPago());
        
        verify(aluguelRepository, times(1)).buscarTodos();
        verify(aluguelRepository, times(1)).somarValorNaoPago();
    }
} 