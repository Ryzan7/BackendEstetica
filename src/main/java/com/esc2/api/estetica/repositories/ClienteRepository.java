package com.esc2.api.estetica.repositories;

import com.esc2.api.estetica.models.ClienteModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClienteRepository extends JpaRepository<ClienteModel, UUID> {

    boolean existsByCpf(String cpf);

    List<ClienteModel> findByNome(String nome);
}
