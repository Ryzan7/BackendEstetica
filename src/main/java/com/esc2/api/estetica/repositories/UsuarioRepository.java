package com.esc2.api.estetica.repositories;

import com.esc2.api.estetica.models.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<UsuarioModel, UUID> {
    // Aqui você pode adicionar consultas personalizadas no futuro, como:
    // Optional<UsuarioModel> findByUsername(String username);
}