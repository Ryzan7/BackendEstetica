package com.esc2.api.estetica.repositories;

import com.esc2.api.estetica.models.ProfissionalModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProfissionalRepository extends JpaRepository<ProfissionalModel, UUID> {
    
    boolean existsByCpf(String cpf);
    List<ProfissionalModel> findByNome(String nome);
}