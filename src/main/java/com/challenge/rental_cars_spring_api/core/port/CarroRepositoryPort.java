package com.challenge.rental_cars_spring_api.core.port;

import com.challenge.rental_cars_spring_api.core.domain.Carro;

import java.util.List;
import java.util.Optional;

public interface CarroRepositoryPort {

    List<Carro> buscarTodos();

    Optional<Carro> buscarPorId(Long id);

    boolean existePorId(Long id);
} 