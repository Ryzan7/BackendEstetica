package com.esc2.api.estetica.dtos;

import com.esc2.api.estetica.enums.CargoEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.br.CPF;

public record ProfissionalRecordDto(

    @NotBlank(message = "O nome não pode ser vazio")
    String nome,

    @NotBlank(message = "O telefone não pode ser vazio")
    String telefone,

    @NotBlank(message = "O CPF não pode ser vazio")
    @Pattern(regexp = "\\d{11}", message = "O CPF deve conter exatamente 11 dígitos")
    @CPF
    String cpf,

    String registroProfissional,

    @NotNull(message = "O cargo não pode ser nulo")
    CargoEnum cargoEnum

) {}