package com.esc2.api.estetica.dtos;

import com.esc2.api.estetica.enums.StatusAgendamentoEnum;
import com.esc2.api.estetica.enums.TipoDesconto;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record AgendamentoDto(
        @NotNull(message = "O Id do cliente é obrigatorio")
        UUID cliente,

        @NotNull(message = "A data e hora do agendamento são obrigatórias.")
        @Future(message = "A data do agendamento deve ser no futuro.")
        Instant dataHora,

        @NotEmpty(message = "Um agendamento deve ter pelo menos um serviço.")
        List<UUID> servicosID,

        boolean desconto,

        BigDecimal valorDesconto,
        TipoDesconto tipoDesconto
) {
}
