package com.esc2.api.estetica.exceptions;

public class AgendamentoConcluidoException extends RuntimeException {
    public AgendamentoConcluidoException(String message) {
    }
    public AgendamentoConcluidoException() {super ("Agendamento com status CONCLUIDO não pode ser alterado");}
}


