package com.esc2.api.estetica.dtos.response;

import com.esc2.api.estetica.dtos.resumes.ClienteResumoDto;
import com.esc2.api.estetica.enums.StatusAgendamentoEnum;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record AgendamentoResponseDto(
        UUID id,
        Instant dataHoraAgendamento,
        StatusAgendamentoEnum status,
        ClienteResumoDto cliente, // Objeto aninhado
        List<AgendamentoServicosDto> servicosAgendados,
        BigDecimal valorTotal,
        Integer duracaoTotal,
        boolean desconto
) {
}
