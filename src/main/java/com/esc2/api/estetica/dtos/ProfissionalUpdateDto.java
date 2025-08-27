package com.esc2.api.estetica.dtos;

import com.esc2.api.estetica.enums.CargoEnum;
import com.esc2.api.estetica.enums.EspecialidadeEnum;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;

public record ProfissionalUpdateDto(
        // Se o nome for enviado, ele não pode ser vazio. Mas se for nulo (não enviado), a validação passa.
        @Size(min = 2, message = "O nome deve ter no mínimo 2 caracteres.")
        String nome,

        @CPF
        String cpf,

        EspecialidadeEnum especialidadeEnum,

        String registroProfissional,

        CargoEnum cargoEnum,

        String telefone
) {
}
