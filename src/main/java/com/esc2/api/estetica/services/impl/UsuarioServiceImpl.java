package com.esc2.api.estetica.services.impl;

import com.esc2.api.estetica.dtos.UsuarioRecordDto;
import com.esc2.api.estetica.exceptions.NotFoundException;
import com.esc2.api.estetica.models.UsuarioModel;
import com.esc2.api.estetica.repositories.UsuarioRepository;
import com.esc2.api.estetica.services.UsuarioServiceAPI;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UsuarioServiceImpl implements UsuarioServiceAPI {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UsuarioModel salvar(UsuarioRecordDto dto) {
        var model = new UsuarioModel();
        BeanUtils.copyProperties(dto, model);
        model.setCreationDate(LocalDateTime.now());
        return usuarioRepository.save(model);
    }

    @Override
    public List<UsuarioModel> listarTodos() {
        return usuarioRepository.findAll();
    }

    @Override
    public Optional<UsuarioModel> buscarPorId(UUID id) {
        return Optional.of(usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado.")));
    }

    @Override
    public UsuarioModel atualizar(UUID id, UsuarioRecordDto dto) {
        UsuarioModel usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado."));

        BeanUtils.copyProperties(dto, usuario);
        return usuarioRepository.save(usuario);
    }

    @Override
    public void deletar(UUID id) {
        UsuarioModel usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado."));
        usuarioRepository.delete(usuario);
    }
}