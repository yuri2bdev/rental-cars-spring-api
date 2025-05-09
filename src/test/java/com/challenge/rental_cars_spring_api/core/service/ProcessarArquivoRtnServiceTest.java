package com.challenge.rental_cars_spring_api.core.service;

import com.challenge.rental_cars_spring_api.core.domain.Aluguel;
import com.challenge.rental_cars_spring_api.core.domain.Carro;
import com.challenge.rental_cars_spring_api.core.domain.Cliente;
import com.challenge.rental_cars_spring_api.core.port.AluguelRepositoryPort;
import com.challenge.rental_cars_spring_api.core.service.exception.ArquivoProcessamentoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProcessarArquivoRtnServiceTest {

    @Mock
    private AluguelRepositoryPort aluguelRepository;

    @Mock
    private LinhaRtnParser linhaRtnParser;

    @InjectMocks
    private ProcessarArquivoRtnService service;

    private Aluguel aluguelValido;
    private String conteudoArquivo;

    @BeforeEach
    void setUp() {
        Cliente cliente = Cliente.builder()
                .id(1L)
                .nome("João Silva")
                .cpf("12345678901")
                .cnh("98765432100")
                .telefone("5199999999")
                .build();
                
        Carro carro = Carro.builder()
                .id(1L)
                .modelo("Onix")
                .ano("2023")
                .qtdPassageiros(5)
                .km(10000)
                .fabricante("Chevrolet")
                .vlrDiaria(BigDecimal.valueOf(100.00))
                .build();
        
        aluguelValido = Aluguel.builder()
                .id(1L)
                .carro(carro)
                .cliente(cliente)
                .dataAluguel(LocalDate.now())
                .dataDevolucao(LocalDate.now().plusDays(5))
                .valor(BigDecimal.valueOf(500.00))
                .pago(false)
                .build();
        
        conteudoArquivo = "0105202312345678901000010000100.00";
    }

    @Test
    void deveProcessarArquivoComSucesso() throws IOException {
        MockMultipartFile arquivo = new MockMultipartFile(
            "arquivo.rtn",
            "arquivo.rtn",
            "text/plain",
            conteudoArquivo.getBytes(StandardCharsets.UTF_8)
        );

        when(linhaRtnParser.processarLinha(anyString())).thenReturn(aluguelValido);
        when(aluguelRepository.salvarTodos(anyList())).thenReturn(List.of(aluguelValido));

        service.processar(arquivo);

        verify(aluguelRepository, times(1)).salvarTodos(anyList());
        verify(linhaRtnParser, times(1)).processarLinha(anyString());
    }

    @Test
    void deveIgnorarLinhaInvalidaDoArquivo() throws IOException {
        String conteudoInvalido = "LINHA_INVALIDA\n" + conteudoArquivo;
        MockMultipartFile arquivo = new MockMultipartFile(
            "arquivo.rtn",
            "arquivo.rtn",
            "text/plain",
            conteudoInvalido.getBytes(StandardCharsets.UTF_8)
        );

        when(linhaRtnParser.processarLinha(anyString())).thenReturn(null).thenReturn(aluguelValido);
        when(aluguelRepository.salvarTodos(anyList())).thenReturn(List.of(aluguelValido));

        service.processar(arquivo);

        verify(aluguelRepository, times(1)).salvarTodos(anyList());
        verify(linhaRtnParser, times(2)).processarLinha(anyString());
    }

    @Test
    void deveLancarExcecaoQuandoNaoHouverAlugueis() throws IOException {
        MockMultipartFile arquivo = new MockMultipartFile(
            "arquivo.rtn",
            "arquivo.rtn",
            "text/plain",
            "".getBytes(StandardCharsets.UTF_8)
        );

        assertThrows(ArquivoProcessamentoException.class, () -> {
            service.processar(arquivo);
        });

        verify(aluguelRepository, never()).salvarTodos(anyList());
    }
} 