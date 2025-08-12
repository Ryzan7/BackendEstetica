package com.esc2.api.estetica.dtos.resumes;

import java.math.BigDecimal;
import java.util.UUID;

public record ServicoResumoDto(
        UUID id,
        String nome,
        BigDecimal valorAtual,
        Integer duracao
) {
}
