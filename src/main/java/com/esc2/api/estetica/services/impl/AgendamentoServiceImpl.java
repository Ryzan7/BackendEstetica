package com.esc2.api.estetica.services.impl;

import com.esc2.api.estetica.dtos.AgendamentoDto;
import com.esc2.api.estetica.dtos.response.AgendamentoResponseDto;
import com.esc2.api.estetica.enums.StatusAgendamentoEnum;
import com.esc2.api.estetica.exceptions.NotFoundException;
import com.esc2.api.estetica.mappers.AgendamentoMapper;
import com.esc2.api.estetica.models.AgendamentoModel;
import com.esc2.api.estetica.models.ClienteModel;
import com.esc2.api.estetica.models.ServicoModel;
import com.esc2.api.estetica.repositories.AgendamentoRepository;
import com.esc2.api.estetica.repositories.ClienteRepository;
import com.esc2.api.estetica.repositories.ServicoRepository;
import com.esc2.api.estetica.services.AgendamentoServiceAPI;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
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
    public AgendamentoResponseDto create(AgendamentoDto agendamentoDto) {
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

        AgendamentoModel agendamentoModel = agendamentoRepository.save(agendamento);


        return AgendamentoMapper.toResponseDto(agendamentoModel);
    }

    @Override
    public AgendamentoResponseDto findById(UUID id) {
        return AgendamentoMapper.toResponseDto(agendamentoRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Agendamento não encontrado")));
    }

    @Override
    public List<AgendamentoResponseDto> findAll() {
        return AgendamentoMapper.toResponseDtoList(agendamentoRepository.findAll());
    }


    // TODO Lançar exceção quando o Status do agendamento estiver CONCLUIDO -> Não pode excluir nem editar
    @Override
    public AgendamentoResponseDto update(UUID id, AgendamentoDto agendamentoDto) {
        AgendamentoModel agendamentoEncontrado = agendamentoRepository.findById(id).orElseThrow( () -> new NotFoundException("Agendamento não encontrado"));

        BeanUtils.copyProperties(agendamentoDto, agendamentoEncontrado,"id");

        AgendamentoModel agendamentoModel = agendamentoRepository.save(agendamentoEncontrado);

        return AgendamentoMapper.toResponseDto(agendamentoModel);

    }

    @Override
    public void delete(UUID id) {

        agendamentoRepository.findById(id).orElseThrow(() -> new NotFoundException("Agendamento não encontrado"));

        agendamentoRepository.deleteById(id);
    }

    
    //TODO Alterar Status do Agendamento
    // TODO Finalizar Agendamento -> Confirmar os serviços prestados e depois mudar status para CONCLUIDO



}
