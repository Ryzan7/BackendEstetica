package com.esc2.api.estetica.services;

import com.esc2.api.estetica.dtos.AgendamentoDto;
import com.esc2.api.estetica.dtos.response.AgendamentoResponseDto;

import java.util.List;
import java.util.UUID;

public interface AgendamentoServiceAPI {

    AgendamentoResponseDto create(AgendamentoDto agendamentoDto);
    AgendamentoResponseDto update(UUID id, AgendamentoDto agendamentoDto);

    void delete(UUID id);
    AgendamentoResponseDto findById(UUID id);
    List<AgendamentoResponseDto> findAll();


}
