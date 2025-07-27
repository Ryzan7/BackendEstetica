package com.esc2.api.estetica.dtos.resumes;

import java.util.UUID;

public record ClienteResumoDto(
        UUID id,
        String nome
) {
}
