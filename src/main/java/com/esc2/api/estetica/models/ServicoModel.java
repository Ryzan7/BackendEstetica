package com.esc2.api.estetica.models;

import com.esc2.api.estetica.enums.CargoEnum;
import com.esc2.api.estetica.enums.EspecialidadeEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "TB_SERVICOS")
public class ServicoModel implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID servicosId;

    @Column(nullable = false)
    private String nome;

    private String descricao;

    @Column(nullable = false)
    private BigDecimal valor;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EspecialidadeEnum especialidadeEnum;

    private Integer duracao;

    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @Column(nullable = false)
    private LocalDateTime creationDate;

    @OneToMany(
            mappedBy = "servico",
            fetch = FetchType.LAZY
    )
    @JsonManagedReference("servico-agendamentoservicos")
    private Set<AgendamentoServicos> agendamentos = new HashSet<>();

    @PrePersist
    public void prePersist() {
        if(creationDate == null) {
            creationDate = LocalDateTime.now();
        }
    }

    public UUID getServicosId() {
        return servicosId;
    }

    public void setServicosId(UUID servicosId) {
        this.servicosId = servicosId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public EspecialidadeEnum getEspecialidadeEnum() {
        return especialidadeEnum;
    }

    public void setEspecialidadeEnum(EspecialidadeEnum especialidadeEnum) {
        this.especialidadeEnum = especialidadeEnum;
    }

    public Integer getDuracao() {
        return duracao;
    }

    public void setDuracao(Integer duracao) {
        this.duracao = duracao;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Set<AgendamentoServicos> getAgendamentos() {
        return agendamentos;
    }

    public void setAgendamentos(Set<AgendamentoServicos> agendamentos) {
        this.agendamentos = agendamentos;
    }
}
