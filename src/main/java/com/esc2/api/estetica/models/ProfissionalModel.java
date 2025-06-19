package com.esc2.api.estetica.models;

import com.esc2.api.estetica.enums.CargoEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "TB_PROFISSIONAIS")
public class ProfissionalModel implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID profissionalId;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String telefone;

    //TODO: Implementar validação de cpf
    @Column(nullable = false, unique = true,length = 11)
    private String cpf;

    private String registroProfissional;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CargoEnum cargoEnum;

    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @Column(nullable = false)
    private LocalDateTime creationDate;

}
