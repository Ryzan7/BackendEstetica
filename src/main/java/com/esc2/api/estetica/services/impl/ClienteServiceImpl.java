package com.esc2.api.estetica.services.impl;

import com.esc2.api.estetica.dtos.ClienteRecordDto;
import com.esc2.api.estetica.dtos.ClienteUpdateDto;
import com.esc2.api.estetica.exceptions.NotFoundException;
import com.esc2.api.estetica.models.ClienteModel;
import com.esc2.api.estetica.repositories.ClienteRepository;
import com.esc2.api.estetica.services.ClienteServiceAPI;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ClienteServiceImpl implements ClienteServiceAPI {

    final ClienteRepository clienteRepository;

    public ClienteServiceImpl(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public ClienteModel save(ClienteRecordDto cliente) {
        if (clienteRepository.existsByCpf(cliente.cpf())) {
            throw new RuntimeException("CPF já cadastrado.");
        }
        ClienteModel clienteModel = new ClienteModel();
        BeanUtils.copyProperties(cliente, clienteModel);
        clienteModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        return clienteRepository.save(clienteModel);
    }

    @Transactional
    public ClienteModel update(UUID id, ClienteUpdateDto clienteDto) {
      ClienteModel clienteEncontrado = clienteRepository.findById(id).orElseThrow(() -> new NotFoundException("Cliente não encontrado"));

      if(clienteDto.nome() != null && !clienteDto.nome().trim().isEmpty()) {
          clienteEncontrado.setNome(clienteDto.nome());
      }
      if(clienteDto.telefone() != null && !clienteDto.telefone().trim().isEmpty()) {
          clienteEncontrado.setTelefone(clienteDto.telefone());
      }
      if(clienteDto.cpf() != null && !clienteDto.cpf().trim().isEmpty()) {
          clienteEncontrado.setCpf(clienteDto.cpf());
      }

      ClienteModel clienteAtualizado = clienteRepository.save(clienteEncontrado);

      return clienteAtualizado;

    }

    @Override
    public List<ClienteModel> findAll() {
        return clienteRepository.findAll();
    }

    @Override
    public Optional<ClienteModel> findById(UUID clienteId) {
        Optional<ClienteModel> clienteModel = clienteRepository.findById(clienteId);
        if (clienteModel.isEmpty()) {
            throw new NotFoundException("Cliente não encontrado.");
        }
        return clienteModel;
    }

    @Override
    public void deleteClienteById(UUID clienteId) {
        ClienteModel clienteModel = clienteRepository.findById(clienteId).orElseThrow(
                () -> new NotFoundException("Cliente não encontrado.")
        );
        clienteRepository.delete(clienteModel);
    }

    @Override
    public List<ClienteModel> findByNome(String nome) {
        List<ClienteModel> clienteModel = clienteRepository.findByNome(nome);
        return clienteModel;

    }

	@Override
	public ClienteModel update(ClienteRecordDto clienteRecordDto, ClienteModel clienteModel) {
		// TODO Stub de método gerado automaticamente
		return null;
	}
}
