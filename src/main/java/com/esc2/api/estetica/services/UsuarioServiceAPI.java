package com.esc2.api.estetica.services;

import com.esc2.api.estetica.dtos.UsuarioRecordDto;
import com.esc2.api.estetica.models.UsuarioModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsuarioServiceAPI {
    UsuarioModel salvar(UsuarioRecordDto dto);
    List<UsuarioModel> listarTodos();
    Optional<UsuarioModel> buscarPorId(UUID id);
    UsuarioModel atualizar(UUID id, UsuarioRecordDto dto);
    void deletar(UUID id);
}