package com.esc2.api.estetica.controllers;


import com.esc2.api.estetica.dtos.AgendamentoDto;
import com.esc2.api.estetica.dtos.response.AgendamentoResponseDto;
import com.esc2.api.estetica.models.AgendamentoModel;
import com.esc2.api.estetica.services.AgendamentoServiceAPI;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {

    AgendamentoServiceAPI agendamentoService;

    public AgendamentoController(AgendamentoServiceAPI agendamentoService) {
        this.agendamentoService = agendamentoService;
    }

    @PostMapping
    public ResponseEntity<AgendamentoResponseDto> save(@RequestBody @Valid AgendamentoDto agendamentoDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(agendamentoService.create(agendamentoDto));
    }

    @GetMapping("/{agendamentoId}")
    public ResponseEntity<AgendamentoResponseDto> findById(@PathVariable UUID agendamentoId) {
        return ResponseEntity.status(HttpStatus.OK).body(agendamentoService.findById(agendamentoId));
    }

    @GetMapping
    public ResponseEntity<List<AgendamentoResponseDto>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(agendamentoService.findAll());
    }

    @DeleteMapping("/{agendamentoId}")
    public ResponseEntity<AgendamentoResponseDto> delete(@PathVariable UUID agendamentoId) {
        agendamentoService.delete(agendamentoId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @PutMapping("/{agendamentoId}")
    public ResponseEntity<AgendamentoResponseDto> update(@PathVariable UUID agendamentoId, @RequestBody @Valid AgendamentoDto agendamentoDto) {
        return ResponseEntity.status(HttpStatus.OK).body(agendamentoService.update(agendamentoId, agendamentoDto));
    }






}
