package com.challenge.rental_cars_spring_api.core.dto;

import com.challenge.rental_cars_spring_api.core.domain.Aluguel;
import org.springframework.stereotype.Component;

@Component
public class AluguelMapper {

    public ListarAlugueisQueryResultItem toListarAlugueisQueryResultItem(Aluguel aluguel) {
        return new ListarAlugueisQueryResultItem(
                aluguel.getDataAluguel(),
                aluguel.getCarro().getModelo(),
                aluguel.getCarro().getKm(),
                aluguel.getCliente().getNome(),
                formatarTelefone(aluguel.getCliente().getTelefone()),
                aluguel.getDataDevolucao(),
                aluguel.getValor(),
                aluguel.isPago() ? "SIM" : "NAO"
        );
    }

    private String formatarTelefone(String telefone) {
        if (telefone == null || telefone.length() != 11) {
            return telefone;
        }
        return String.format("(%s) %s-%s",
                telefone.substring(0, 2),
                telefone.substring(2, 7),
                telefone.substring(7));
    }
} 