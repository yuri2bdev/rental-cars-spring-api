package com.challenge.rental_cars_spring_api.access;

import com.challenge.rental_cars_spring_api.core.service.GerarRelatorioPdfService;
import com.itextpdf.text.DocumentException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Tag(name = "Relatórios", description = "Endpoints para geração de relatórios em PDF")
@RestController
@RequestMapping("/api/relatorios")
@RequiredArgsConstructor
public class RelatoriosRestController {

    private final GerarRelatorioPdfService gerarRelatorioPdfService;

    @GetMapping("/alugueis")
    @Operation(summary = "Gera relatório de aluguéis em PDF")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Relatório gerado com sucesso", content = {
                    @Content(mediaType = MediaType.APPLICATION_PDF_VALUE)}),
            @ApiResponse(responseCode = "500", description = "Erro ao gerar relatório", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})})
    public ResponseEntity<byte[]> gerarRelatorioAlugueis() throws DocumentException, IOException {
        byte[] pdfBytes = gerarRelatorioPdfService.gerarRelatorioAlugueis();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.set("Content-Disposition", "attachment; filename=\"relatorio-alugueis.pdf\"");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    @GetMapping("/carros")
    @Operation(summary = "Gera relatório da frota de carros em PDF")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Relatório gerado com sucesso", content = {
                    @Content(mediaType = MediaType.APPLICATION_PDF_VALUE)}),
            @ApiResponse(responseCode = "500", description = "Erro ao gerar relatório", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})})
    public ResponseEntity<byte[]> gerarRelatorioCarros() throws DocumentException, IOException {
        byte[] pdfBytes = gerarRelatorioPdfService.gerarRelatorioCarros();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.set("Content-Disposition", "attachment; filename=\"relatorio-carros.pdf\"");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }
} 