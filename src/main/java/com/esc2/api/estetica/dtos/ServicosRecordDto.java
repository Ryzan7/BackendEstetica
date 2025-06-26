package com.esc2.api.estetica.dtos;

import com.esc2.api.estetica.enums.EspecialidadeEnum;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ServicosRecordDto(
        @NotBlank(message = "O nome não pode ser vazio")
        String nome,

        @NotBlank(message = "A descrição não pode ser vazia")
        String descricao,

        @NotNull(message = "O valor do serviço não pode ser nulo.")
        @Positive(message = "O valor do serviço deve ser maior que zero.")
        BigDecimal valor,

        @NotNull(message = "O serviço deve possuir uma duração")
        @Positive(message = "A duração do serviço deve ser maior que zero.")
        Integer duracao,

        @NotNull(message = "O serviço deve possuir uma especialidade")
        EspecialidadeEnum especialidadeEnum

) {
}
