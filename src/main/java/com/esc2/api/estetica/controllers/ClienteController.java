package com.esc2.api.estetica.controllers;

import com.esc2.api.estetica.dtos.ClienteRecordDto;
import com.esc2.api.estetica.models.ClienteModel;
import com.esc2.api.estetica.services.ClienteServiceAPI;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    final ClienteServiceAPI clienteService;

    public ClienteController(ClienteServiceAPI clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping
    public ResponseEntity<ClienteModel> save(
            @RequestBody @Valid ClienteRecordDto cliente) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteService.save(cliente));
    }

    @GetMapping
    public ResponseEntity<List<ClienteModel>> getAllClientes() {
        return ResponseEntity.ok(clienteService.findAll());
    }
    @GetMapping("/{clienteId}")
    public ResponseEntity<ClienteModel> getClienteById(@PathVariable UUID clienteId) {
        return clienteService.findById(clienteId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{clienteId}")
    public ResponseEntity<Object> updateCliente(
            @PathVariable(value = "clienteId") UUID clienteId,
            @RequestBody @Valid ClienteRecordDto clienteRecordDto
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(clienteService.update(clienteRecordDto,clienteService.findById(clienteId).get()));
    }
}
