package com.esc2.api.estetica.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ClienteRecordDto(
        @NotBlank(message = "O nome não pode ser vazio")
        String nome,

        @NotBlank(message = "O telefone não pode ser vazio")
        String telefone,

        @NotBlank(message = "O CPF não pode ser vazio")
        @Pattern(regexp = "\\d{11}", message = "O CPF deve conter exatamente 11 dígitos")
        String cpf
) {
}
