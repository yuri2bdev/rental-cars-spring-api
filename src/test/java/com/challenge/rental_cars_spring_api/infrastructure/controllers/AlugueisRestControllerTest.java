package com.challenge.rental_cars_spring_api.infrastructure.controllers;

import com.challenge.rental_cars_spring_api.access.AlugueisRestController;
import com.challenge.rental_cars_spring_api.core.queries.ListarAlugueisQuery;
import com.challenge.rental_cars_spring_api.core.dto.ListarAlugueisQueryResultItem;
import com.challenge.rental_cars_spring_api.core.service.ArquivoRtnProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlugueisRestControllerTest {

    @Mock
    private ListarAlugueisQuery listarAlugueisQuery;

    @Mock
    private ArquivoRtnProcessor processarArquivoRtnService;

    @InjectMocks
    private AlugueisRestController controller;

    private ListarAlugueisQueryResultItem aluguel1;
    private ListarAlugueisQueryResultItem aluguel2;

    @BeforeEach
    void setUp() {
        LocalDate dataAluguel1 = LocalDate.of(2024, 3, 1);
        LocalDate dataDevolucao1 = LocalDate.of(2024, 3, 5);
        aluguel1 = new ListarAlugueisQueryResultItem(
            dataAluguel1,
            "Onix",
            10000,
            "João Silva",
            "(51) 99999-9999",
            dataDevolucao1,
            BigDecimal.valueOf(400.00),
            "SIM"
        );

        LocalDate dataAluguel2 = LocalDate.of(2024, 3, 10);
        LocalDate dataDevolucao2 = LocalDate.of(2024, 3, 15);
        aluguel2 = new ListarAlugueisQueryResultItem(
            dataAluguel2,
            "HB20",
            15000,
            "Maria Santos",
            "(51) 88888-8888",
            dataDevolucao2,
            BigDecimal.valueOf(500.00),
            "NAO"
        );
    }

    @Test
    void deveListarAlugueis() {
        ListarAlugueisQuery.Result queryResult = new ListarAlugueisQuery.Result(
            Arrays.asList(aluguel1, aluguel2),
            BigDecimal.valueOf(500.00)
        );
        when(listarAlugueisQuery.execute()).thenReturn(queryResult);

        ResponseEntity<ListarAlugueisQuery.Result> response = controller.listarAlugueis();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        
        ListarAlugueisQuery.Result resultado = response.getBody();
        assertNotNull(resultado);
        assertEquals(2, resultado.alugueis().size());
        
        ListarAlugueisQueryResultItem primeiroAluguel = resultado.alugueis().get(0);
        assertEquals(aluguel1.nomeCliente(), primeiroAluguel.nomeCliente());
        assertEquals(aluguel1.telefoneCliente(), primeiroAluguel.telefoneCliente());
        assertEquals(aluguel1.modeloCarro(), primeiroAluguel.modeloCarro());
        assertEquals(aluguel1.dataAluguel(), primeiroAluguel.dataAluguel());
        assertEquals(aluguel1.valor(), primeiroAluguel.valor());
        assertEquals(aluguel1.pago(), primeiroAluguel.pago());

        ListarAlugueisQueryResultItem segundoAluguel = resultado.alugueis().get(1);
        assertEquals(aluguel2.nomeCliente(), segundoAluguel.nomeCliente());
        assertEquals(aluguel2.telefoneCliente(), segundoAluguel.telefoneCliente());
        assertEquals(aluguel2.modeloCarro(), segundoAluguel.modeloCarro());
        assertEquals(aluguel2.dataAluguel(), segundoAluguel.dataAluguel());
        assertEquals(aluguel2.valor(), segundoAluguel.valor());
        assertEquals(aluguel2.pago(), segundoAluguel.pago());

        verify(listarAlugueisQuery, times(1)).execute();
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHouverAlugueis() {
        ListarAlugueisQuery.Result queryResult = new ListarAlugueisQuery.Result(List.of(), BigDecimal.ZERO);
        when(listarAlugueisQuery.execute()).thenReturn(queryResult);

        ResponseEntity<ListarAlugueisQuery.Result> response = controller.listarAlugueis();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        
        ListarAlugueisQuery.Result resultado = response.getBody();
        assertNotNull(resultado);
        assertTrue(resultado.alugueis().isEmpty());
        
        verify(listarAlugueisQuery, times(1)).execute();
    }

    @Test
    void deveProcessarArquivoRtn() throws Exception {
        String conteudoArquivo = "0105202312345678901000010000100.00";
        MockMultipartFile arquivo = new MockMultipartFile(
            "arquivo.rtn",
            "arquivo.rtn",
            "text/plain",
            conteudoArquivo.getBytes(StandardCharsets.UTF_8)
        );

        when(processarArquivoRtnService.processarAsync(any())).thenReturn(CompletableFuture.completedFuture(null));
        
        CompletableFuture<ResponseEntity<Void>> response = controller.processarArquivo(arquivo);

        assertNotNull(response);
        ResponseEntity<Void> responseEntity = response.get();
        assertEquals(202, responseEntity.getStatusCodeValue());
        verify(processarArquivoRtnService, times(1)).processarAsync(any());
    }
} 