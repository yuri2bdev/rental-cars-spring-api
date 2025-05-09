package com.challenge.rental_cars_spring_api.core.port;

import com.challenge.rental_cars_spring_api.core.domain.Cliente;

import java.util.List;
import java.util.Optional;

public interface ClienteRepositoryPort {

    List<Cliente> buscarTodos();

    Optional<Cliente> buscarPorId(Long id);

    Optional<Cliente> buscarPorCpf(String cpf);

    boolean existePorId(Long id);
} 