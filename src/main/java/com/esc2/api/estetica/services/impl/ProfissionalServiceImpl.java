package com.esc2.api.estetica.services.impl;

import com.esc2.api.estetica.dtos.ProfissionalRecordDto;
import com.esc2.api.estetica.enums.CargoEnum;
import com.esc2.api.estetica.exceptions.NotFoundException;
import com.esc2.api.estetica.models.ProfissionalModel;
import com.esc2.api.estetica.repositories.ProfissionalRepository;
import com.esc2.api.estetica.services.ProfissionalServiceAPI;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProfissionalServiceImpl implements ProfissionalServiceAPI {

    @Autowired
    private ProfissionalRepository profissionalRepository;

    @Override
    public ProfissionalModel cadastrar(ProfissionalRecordDto dto) {
        if (profissionalRepository.existsByCpf(dto.cpf())) {
            throw new RuntimeException("CPF já cadastrado");
        }

        if ((dto.cargoEnum() == CargoEnum.PROFESSOR || dto.cargoEnum() == CargoEnum.COORDENADOR)
                && (dto.registroProfissional() == null || dto.registroProfissional().isBlank())) {
            throw new RuntimeException("Este cargo exige registro profissional.");
        }

        ProfissionalModel model = new ProfissionalModel();
        BeanUtils.copyProperties(dto, model);
        model.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));

        return profissionalRepository.save(model);
    }

    @Override
    public ProfissionalModel atualizar(ProfissionalRecordDto dto, ProfissionalModel model) {
        BeanUtils.copyProperties(dto, model);
        return profissionalRepository.save(model);
    }

    @Override
    public List<ProfissionalModel> listarTodos() {
        return profissionalRepository.findAll();
    }

    @Override
    public Optional<ProfissionalModel> buscarPorId(UUID id) {
        Optional<ProfissionalModel> model = profissionalRepository.findById(id);
        if (model.isEmpty()) {
            throw new NotFoundException("Profissional não encontrado.");
        }
        return model;
    }

    @Override
    public void deletarPorId(UUID id) {
        ProfissionalModel model = profissionalRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Profissional não encontrado."));
        profissionalRepository.delete(model);
    }

    @Override
    public List<ProfissionalModel> buscarPorNome(String nome) {
        return profissionalRepository.findByNome(nome);
    }
}