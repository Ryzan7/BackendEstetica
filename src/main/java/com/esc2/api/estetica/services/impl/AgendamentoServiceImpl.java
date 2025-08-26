package com.esc2.api.estetica.services.impl;

import com.esc2.api.estetica.dtos.AgendamentoDto;
import com.esc2.api.estetica.dtos.response.AgendamentoResponseDto;
import com.esc2.api.estetica.enums.StatusAgendamentoEnum;
import com.esc2.api.estetica.exceptions.AgendamentoConcluidoException;
import com.esc2.api.estetica.exceptions.NotFoundException;
import com.esc2.api.estetica.mappers.AgendamentoMapper;
import com.esc2.api.estetica.models.AgendamentoModel;
import com.esc2.api.estetica.models.AgendamentoServicos;
import com.esc2.api.estetica.models.ClienteModel;
import com.esc2.api.estetica.models.ServicoModel;
import com.esc2.api.estetica.repositories.AgendamentoRepository;
import com.esc2.api.estetica.repositories.ClienteRepository;
import com.esc2.api.estetica.repositories.ServicoRepository;
import com.esc2.api.estetica.services.AgendamentoServiceAPI;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;


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

        if(agendamentoDto.tipoDesconto()!=null && agendamentoDto.valorDesconto()!=null){
            BigDecimal valorDoDescontoCalculado = agendamento.getValorFinalComDesconto(agendamentoDto.valorDesconto(), agendamentoDto.tipoDesconto());

            agendamento.setValorDescontoAplicado(agendamentoDto.valorDesconto());
            agendamento.setTipoDescontoAplicado(agendamentoDto.tipoDesconto());


            agendamento.setValorTotal(valorDoDescontoCalculado);
            agendamento.setDesconto(true);

        }else{
            agendamento.setValorTotal(agendamento.calculaValorTotal());
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

    @Override
    public AgendamentoResponseDto concluirAgendamento(UUID id) {
        AgendamentoModel agendamentoEncontrado = agendamentoRepository.findById(id)
                .orElseThrow(
                        () -> new NotFoundException("Agendamento não encontrado"));

        agendamentoEncontrado.setStatus(StatusAgendamentoEnum.CONCLUIDO);
        AgendamentoModel agendamentoAtualizado =  agendamentoRepository.save(agendamentoEncontrado);

        return AgendamentoMapper.toResponseDto(agendamentoAtualizado);
    }

    public AgendamentoResponseDto cancelarAgendamento(UUID id) {

        AgendamentoModel agendamentoEncontrado = agendamentoRepository.findById(id)
                .orElseThrow(
                        () -> new NotFoundException("Agendamento não encontrado"));

        if(agendamentoEncontrado.getStatus() == StatusAgendamentoEnum.CONCLUIDO){
            throw new AgendamentoConcluidoException();
        }

        agendamentoEncontrado.setStatus(StatusAgendamentoEnum.CANCELADO);
        AgendamentoModel agendamentoAtualizado =  agendamentoRepository.save(agendamentoEncontrado);

        return AgendamentoMapper.toResponseDto(agendamentoAtualizado);
    }


    @Override
    @Transactional
    public AgendamentoResponseDto update(UUID id, AgendamentoDto agendamentoDto) {
        AgendamentoModel agendamentoEncontrado = agendamentoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Agendamento não encontrado"));

        if(agendamentoEncontrado.getStatus() == StatusAgendamentoEnum.CONCLUIDO){
            throw new AgendamentoConcluidoException();
        }

        if (agendamentoDto.dataHora() != null) {
            agendamentoEncontrado.setDataHora(agendamentoDto.dataHora());
        }


        if(agendamentoDto.observacoes() != null){
            agendamentoEncontrado.setObservacoes(agendamentoDto.observacoes());
        }

        if(agendamentoDto.tipoDesconto()!=null || agendamentoDto.valorDesconto()!=null){
            BigDecimal valorDoDescontoCalculado = agendamentoEncontrado.getValorFinalComDesconto(agendamentoDto.valorDesconto(), agendamentoDto.tipoDesconto());

            agendamentoEncontrado.setValorDescontoAplicado(agendamentoDto.valorDesconto());
            agendamentoEncontrado.setTipoDescontoAplicado(agendamentoDto.tipoDesconto());


            agendamentoEncontrado.setValorTotal(valorDoDescontoCalculado);
            agendamentoEncontrado.setDesconto(true);

        }
        
        if (agendamentoDto.cliente() != null) {
            ClienteModel novoCliente = clienteRepository.findById(agendamentoDto.cliente())
                    .orElseThrow(() -> new NotFoundException("Cliente com o ID fornecido não encontrado"));
            agendamentoEncontrado.setCliente(novoCliente);
        }

        if (agendamentoDto.servicosID() != null) {
            Set<UUID> idDosServicosEnviados = new HashSet<>(agendamentoDto.servicosID());
            Set<AgendamentoServicos> servicosAtuais = agendamentoEncontrado.getServicosAgendados();

            servicosAtuais.removeIf(item -> !idDosServicosEnviados.contains(item.getServico().getServicosId()));

            Set<UUID> idsDosServicosAtuais = servicosAtuais.stream()
                    .map(item -> item.getServico().getServicosId())
                    .collect(Collectors.toSet());


            for (UUID idNovoServico : idDosServicosEnviados) {
                if (!idsDosServicosAtuais.contains(idNovoServico)) {
                    ServicoModel servicoParaAdicionar = servicoRepository.findById(idNovoServico)
                            .orElseThrow(() -> new NotFoundException("Serviço com o id: " + idNovoServico + " não encontrado"));
                    agendamentoEncontrado.adicionarServico(servicoParaAdicionar, servicoParaAdicionar.getValor(), servicoParaAdicionar.getDuracao());
                }
            }

            agendamentoEncontrado.setStatus(StatusAgendamentoEnum.AGENDADO);
        }

        AgendamentoModel agendamentoAtualizado = agendamentoRepository.save(agendamentoEncontrado);

        return AgendamentoMapper.toResponseDto(agendamentoAtualizado);
    }


    @Override
    public void delete(UUID id) {
        AgendamentoModel agendamentoEncontrado = agendamentoRepository.getReferenceById(id);

        if(agendamentoEncontrado.getStatus() == StatusAgendamentoEnum.CONCLUIDO){
            throw new AgendamentoConcluidoException();
        }

        agendamentoRepository.findById(id).orElseThrow(() -> new NotFoundException("Agendamento não encontrado"));
        agendamentoRepository.deleteById(id);
    }


    @Override
    public List<AgendamentoModel> buscarAgendamentoPorPeriodo(Instant inicio, Instant fim) {
        return agendamentoRepository.findAllByDataHoraBetween(inicio,fim);
    }
    
    // TODO Finalizar Agendamento -> Confirmar os serviços prestados e depois mudar status para CONCLUIDO


}
