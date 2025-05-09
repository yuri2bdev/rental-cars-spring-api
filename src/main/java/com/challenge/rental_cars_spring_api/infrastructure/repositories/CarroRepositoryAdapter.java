package com.challenge.rental_cars_spring_api.infrastructure.repositories;

import com.challenge.rental_cars_spring_api.core.domain.Carro;
import com.challenge.rental_cars_spring_api.core.port.CarroRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CarroRepositoryAdapter implements CarroRepositoryPort {

    private final CarroRepository carroRepository;

    @Override
    public List<Carro> buscarTodos() {
        return carroRepository.findAll();
    }

    @Override
    public Optional<Carro> buscarPorId(Long id) {
        return carroRepository.findById(id);
    }

    @Override
    public boolean existePorId(Long id) {
        return carroRepository.existsById(id);
    }
} 