package com.challenge.rental_cars_spring_api.core.services;

import com.challenge.rental_cars_spring_api.core.domain.Aluguel;
import com.challenge.rental_cars_spring_api.core.domain.Carro;
import com.challenge.rental_cars_spring_api.core.domain.Cliente;
import com.challenge.rental_cars_spring_api.core.port.CarroRepositoryPort;
import com.challenge.rental_cars_spring_api.core.port.ClienteRepositoryPort;
import com.challenge.rental_cars_spring_api.core.service.LinhaRtnParserImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LinhaRtnParserImplTest {

    @Mock
    private CarroRepositoryPort carroRepository;

    @Mock
    private ClienteRepositoryPort clienteRepository;

    @InjectMocks
    private LinhaRtnParserImpl linhaRtnParser;

    private Carro carro;
    private Cliente cliente;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    @BeforeEach
    void setUp() {
        carro = Carro.builder()
                .id(1L)
                .modelo("Onix")
                .ano("2023")
                .qtdPassageiros(5)
                .km(10000)
                .fabricante("Chevrolet")
                .vlrDiaria(BigDecimal.valueOf(100.00))
                .build();
                
        cliente = Cliente.builder()
                .id(2L)
                .nome("João Silva")
                .cpf("12345678901")
                .cnh("98765432100")
                .telefone("5199999999")
                .build();
    }

    @Test
    void deveProcessarLinhaValida() {
        // Formato da linha: carroId(2) + clienteId(2) + dataAluguel(8) + dataDevolucao(8)
        String linha = "0102" + LocalDate.now().format(DATE_FORMATTER) + LocalDate.now().plusDays(5).format(DATE_FORMATTER);
        
        when(carroRepository.existePorId(1L)).thenReturn(true);
        when(clienteRepository.existePorId(2L)).thenReturn(true);
        when(carroRepository.buscarPorId(1L)).thenReturn(Optional.of(carro));
        when(clienteRepository.buscarPorId(2L)).thenReturn(Optional.of(cliente));

        Aluguel aluguel = linhaRtnParser.processarLinha(linha);

        assertNotNull(aluguel);
        assertEquals(carro, aluguel.getCarro());
        assertEquals(cliente, aluguel.getCliente());
        assertEquals(LocalDate.now(), aluguel.getDataAluguel());
        assertEquals(LocalDate.now().plusDays(5), aluguel.getDataDevolucao());
        assertEquals(BigDecimal.valueOf(500.00), aluguel.getValor());
        assertFalse(aluguel.isPago());
        
        verify(carroRepository, times(1)).existePorId(1L);
        verify(clienteRepository, times(1)).existePorId(2L);
        verify(carroRepository, times(1)).buscarPorId(1L);
        verify(clienteRepository, times(1)).buscarPorId(2L);
    }

    @Test
    void deveRetornarNullParaLinhaInvalida() {
        String linha = "linha_invalida";
        
        Aluguel aluguel = linhaRtnParser.processarLinha(linha);
        
        assertNull(aluguel);
        verifyNoInteractions(carroRepository, clienteRepository);
    }

    @Test
    void deveRetornarNullQuandoCarroNaoExiste() {
        String linha = "0102" + LocalDate.now().format(DATE_FORMATTER) + LocalDate.now().plusDays(5).format(DATE_FORMATTER);
        
        when(carroRepository.existePorId(1L)).thenReturn(false);
        
        Aluguel aluguel = linhaRtnParser.processarLinha(linha);
        
        assertNull(aluguel);
        verify(carroRepository, times(1)).existePorId(1L);
        verifyNoInteractions(clienteRepository);
    }

    @Test
    void deveRetornarNullQuandoClienteNaoExiste() {
        String linha = "0102" + LocalDate.now().format(DATE_FORMATTER) + LocalDate.now().plusDays(5).format(DATE_FORMATTER);
        
        when(carroRepository.existePorId(1L)).thenReturn(true);
        when(clienteRepository.existePorId(2L)).thenReturn(false);
        
        Aluguel aluguel = linhaRtnParser.processarLinha(linha);
        
        assertNull(aluguel);
        verify(carroRepository, times(1)).existePorId(1L);
        verify(clienteRepository, times(1)).existePorId(2L);
    }
    
    @Test
    void deveRetornarNullParaDataInvalida() {
        String linha = "0102" + "99999999" + LocalDate.now().plusDays(5).format(DATE_FORMATTER);
        
        Aluguel aluguel = linhaRtnParser.processarLinha(linha);
        
        assertNull(aluguel);
        verifyNoInteractions(carroRepository, clienteRepository);
    }
} 