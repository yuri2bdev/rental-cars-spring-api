package com.challenge.rental_cars_spring_api.access;

import com.challenge.rental_cars_spring_api.core.service.GerarRelatorioPdfService;
import com.itextpdf.text.DocumentException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RelatoriosRestControllerTest {

    @Mock
    private GerarRelatorioPdfService gerarRelatorioPdfService;

    @InjectMocks
    private RelatoriosRestController controller;

    @Test
    void deveGerarRelatorioAlugueis() throws DocumentException, IOException {
        byte[] pdfBytes = new byte[]{1, 2, 3};
        when(gerarRelatorioPdfService.gerarRelatorioAlugueis()).thenReturn(pdfBytes);

        ResponseEntity<byte[]> response = controller.gerarRelatorioAlugueis();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(pdfBytes, response.getBody());
        
        HttpHeaders headers = response.getHeaders();
        assertEquals(MediaType.APPLICATION_PDF_VALUE, headers.getContentType().toString());
        assertEquals("attachment; filename=\"relatorio-alugueis.pdf\"", headers.getContentDisposition().toString());
        
        verify(gerarRelatorioPdfService, times(1)).gerarRelatorioAlugueis();
    }

    @Test
    void deveGerarRelatorioCarros() throws DocumentException, IOException {
        byte[] pdfBytes = new byte[]{1, 2, 3};
        when(gerarRelatorioPdfService.gerarRelatorioCarros()).thenReturn(pdfBytes);

        ResponseEntity<byte[]> response = controller.gerarRelatorioCarros();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(pdfBytes, response.getBody());
        
        HttpHeaders headers = response.getHeaders();
        assertEquals(MediaType.APPLICATION_PDF_VALUE, headers.getContentType().toString());
        assertEquals("attachment; filename=\"relatorio-carros.pdf\"", headers.getContentDisposition().toString());
        
        verify(gerarRelatorioPdfService, times(1)).gerarRelatorioCarros();
    }
} 