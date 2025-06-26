package com.esc2.api.estetica.services.impl;

import com.esc2.api.estetica.dtos.ServicosRecordDto;
import com.esc2.api.estetica.exceptions.NotFoundException;
import com.esc2.api.estetica.models.ServicoModel;
import com.esc2.api.estetica.repositories.ServicoRepository;
import com.esc2.api.estetica.services.ServicoServiceAPI;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ServicoServiceImpl implements ServicoServiceAPI {

    private final ServicoRepository servicoRepository;

    public ServicoServiceImpl(ServicoRepository servicoRepository) {
        this.servicoRepository = servicoRepository;
    }


    @Override
    public ServicoModel save(ServicosRecordDto servico) {
        ServicoModel servicoModel = new ServicoModel();
        BeanUtils.copyProperties(servico, servicoModel);

        servicoModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));

        servicoRepository.save(servicoModel);

        return servicoRepository.save(servicoModel);
    }

    @Override
    public ServicoModel findById(UUID id) {
        ServicoModel servico = servicoRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Serviço não econtrado")
        );

        return servico;
    }

    @Override
    public List<ServicoModel> findAll() {
        return servicoRepository.findAll();
    }

    @Override
    public void deleteById(UUID id) {
        servicoRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Serviço não encontrado")
        );

        servicoRepository.deleteById(id);
    }

    @Override
    @Transactional
    public ServicoModel update(UUID id, ServicosRecordDto servicosRecordDto) {
        ServicoModel servicoEncontrado = servicoRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Serviço não encontrado")
        );

        BeanUtils.copyProperties(servicosRecordDto, servicoEncontrado,"id");

        ServicoModel servicoAtualizado = servicoRepository.save(servicoEncontrado);


        return servicoAtualizado;
    }

    @Override
    public ServicoModel findByNome(String nome) {
        return servicoRepository.findByNome(nome);
    }
}
