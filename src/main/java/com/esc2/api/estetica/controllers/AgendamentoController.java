package com.esc2.api.estetica.controllers;


import com.esc2.api.estetica.dtos.AgendamentoDto;
import com.esc2.api.estetica.dtos.response.AgendamentoResponseDto;
import com.esc2.api.estetica.models.AgendamentoModel;
import com.esc2.api.estetica.services.AgendamentoServiceAPI;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}
