package com.esc2.api.estetica.mappers;

import com.esc2.api.estetica.dtos.response.AgendamentoResponseDto;
import com.esc2.api.estetica.dtos.response.AgendamentoServicosDto;
import com.esc2.api.estetica.dtos.resumes.ClienteResumoDto;
import com.esc2.api.estetica.dtos.resumes.ServicoResumoDto;
import com.esc2.api.estetica.models.AgendamentoModel;
import com.esc2.api.estetica.models.AgendamentoServicos;
import com.esc2.api.estetica.models.ClienteModel;
import com.esc2.api.estetica.models.ServicoModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class AgendamentoMapper {

    public static AgendamentoResponseDto toResponseDto(AgendamentoModel agendamento) {
        if (agendamento == null) {
            return null;
        }

        ClienteResumoDto clienteDto = toClienteResumoDto(agendamento.getCliente());

        List<AgendamentoServicosDto> servicosAgendadosDto;
        if(agendamento.getServicosAgendados() == null){
            servicosAgendadosDto = Collections.emptyList();
        }else{
            servicosAgendadosDto = agendamento.getServicosAgendados().stream()
            .map(AgendamentoMapper::toAgendamentoServicoDto)
                    .collect(Collectors.toList());
        }

        return new AgendamentoResponseDto(
                agendamento.getAgendamentoId(),
                agendamento.getDataHora(),
                agendamento.getStatus(),
                clienteDto,
                servicosAgendadosDto,
                agendamento.calculaValorTotal(),
                agendamento.calculaDuracaoTotal()
        );


    }

    public static List<AgendamentoResponseDto> toResponseDtoList(List<AgendamentoModel> agendamentos) {
        if (agendamentos == null) {
            return Collections.emptyList();
        }

        return agendamentos.stream()
                .map(AgendamentoMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    private static AgendamentoServicosDto toAgendamentoServicoDto(AgendamentoServicos item) {
        if (item == null) {
            return null;
        }

        ServicoResumoDto servicoDto = toServicoResumoDto(item.getServico());

        return new AgendamentoServicosDto(
                item.getId(),
                item.getValorTotal(),
                servicoDto
        );
    }

    private static ServicoResumoDto toServicoResumoDto(ServicoModel servico) {
        if (servico == null) {
            return null;
        }
        return new ServicoResumoDto(servico.getServicosId(), servico.getNome(),servico.getValor(),servico.getDuracao());
    }


    private static ClienteResumoDto toClienteResumoDto(ClienteModel cliente) {
        if (cliente == null) {
            return null;
        }
        return new ClienteResumoDto(cliente.getClienteId(), cliente.getNome());
    }
}
