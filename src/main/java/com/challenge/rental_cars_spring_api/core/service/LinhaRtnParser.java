package com.challenge.rental_cars_spring_api.core.service;

import com.challenge.rental_cars_spring_api.core.domain.Aluguel;

public interface LinhaRtnParser {

    Aluguel processarLinha(String linha);
} 