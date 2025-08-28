package com.esc2.api.estetica.controllers;

import com.esc2.api.estetica.dtos.ProfissionalRecordDto;
import com.esc2.api.estetica.dtos.ProfissionalUpdateDto;
import com.esc2.api.estetica.models.ProfissionalModel;
import com.esc2.api.estetica.services.ProfissionalServiceAPI;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/profissionais")
public class ProfissionalController {

    @Autowired
    private ProfissionalServiceAPI profissionalService;

    @PostMapping
    public ResponseEntity<ProfissionalModel> cadastrar(@RequestBody @Valid ProfissionalRecordDto dto) {
        ProfissionalModel profissionalModel = profissionalService.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(profissionalModel);
    }

    @GetMapping
    public ResponseEntity<List<ProfissionalModel>> listarTodos() {
        return ResponseEntity.ok(profissionalService.listarTodos());
    }

    @GetMapping(params = "nome")
    public ResponseEntity<List<ProfissionalModel>> buscarPorNome(@RequestParam String nome) {
        return ResponseEntity.ok(profissionalService.buscarPorNome(nome));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfissionalModel> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK)
        		.body(profissionalService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfissionalModel> atualizar(
    		@PathVariable UUID id,
    		@RequestBody @Valid ProfissionalUpdateDto dto) {
        return ResponseEntity.status(HttpStatus.OK).body(profissionalService.atualizar(id,dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        profissionalService.deletarPorId(id);
        return ResponseEntity.noContent().build();
    }
}
