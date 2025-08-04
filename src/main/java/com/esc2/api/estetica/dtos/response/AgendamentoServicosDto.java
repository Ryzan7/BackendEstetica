package com.esc2.api.estetica.dtos.response;

import com.esc2.api.estetica.dtos.resumes.ServicoResumoDto;

import java.math.BigDecimal;

public record AgendamentoServicosDto(
        Long id,
        BigDecimal valorCobrado,
        ServicoResumoDto servico
) {
}
