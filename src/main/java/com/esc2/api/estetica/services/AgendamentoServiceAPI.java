package com.esc2.api.estetica.services;

import com.esc2.api.estetica.dtos.AgendamentoDto;
import com.esc2.api.estetica.dtos.response.AgendamentoResponseDto;
import com.esc2.api.estetica.models.AgendamentoModel;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface AgendamentoServiceAPI {

    AgendamentoResponseDto create(AgendamentoDto agendamentoDto);
    AgendamentoResponseDto update(UUID id, AgendamentoDto agendamentoDto);

    void delete(UUID id);
    AgendamentoResponseDto findById(UUID id);
    List<AgendamentoResponseDto> findAll();

    AgendamentoResponseDto concluirAgendamento(UUID id);
    AgendamentoResponseDto cancelarAgendamento(UUID id);

    List<AgendamentoModel> buscarAgendamentoPorPeriodo(Instant inicio, Instant fim);
    
}
