package com.esc2.api.estetica.services;

import com.esc2.api.estetica.dtos.AgendamentoDto;
import com.esc2.api.estetica.models.AgendamentoModel;
import com.esc2.api.estetica.models.AgendamentoServicos;
import com.esc2.api.estetica.repositories.AgendamentoRepository;

public interface AgendamentoServiceAPI {

    AgendamentoModel create(AgendamentoDto agendamentoDto);

}
