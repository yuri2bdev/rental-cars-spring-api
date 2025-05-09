package com.challenge.rental_cars_spring_api.infrastructure.repositories;

import com.challenge.rental_cars_spring_api.core.domain.Aluguel;
import com.challenge.rental_cars_spring_api.core.port.AluguelRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AluguelRepositoryAdapter implements AluguelRepositoryPort {

    private final AluguelRepository aluguelRepository;

    @Override
    public List<Aluguel> buscarTodos() {
        return aluguelRepository.findAll();
    }

    @Override
    public List<Aluguel> salvarTodos(List<Aluguel> alugueis) {
        return aluguelRepository.saveAll(alugueis);
    }

    @Override
    public BigDecimal somarValorNaoPago() {
        return aluguelRepository.sumValorNaoPago();
    }
} 