package com.esc2.api.estetica.repositories;

import com.esc2.api.estetica.models.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<UsuarioModel, UUID> {

    Optional<UsuarioModel> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}