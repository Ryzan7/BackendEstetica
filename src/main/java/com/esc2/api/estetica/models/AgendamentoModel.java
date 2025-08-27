package com.esc2.api.estetica.models;

import com.esc2.api.estetica.enums.CargoEnum;
import com.esc2.api.estetica.enums.StatusAgendamentoEnum;
import com.esc2.api.estetica.enums.TipoDesconto;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "TB_AGENDAMENTOS")
public class AgendamentoModel implements Serializable {

    //TESTE


    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID agendamentoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    @JsonBackReference("cliente-agendamentos")
    private ClienteModel cliente;

    @OneToMany(
            mappedBy = "agendamento",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonManagedReference("agendamento-servicos")
    private Set<AgendamentoServicos> servicosAgendados = new HashSet<>();

    private Instant dataHora;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusAgendamentoEnum status;

    @Column(name = "valor-total")
    private BigDecimal valorTotal;

    @Column(name = "duracao-total")
    private Integer duracaoTotal;

    @Column(name = "observacoes")
    private String observacoes;

    @Column(nullable = true)
    private boolean desconto;

    @Column(name = "tipo_desconto_aplicado")
    @Enumerated(EnumType.STRING)
    private TipoDesconto tipoDescontoAplicado;

    @Column(name = "valor_desconto_aplicado")
    private BigDecimal valorDescontoAplicado;
    //**
    public void adicionarServico(ServicoModel servico, BigDecimal valorCobrado, Integer duracao){
        AgendamentoServicos servicoAdicionado = new AgendamentoServicos();
        servicoAdicionado.setServico(servico);
        servicoAdicionado.setAgendamento(this);
        servicoAdicionado.setValorTotal(valorCobrado);
        servicoAdicionado.setDuracaoTotal(duracao);
        this.servicosAgendados.add(servicoAdicionado);
    }

    public BigDecimal calculaValorTotal(){
        if(this.servicosAgendados.isEmpty()){
            return BigDecimal.ZERO;
        }
        return servicosAgendados.stream()
                .map(AgendamentoServicos::getValorTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Integer calculaDuracaoTotal(){
        if(this.servicosAgendados.isEmpty()){
            return 0;
        }
        return servicosAgendados.stream()
                .map(item -> item.getServico().getDuracao())
                .reduce(0, Integer::sum);
    }

    //TODO Aplicar desconto (R$ ou %)

    public BigDecimal getValorDoDesconto(BigDecimal valor, TipoDesconto tipo) {
        BigDecimal valorTotal = calculaValorTotal();

        if (valorTotal.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        if (valor == null || tipo == null) {
            return BigDecimal.ZERO;
        }

        switch (tipo) {
            case PORCENTAGEM:
                // Validação: Desconto percentual não deve ser negativo ou maior que 100
                if (valor.compareTo(BigDecimal.ZERO) < 0 || valor.compareTo(new BigDecimal("100")) > 0) {
                    throw new IllegalArgumentException("Desconto percentual deve estar entre 0 e 100.");
                }
                BigDecimal fator = valor.divide(new BigDecimal("100"));
                BigDecimal descontoCalculado = valorTotal.multiply(fator);
                return descontoCalculado.setScale(2, RoundingMode.HALF_UP);

            case FIXO:
                // Validação: Desconto fixo não deve ser negativo
                if (valor.compareTo(BigDecimal.ZERO) < 0) {
                    throw new IllegalArgumentException("Desconto fixo não pode ser negativo.");
                }
                // O desconto não pode ser maior que o valor total, então retornamos o valor total
                return valor;

            default:
                // Lançar exceção se um tipo desconhecido for passado
                throw new IllegalStateException("Tipo de desconto desconhecido: " + tipo);
        }
    }

    public BigDecimal getValorFinalComDesconto(BigDecimal valor, TipoDesconto tipo) {
        BigDecimal valorTotal = this.calculaValorTotal();
        BigDecimal valorDoDesconto = this.getValorDoDesconto(valor, tipo);

        return valorTotal.subtract(valorDoDesconto);
    }


    //TODO: Verificar a possibilidade de adicionar um profissional ao agendamento


    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @Column(nullable = false)
    private LocalDateTime creationDate;

    @PrePersist
    public void prePersist(){
        creationDate = LocalDateTime.now();
    }


    public UUID getAgendamentoId() {
        return agendamentoId;
    }

    public void setAgendamentoId(UUID agendamentoId) {
        this.agendamentoId = agendamentoId;
    }

    public ClienteModel getCliente() {
        return cliente;
    }

    public void setCliente(ClienteModel cliente) {
        this.cliente = cliente;
    }

    public Set<AgendamentoServicos> getServicosAgendados() {
        return servicosAgendados;
    }

    public void setServicosAgendados(Set<AgendamentoServicos> servicosAgendados) {
        this.servicosAgendados = servicosAgendados;
    }

    public Instant getDataHora() {
        return dataHora;
    }

    public void setDataHora(Instant dataHora) {
        this.dataHora = dataHora;
    }

    public StatusAgendamentoEnum getStatus() {
        return status;
    }

    public void setStatus(StatusAgendamentoEnum status) {
        this.status = status;
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

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = LocalDateTime.now();
    }

    public TipoDesconto getTipoDescontoAplicado() {
        return tipoDescontoAplicado;
    }

    public void setTipoDescontoAplicado(TipoDesconto tipoDescontoAplicado) {
        this.tipoDescontoAplicado = tipoDescontoAplicado;
    }

    public BigDecimal getValorDescontoAplicado() {
        return valorDescontoAplicado;
    }

    public void setValorDescontoAplicado(BigDecimal valorDescontoAplicado) {
        this.valorDescontoAplicado = valorDescontoAplicado;
    }

    public boolean isDesconto() {
        return desconto;
    }

    public void setDesconto(boolean desconto) {
        this.desconto = desconto;
    }
}
