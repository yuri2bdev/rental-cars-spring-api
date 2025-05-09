package com.challenge.rental_cars_spring_api.infrastructure.repositories;

import com.challenge.rental_cars_spring_api.core.domain.Aluguel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;

public interface AluguelRepository extends JpaRepository<Aluguel, Long> {
    
    @Query("SELECT SUM(a.valor) FROM Aluguel a WHERE a.pago = false")
    BigDecimal sumValorNaoPago();
} 