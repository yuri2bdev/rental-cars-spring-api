package com.challenge.rental_cars_spring_api.infrastructure.repositories;

import com.challenge.rental_cars_spring_api.core.domain.Carro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarroRepository extends JpaRepository<Carro, Long> {
}
