package com.esc2.api.estetica.services.impl;

import com.esc2.api.estetica.dtos.UsuarioRecordDto;
import com.esc2.api.estetica.exceptions.NotFoundException;
import com.esc2.api.estetica.models.UsuarioModel;
import com.esc2.api.estetica.repositories.UsuarioRepository;
import com.esc2.api.estetica.services.UsuarioServiceAPI;

import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UsuarioServiceImpl implements UsuarioServiceAPI {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository,
                              PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public UsuarioModel salvar(UsuarioRecordDto dto) {
        var model = new UsuarioModel();
        BeanUtils.copyProperties(dto, model);

        // Valida duplicidades
        if (usuarioRepository.existsByEmail(model.getEmail())) {
            throw new IllegalArgumentException("E-mail já cadastrado.");
        }
        if (usuarioRepository.existsByUsername(model.getUsername())) {
            throw new IllegalArgumentException("Username já cadastrado.");
        }

        model.setPassword(passwordEncoder.encode(dto.password()));
        //TODO Usar o @PrePersist no Model
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
    @Transactional
    public UsuarioModel atualizar(UUID id, UsuarioRecordDto dto) {
    	
        UsuarioModel u = usuarioRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("Usuário com id: " + id + "não encontrado"));

        BeanUtils.copyProperties(dto, u, "usuarioId", "creationDate", "password");

        // Se veio senha nova no DTO, encode (evita double-encode)
        if (dto.password() != null && !dto.password().isBlank()) {
            u.setPassword(passwordEncoder.encode(dto.password()));
        }
        return usuarioRepository.save(u);
    }

    @Override
    @Transactional
    public void deletar(UUID id) {
        UsuarioModel usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado."));
        usuarioRepository.delete(usuario);
    }

}
