package com.esc2.api.estetica.repositories;

import com.esc2.api.estetica.models.AgendamentoModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AgendamentoRepository extends JpaRepository<AgendamentoModel, UUID> {
}
