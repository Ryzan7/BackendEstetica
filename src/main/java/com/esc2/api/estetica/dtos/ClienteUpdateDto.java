package com.esc2.api.estetica.dtos;

import org.hibernate.validator.constraints.br.CPF;

public record ClienteUpdateDto(
        String nome,

        String telefone,

        @CPF
        String cpf
) {
}