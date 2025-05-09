package com.challenge.rental_cars_spring_api.core.queries.dtos;

import com.challenge.rental_cars_spring_api.core.domain.Aluguel;
import com.challenge.rental_cars_spring_api.core.domain.Carro;
import com.challenge.rental_cars_spring_api.core.domain.Cliente;
import com.challenge.rental_cars_spring_api.core.dto.AluguelMapper;
import com.challenge.rental_cars_spring_api.core.dto.ListarAlugueisQueryResultItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AluguelMapperTest {

    private AluguelMapper mapper;
    private Aluguel aluguel;
    private LocalDate dataAluguel;
    private LocalDate dataDevolucao;

    @BeforeEach
    void setUp() {
        mapper = new AluguelMapper();
        
        Carro carro = Carro.builder()
                .id(1L)
                .modelo("Onix")
                .ano("2023")
                .qtdPassageiros(5)
                .km(10000)
                .fabricante("Chevrolet")
                .vlrDiaria(BigDecimal.valueOf(100.00))
                .build();
                
        Cliente cliente = Cliente.builder()
                .id(1L)
                .nome("João Silva")
                .cpf("12345678901")
                .cnh("98765432100")
                .telefone("51999999999")
                .build();
        
        dataAluguel = LocalDate.of(2024, 3, 1);
        dataDevolucao = LocalDate.of(2024, 3, 5);
        aluguel = Aluguel.builder()
                .id(1L)
                .carro(carro)
                .cliente(cliente)
                .dataAluguel(dataAluguel)
                .dataDevolucao(dataDevolucao)
                .valor(BigDecimal.valueOf(400.00))
                .pago(true)
                .build();
    }

    @Test
    void deveMapearAluguelParaDTO() {
        ListarAlugueisQueryResultItem dto = mapper.toListarAlugueisQueryResultItem(aluguel);
        
        assertNotNull(dto);
        assertEquals(dataAluguel, dto.dataAluguel());
        assertEquals("Onix", dto.modeloCarro());
        assertEquals(10000, dto.kmCarro());
        assertEquals("João Silva", dto.nomeCliente());
        assertEquals("(51) 99999-9999", dto.telefoneCliente());
        assertEquals(dataDevolucao, dto.dataDevolucao());
        assertEquals(BigDecimal.valueOf(400.00), dto.valor());
        assertEquals("SIM", dto.pago());
    }
    
    @Test
    void deveFormatarTelefoneCorretamente() {
        ListarAlugueisQueryResultItem dto = mapper.toListarAlugueisQueryResultItem(aluguel);
        
        assertEquals("(51) 99999-9999", dto.telefoneCliente());
    }
    
    @Test
    void deveRetornarTelefoneSemFormatacaoQuandoTamanhoForIncorreto() {
        Cliente clienteComTelefoneInvalido = Cliente.builder()
                .id(1L)
                .nome("João Silva")
                .cpf("12345678901")
                .cnh("98765432100")
                .telefone("519999999")
                .build();
                
        Aluguel aluguelComTelefoneInvalido = Aluguel.builder()
                .id(1L)
                .carro(aluguel.getCarro())
                .cliente(clienteComTelefoneInvalido)
                .dataAluguel(dataAluguel)
                .dataDevolucao(dataDevolucao)
                .valor(BigDecimal.valueOf(400.00))
                .pago(true)
                .build();
        
        ListarAlugueisQueryResultItem dto = mapper.toListarAlugueisQueryResultItem(aluguelComTelefoneInvalido);
        
        assertEquals("519999999", dto.telefoneCliente());
    }
} 