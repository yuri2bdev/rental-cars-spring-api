package com.challenge.rental_cars_spring_api.core.queries;

import com.challenge.rental_cars_spring_api.core.port.CarroRepositoryPort;
import com.challenge.rental_cars_spring_api.core.dto.CarroMapper;
import com.challenge.rental_cars_spring_api.core.dto.ListarCarrosQueryResultItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListarCarrosQuery {

    private final CarroRepositoryPort carroRepository;
    private final CarroMapper carroMapper;

    public List<ListarCarrosQueryResultItem> execute() {
        return carroRepository.buscarTodos().stream()
                .map(carroMapper::toListarCarrosQueryResultItem)
                .toList();
    }
}
