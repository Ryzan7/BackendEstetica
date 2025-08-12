package com.esc2.api.estetica.models;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Objects;


@Entity
@Table(name = "TB_AGENDAMENTO_SERVICOS")
public class AgendamentoServicos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "servico_id", nullable = false)
    @JsonBackReference("servico-agendamentoservicos")
    private ServicoModel servico;

    //TODO Adicionar relacionamento com Agendamento aqui
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agendamento_id", nullable = false)
    @JsonBackReference("agendamento-servicos")
    private AgendamentoModel agendamento;

    @Column(name = "valor_total")
    private BigDecimal valorTotal;

    @Column(name = "duracao_total")
    private Integer duracaoTotal;

    //Pensar em mais campos para serem adicionados


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ServicoModel getServico() {
        return servico;
    }

    public void setServico(ServicoModel servico) {
        this.servico = servico;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Integer getDuracaoTotal() {
        return duracaoTotal;
    }

    public void setDuracaoTotal(Integer duracaoTotal) {
        this.duracaoTotal = duracaoTotal;
    }

    public AgendamentoModel getAgendamento() {
        return agendamento;
    }

    public void setAgendamento(AgendamentoModel agendamento) {
        this.agendamento = agendamento;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AgendamentoServicos that = (AgendamentoServicos) o;
        // Dois itens são "iguais" se pertencerem ao mesmo agendamento e ao mesmo serviço
        return Objects.equals(agendamento, that.agendamento) &&
                Objects.equals(servico, that.servico);
    }

    @Override
    public int hashCode() {
        // O hashCode deve ser baseado nos mesmos campos do equals
        return Objects.hash(agendamento, servico);
    }


}
