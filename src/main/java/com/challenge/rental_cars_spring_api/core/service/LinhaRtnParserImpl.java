package com.challenge.rental_cars_spring_api.core.service;

import com.challenge.rental_cars_spring_api.core.domain.Aluguel;
import com.challenge.rental_cars_spring_api.core.domain.Carro;
import com.challenge.rental_cars_spring_api.core.domain.Cliente;
import com.challenge.rental_cars_spring_api.core.port.CarroRepositoryPort;
import com.challenge.rental_cars_spring_api.core.port.ClienteRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Slf4j
@Service
@RequiredArgsConstructor
public class LinhaRtnParserImpl implements LinhaRtnParser {

    private final CarroRepositoryPort carroRepository;
    private final ClienteRepositoryPort clienteRepository;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    @Override
    public Aluguel processarLinha(String linha) {
        try {
            String carroId = linha.substring(0, 2);
            String clienteId = linha.substring(2, 4);
            String dataAluguelStr = linha.substring(4, 12);
            String dataDevolucaoStr = linha.substring(12, 20);

            LocalDate dataAluguel = validarData(dataAluguelStr, "aluguel");
            if (dataAluguel == null) {
                return null;
            }

            LocalDate dataDevolucao = validarData(dataDevolucaoStr, "devolução");
            if (dataDevolucao == null) {
                return null;
            }

            Long carroIdLong = validarId(carroId, "carro");
            if (carroIdLong == null || !carroRepository.existePorId(carroIdLong)) {
                log.warn("Carro não encontrado: {}", carroId);
                return null;
            }

            Long clienteIdLong = validarId(clienteId, "cliente");
            if (clienteIdLong == null || !clienteRepository.existePorId(clienteIdLong)) {
                log.warn("Cliente não encontrado: {}", clienteId);
                return null;
            }

            Carro carro = carroRepository.buscarPorId(carroIdLong).get();
            Cliente cliente = clienteRepository.buscarPorId(clienteIdLong).get();

            long diasAluguel = java.time.temporal.ChronoUnit.DAYS.between(dataAluguel, dataDevolucao);
            BigDecimal valor = carro.getVlrDiaria().multiply(BigDecimal.valueOf(diasAluguel));

            return Aluguel.builder()
                .carro(carro)
                .cliente(cliente)
                .dataAluguel(dataAluguel)
                .dataDevolucao(dataDevolucao)
                .valor(valor)
                .pago(false)
                .build();
        } catch (Exception e) {
            log.warn("Erro ao processar linha: {} - {}", linha, e.getMessage());
            return null;
        }
    }

    private LocalDate validarData(String dataStr, String tipo) {
        try {
            LocalDate data = LocalDate.parse(dataStr, DATE_FORMATTER);
            if (data.getMonthValue() < 1 || data.getMonthValue() > 12 ||
                data.getDayOfMonth() < 1 || data.getDayOfMonth() > data.getMonth().maxLength()) {
                log.warn("Data de {} inválida: {}", tipo, dataStr);
                return null;
            }
            return data;
        } catch (DateTimeParseException e) {
            log.warn("Erro ao processar data de {}: {} - {}", tipo, dataStr, e.getMessage());
            return null;
        }
    }

    private Long validarId(String idStr, String tipo) {
        try {
            Long id = Long.parseLong(idStr);
            if (id <= 0) {
                log.warn("ID do {} inválido: {}", tipo, idStr);
                return null;
            }
            return id;
        } catch (NumberFormatException e) {
            log.warn("Erro ao processar ID do {}: {} - {}", tipo, idStr, e.getMessage());
            return null;
        }
    }
} 