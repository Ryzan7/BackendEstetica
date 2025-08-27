package com.esc2.api.estetica.services;

import com.esc2.api.estetica.dtos.ProfissionalRecordDto;
import com.esc2.api.estetica.dtos.ProfissionalUpdateDto;
import com.esc2.api.estetica.models.ProfissionalModel;

import java.util.List;
import java.util.UUID;

public interface ProfissionalServiceAPI {
    ProfissionalModel cadastrar(ProfissionalRecordDto dto);
    ProfissionalModel atualizar(UUID id, ProfissionalUpdateDto dto);
    List<ProfissionalModel> listarTodos();
    ProfissionalModel buscarPorId(UUID id);
    void deletarPorId(UUID id);
    List<ProfissionalModel> buscarPorNome(String nome);
}