package com.challenge.rental_cars_spring_api.access;

import com.challenge.rental_cars_spring_api.core.queries.ListarAlugueisQuery;
import com.challenge.rental_cars_spring_api.core.dto.ListarAlugueisQueryResultItem;
import com.challenge.rental_cars_spring_api.core.service.ArquivoRtnProcessor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AlugueisRestControllerTest {

    @Mock
    private ListarAlugueisQuery listarAlugueisQuery;

    @Mock
    private ArquivoRtnProcessor processarArquivoRtnService;

    @InjectMocks
    private AlugueisRestController controller;

    @Test
    void deveListarAlugueis() {
        var aluguel = new ListarAlugueisQueryResultItem(
            LocalDate.now(),
            "Onix",
            10000,
            "João Silva",
            "(51) 99999-9999",
            LocalDate.now().plusDays(5),
            BigDecimal.valueOf(100.00),
            "NAO"
        );
        var queryResult = new ListarAlugueisQuery.Result(
            List.of(aluguel),
            BigDecimal.valueOf(100.00)
        );
        when(listarAlugueisQuery.execute()).thenReturn(queryResult);

        var response = controller.listarAlugueis();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().alugueis().size());
        assertEquals(BigDecimal.valueOf(100.00), response.getBody().valorTotalNaoPago());
    }

    @Test
    void deveProcessarArquivo() throws Exception {
        MockMultipartFile arquivo = new MockMultipartFile(
            "arquivo",
            "arquivo.rtn",
            MediaType.TEXT_PLAIN_VALUE,
            "conteudo".getBytes()
        );
        when(processarArquivoRtnService.processarAsync(any()))
            .thenReturn(CompletableFuture.completedFuture(null));

        var response = controller.processarArquivo(arquivo);

        assertNotNull(response);
        assertEquals(HttpStatus.ACCEPTED, response.get().getStatusCode());
    }
} 