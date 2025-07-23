package com.esc2.api.estetica.services.impl;

import com.esc2.api.estetica.dtos.AgendamentoDto;
import com.esc2.api.estetica.enums.StatusAgendamentoEnum;
import com.esc2.api.estetica.exceptions.NotFoundException;
import com.esc2.api.estetica.models.AgendamentoModel;
import com.esc2.api.estetica.models.ClienteModel;
import com.esc2.api.estetica.models.ServicoModel;
import com.esc2.api.estetica.repositories.AgendamentoRepository;
import com.esc2.api.estetica.repositories.ClienteRepository;
import com.esc2.api.estetica.repositories.ServicoRepository;
import com.esc2.api.estetica.services.AgendamentoServiceAPI;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public class AgendamentoServiceImpl implements AgendamentoServiceAPI {
    AgendamentoRepository agendamentoRepository;
    ClienteRepository clienteRepository;
    ServicoRepository servicoRepository;

    public AgendamentoServiceImpl(AgendamentoRepository agendamentoRepository, ClienteRepository clienteRepository, ServicoRepository servicoRepository) {
        this.agendamentoRepository = agendamentoRepository;
        this.clienteRepository = clienteRepository;
        this.servicoRepository = servicoRepository;
    }

    @Override
    public AgendamentoModel create(AgendamentoDto agendamentoDto) {
        ClienteModel cliente = clienteRepository.findById(agendamentoDto.cliente())
                .orElseThrow( () -> new NotFoundException("Cliente não encontrado"));

        AgendamentoModel agendamento = new AgendamentoModel();
        agendamento.setCliente(cliente);
        agendamento.setDataHora(agendamentoDto.dataHora());
        agendamento.setStatus(StatusAgendamentoEnum.AGENDADO);

        for(UUID servicoId : agendamentoDto.servicosID()){
            ServicoModel servico = servicoRepository.findById(servicoId)
                    .orElseThrow(() -> new NotFoundException("Servico com o id: " + servicoId + "não encontrado"));
            agendamento.adicionarServico(servico,servico.getValor(),servico.getDuracao());

        }

        AgendamentoModel agendamentoSalvo = agendamentoRepository.save(agendamento);
        return agendamentoSalvo;
    }
}
