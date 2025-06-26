package com.esc2.api.estetica.repositories;


import com.esc2.api.estetica.models.ServicoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ServicoRepository extends JpaRepository<ServicoModel, UUID> {

    ServicoModel findByNome(String nome);
}
