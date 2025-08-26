package com.esc2.api.estetica.repositories;

import com.esc2.api.estetica.dtos.response.AgendamentoResponseDto;
import com.esc2.api.estetica.models.AgendamentoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public interface AgendamentoRepository extends JpaRepository<AgendamentoModel, UUID> {
    List<AgendamentoModel> findAllByDataHoraBetween(Instant dataInicial, Instant dataFinal);}
