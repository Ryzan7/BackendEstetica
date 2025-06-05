package com.esc2.api.estetica.services;

import com.esc2.api.estetica.dtos.ClienteRecordDto;
import com.esc2.api.estetica.models.ClienteModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClienteServiceAPI {
    ClienteModel save(ClienteRecordDto cliente);
    ClienteModel update(ClienteRecordDto clienteRecordDto, ClienteModel clienteModel);
    List<ClienteModel> findAll();
    Optional<ClienteModel> findById(UUID clienteId);
}
