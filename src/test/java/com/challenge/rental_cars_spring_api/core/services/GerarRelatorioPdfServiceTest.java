package com.challenge.rental_cars_spring_api.core.services;

import com.challenge.rental_cars_spring_api.core.queries.ListarAlugueisQuery;
import com.challenge.rental_cars_spring_api.core.queries.ListarCarrosQuery;
import com.challenge.rental_cars_spring_api.core.dto.ListarAlugueisQueryResultItem;
import com.challenge.rental_cars_spring_api.core.dto.ListarCarrosQueryResultItem;
import com.challenge.rental_cars_spring_api.core.service.GerarRelatorioPdfService;
import com.itextpdf.text.DocumentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GerarRelatorioPdfServiceTest {

    @Mock
    private ListarAlugueisQuery listarAlugueisQuery;

    @Mock
    private ListarCarrosQuery listarCarrosQuery;

    @InjectMocks
    private GerarRelatorioPdfService service;

    private ListarAlugueisQueryResultItem aluguel1;
    private ListarAlugueisQueryResultItem aluguel2;
    private ListarCarrosQueryResultItem carro1;
    private ListarCarrosQueryResultItem carro2;

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
    void deveGerarRelatorioAlugueis() throws DocumentException {
        ListarAlugueisQuery.Result queryResult = new ListarAlugueisQuery.Result(
            Arrays.asList(aluguel1, aluguel2),
            BigDecimal.valueOf(500.00)
        );
        when(listarAlugueisQuery.execute()).thenReturn(queryResult);

        byte[] pdfBytes = service.gerarRelatorioAlugueis();

        assertNotNull(pdfBytes);
        assertTrue(pdfBytes.length > 0);
        verify(listarAlugueisQuery, times(1)).execute();
    }

    @Test
    void deveGerarRelatorioCarros() throws DocumentException {
        when(listarCarrosQuery.execute()).thenReturn(Arrays.asList(carro1, carro2));

        byte[] pdfBytes = service.gerarRelatorioCarros();

        assertNotNull(pdfBytes);
        assertTrue(pdfBytes.length > 0);
        verify(listarCarrosQuery, times(1)).execute();
    }
} 