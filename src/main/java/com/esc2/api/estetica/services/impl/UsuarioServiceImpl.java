package com.esc2.api.estetica.services.impl;

import com.esc2.api.estetica.dtos.UsuarioRecordDto;
import com.esc2.api.estetica.exceptions.NotFoundException;
import com.esc2.api.estetica.models.UsuarioModel;
import com.esc2.api.estetica.repositories.UsuarioRepository;
import com.esc2.api.estetica.services.UsuarioServiceAPI;

import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UsuarioServiceImpl implements UsuarioServiceAPI {

    private final UsuarioRepository usuarioRepository;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UsuarioModel salvar(UsuarioRecordDto dto) {
        var model = new UsuarioModel();
        BeanUtils.copyProperties(dto, model);
        
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
    public UsuarioModel atualizar(UUID id, UsuarioRecordDto dto) {
    	
        UsuarioModel u = usuarioRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("Usuário com id: " + id + "não encontrado"));
        BeanUtils.copyProperties(dto, usuarioRepository);
        return usuarioRepository.save(u);
    }

    @Override
    public void deletar(UUID id) {
        UsuarioModel usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado."));
        usuarioRepository.delete(usuario);
    }

	@Override
	public UsuarioModel salvar(UsuarioModel usuarioModel) {
		// TODO Stub de método gerado automaticamente
		return null;
	}
}
