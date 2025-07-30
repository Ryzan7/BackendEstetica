package com.esc2.api.estetica.repositories;

import com.esc2.api.estetica.models.AgendamentoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AgendamentoRepository extends JpaRepository<AgendamentoModel, UUID> {
}
