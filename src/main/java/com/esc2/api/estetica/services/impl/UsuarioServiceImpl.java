package com.esc2.api.estetica.services.impl;

import com.esc2.api.estetica.dtos.UsuarioRecordDto;
import com.esc2.api.estetica.models.UsuarioModel;
import com.esc2.api.estetica.repositories.UsuarioRepository;
import com.esc2.api.estetica.services.UsuarioServiceAPI;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UsuarioServiceImpl implements UsuarioServiceAPI {

    private final UsuarioRepository repo;
    private final PasswordEncoder encoder;

    public UsuarioServiceImpl(UsuarioRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    @Override
    public UsuarioModel salvar(UsuarioRecordDto dto) {
        UsuarioModel u = new UsuarioModel();
        u.setNome(dto.nome());
        u.setEmail(dto.email());
        u.setUsername(dto.username());
        u.setCargoEnum(dto.cargoEnum());
        u.setCreationDate(LocalDateTime.now());

        // Insere a criptografia:
        u.setPassword(encoder.encode(dto.password()));

        return repo.save(u);
    }

    @Override
    public List<UsuarioModel> listarTodos() {
        return repo.findAll();
    }

    @Override
    public Optional<UsuarioModel> buscarPorId(UUID id) {
        return repo.findById(id);
    }

    @Override
    public UsuarioModel atualizar(UUID id, UsuarioRecordDto dto) {
        UsuarioModel u = repo.findById(id).orElseThrow();

        u.setNome(dto.nome());
        u.setEmail(dto.email());
        u.setUsername(dto.username());
        u.setCargoEnum(dto.cargoEnum());

        // Atualiza a senha só se vier no DTO e sempre codifica
        if (dto.password() != null && !dto.password().isBlank()) {
            u.setPassword(encoder.encode(dto.password()));
        }

        return repo.save(u);
    }

    @Override
    public void deletar(UUID id) {
        repo.deleteById(id);
    }

	@Override
	public UsuarioModel salvar(UsuarioModel usuarioModel) {
		// TODO Stub de método gerado automaticamente
		return null;
	}
}
