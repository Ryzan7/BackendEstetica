package com.esc2.api.estetica.services;



import com.esc2.api.estetica.dtos.ServicosRecordDto;
import com.esc2.api.estetica.models.ServicoModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ServicoServiceAPI {
    ServicoModel save(ServicosRecordDto servicosRecordDto);
    ServicoModel findById(UUID id);
    List<ServicoModel> findAll();
    void deleteById(UUID id);

    ServicoModel update(UUID id, ServicosRecordDto servicosRecordDto);
    ServicoModel findByNome(String nome);

}
