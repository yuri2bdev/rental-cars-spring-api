package com.challenge.rental_cars_spring_api.core.service.exception;

public class ArquivoProcessamentoException extends RuntimeException {
    
    public ArquivoProcessamentoException(String mensagem) {
        super(mensagem);
    }
    
    public ArquivoProcessamentoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
} 