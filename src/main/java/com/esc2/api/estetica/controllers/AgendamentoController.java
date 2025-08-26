package com.esc2.api.estetica.controllers;


import com.esc2.api.estetica.dtos.AgendamentoDto;
import com.esc2.api.estetica.dtos.response.AgendamentoResponseDto;
import com.esc2.api.estetica.models.AgendamentoModel;
import com.esc2.api.estetica.services.AgendamentoServiceAPI;
import com.esc2.api.estetica.services.impl.RelatorioService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {

    AgendamentoServiceAPI agendamentoService;
    RelatorioService relatorioService;

    public AgendamentoController(AgendamentoServiceAPI agendamentoService, RelatorioService relatorioService) {
        this.agendamentoService = agendamentoService;
        this.relatorioService = relatorioService;
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

    @PutMapping("/concluir/{agendamentoId}")
   public ResponseEntity<AgendamentoResponseDto> concluirAgendamento(@PathVariable UUID agendamentoId) {
        return ResponseEntity.status(HttpStatus.OK).body(agendamentoService.concluirAgendamento(agendamentoId));
   }

   @PutMapping("/cancelar/{agendamentoId}")
    public ResponseEntity<AgendamentoResponseDto> cancelarAgendamento(@PathVariable UUID agendamentoId) {
        return ResponseEntity.status(HttpStatus.OK).body(agendamentoService.cancelarAgendamento(agendamentoId));
    }


   @GetMapping("/relatorio/excel")
   public void exportarAgendamentosParaExcel(
           @RequestParam("dataInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Instant dataInicio,
           @RequestParam("dataFim") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Instant dataFim,
           HttpServletResponse response
   ) throws IOException {

       response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
       String headerValue = "attachment; filename=relatorio_agendamentos_" + dataInicio + "_a_" + dataFim + ".xlsx";
       response.setHeader("Content-Disposition", headerValue);


       List<AgendamentoModel> agendamentos = agendamentoService.buscarAgendamentoPorPeriodo(dataInicio, dataFim);
       relatorioService.gerarRelatorioAgendamentos(agendamentos, response);
   }


}
