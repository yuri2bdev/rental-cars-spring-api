package com.challenge.rental_cars_spring_api.access.exception;

import com.challenge.rental_cars_spring_api.core.service.exception.ArquivoProcessamentoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ArquivoProcessamentoException.class)
    public ResponseEntity<Map<String, String>> handleArquivoProcessamentoException(ArquivoProcessamentoException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("erro", ex.getMessage());
        response.put("tipo", "Erro no processamento do arquivo");
        
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
        Map<String, String> response = new HashMap<>();
        response.put("erro", "Ocorreu um erro interno");
        response.put("mensagem", ex.getMessage());
        
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
} 