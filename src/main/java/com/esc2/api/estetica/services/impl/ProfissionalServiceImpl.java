package com.esc2.api.estetica.services.impl;

import com.esc2.api.estetica.dtos.ProfissionalRecordDto;
import com.esc2.api.estetica.dtos.ProfissionalUpdateDto;
import com.esc2.api.estetica.enums.CargoEnum;
import com.esc2.api.estetica.exceptions.NotFoundException;
import com.esc2.api.estetica.models.ProfissionalModel;
import com.esc2.api.estetica.repositories.ProfissionalRepository;
import com.esc2.api.estetica.services.ProfissionalServiceAPI;
import com.esc2.api.estetica.mappers.ProfissionalMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

@Service
public class ProfissionalServiceImpl implements ProfissionalServiceAPI {

    private final ProfissionalRepository profissionalRepository;
    private final ProfissionalMapper profissionalMapper;

    public ProfissionalServiceImpl(ProfissionalRepository profissionalRepository, ProfissionalMapper profissionalMapper) {
        this.profissionalRepository = profissionalRepository;
        this.profissionalMapper = profissionalMapper;
    }

    @Override
    public ProfissionalModel cadastrar(ProfissionalRecordDto dto) {
        if (profissionalRepository.existsByCpf(dto.cpf())) {
            throw new RuntimeException("CPF já cadastrado");
        }

        if ((dto.cargoEnum() == CargoEnum.PROFESSOR || dto.cargoEnum() == CargoEnum.COORDENADOR)
                && (dto.registroProfissional() == null || dto.registroProfissional().isBlank())) {
            throw new RuntimeException("Este cargo exige registro profissional.");
        }

        ProfissionalModel profissionalModel = profissionalMapper.toModel(dto);
        profissionalModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        return profissionalRepository.save(profissionalModel);
    }

    @Override
    @Transactional
    public ProfissionalModel atualizar(UUID id, ProfissionalUpdateDto profissionalDto) {
        ProfissionalModel profissionalEncontrado = profissionalRepository.findById(id).orElseThrow(() -> new NotFoundException("Profissional não encontrado"));

        if(profissionalDto.cpf() != null){
            profissionalEncontrado.setCpf(profissionalDto.cpf());
        }

        if(profissionalDto.nome() != null){
            profissionalEncontrado.setNome(profissionalDto.nome());
        }

        if(profissionalDto.cargoEnum() != null){
            profissionalEncontrado.setCargoEnum(profissionalDto.cargoEnum());
        }

        if(profissionalDto.registroProfissional() != null){
            profissionalEncontrado.setRegistroProfissional(profissionalDto.registroProfissional());
        }

        if(profissionalDto.telefone() != null){
            profissionalEncontrado.setTelefone(profissionalDto.telefone());
        }


        ProfissionalModel profissionalAtualizado = profissionalRepository.save(profissionalEncontrado);

        return profissionalAtualizado;
    }

    @Override
    public List<ProfissionalModel> listarTodos() {
        return profissionalRepository.findAll();
    }

    @Override
    public ProfissionalModel buscarPorId(UUID id) {
    	ProfissionalModel profissional = profissionalRepository.findById(id).orElseThrow(() -> new NotFoundException("Profissional não encontrado!"));

        return profissional;
    }

    @Override
    public void deletarPorId(UUID id) {
        ProfissionalModel profissionalModel = profissionalRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Profissional não encontrado."));
        profissionalRepository.delete(profissionalModel);
    }

    @Override
    public List<ProfissionalModel> buscarPorNome(String nome) {
        return profissionalRepository.findByNome(nome);
    }
}