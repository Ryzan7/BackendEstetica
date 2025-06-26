package com.esc2.api.estetica.mappers;

import com.esc2.api.estetica.dtos.ProfissionalRecordDto;
import com.esc2.api.estetica.models.ProfissionalModel;
import org.springframework.stereotype.Component;

@Component
public class ProfissionalMapper {
	
	public ProfissionalModel toModel(ProfissionalRecordDto dto) {
		ProfissionalModel profissionalModel = new ProfissionalModel();
		updateFromDto(dto, profissionalModel);
		return profissionalModel;
	}

    public void updateFromDto(ProfissionalRecordDto dto, ProfissionalModel profissionalModel){ 
        
        profissionalModel.setNome(dto.nome());
        profissionalModel.setCpf(dto.cpf());
        profissionalModel.setTelefone(dto.telefone());
        profissionalModel.setCargoEnum(dto.cargoEnum());
        profissionalModel.setRegistroProfissional(dto.registroProfissional());
      
    }
}