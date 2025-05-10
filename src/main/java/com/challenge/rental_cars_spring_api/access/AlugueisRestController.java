package com.challenge.rental_cars_spring_api.access;

import com.challenge.rental_cars_spring_api.core.queries.ListarAlugueisQuery;
import com.challenge.rental_cars_spring_api.core.service.ArquivoRtnProcessor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/alugueis")
@RequiredArgsConstructor
public class AlugueisRestController {

    private final ListarAlugueisQuery listarAlugueisQuery;
    private final ArquivoRtnProcessor processarArquivoRtnService;

    @GetMapping
    @Operation(summary = "Lista todos os aluguéis")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna a lista de aluguéis e o valor total não pago", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ListarAlugueisQuery.Result.class))}),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})})
    public ResponseEntity<ListarAlugueisQuery.Result> listarAlugueis() {
        return new ResponseEntity<>(listarAlugueisQuery.execute(), HttpStatus.OK);
    }

    @PostMapping("/processar-arquivo")
    @Operation(summary = "Processa arquivo .rtn de aluguéis")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Arquivo aceito para processamento"),
            @ApiResponse(responseCode = "400", description = "Erro no formato do arquivo"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor")})
    public CompletableFuture<ResponseEntity<Void>> processarArquivo(@RequestParam("arquivo") MultipartFile arquivo) throws IOException {
        return processarArquivoRtnService.processarAsync(arquivo)
            .thenApply(v -> ResponseEntity.accepted().build());
    }
} 