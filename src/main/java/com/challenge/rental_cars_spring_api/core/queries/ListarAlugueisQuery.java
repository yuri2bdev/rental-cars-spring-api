package com.challenge.rental_cars_spring_api.core.queries;

import com.challenge.rental_cars_spring_api.core.port.AluguelRepositoryPort;
import com.challenge.rental_cars_spring_api.core.dto.AluguelMapper;
import com.challenge.rental_cars_spring_api.core.dto.ListarAlugueisQueryResultItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ListarAlugueisQuery {

    private final AluguelRepositoryPort aluguelRepository;
    private final AluguelMapper aluguelMapper;

    public record Result(List<ListarAlugueisQueryResultItem> alugueis, BigDecimal valorTotalNaoPago) {}

    public Result execute() {
        var alugueis = aluguelRepository.buscarTodos().stream()
                .map(aluguelMapper::toListarAlugueisQueryResultItem)
                .toList();
        
        BigDecimal valorTotal = aluguelRepository.somarValorNaoPago();
        BigDecimal valorTotalNaoPago = valorTotal != null ? valorTotal : BigDecimal.ZERO;
        
        return new Result(alugueis, valorTotalNaoPago);
    }
} 