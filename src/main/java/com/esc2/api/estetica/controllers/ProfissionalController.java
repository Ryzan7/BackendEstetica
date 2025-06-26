package com.esc2.api.estetica.controllers;

import com.esc2.api.estetica.dtos.ProfissionalRecordDto;
import com.esc2.api.estetica.models.ProfissionalModel;
import com.esc2.api.estetica.services.ProfissionalServiceAPI;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
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
        return profissionalService.buscarPorId(id)
        		.map(ResponseEntity::ok)
        		.orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfissionalModel> atualizar(
    		@PathVariable UUID id,
    		@RequestBody @Valid ProfissionalRecordDto dto) {
        ProfissionalModel profissionalModel = profissionalService.buscarPorId(id)
        		.orElseThrow(() -> new RuntimeException("Profissional não encontrado."));
        ProfissionalModel atualizado = profissionalService.atualizar(dto, profissionalModel);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        profissionalService.deletarPorId(id);
        return ResponseEntity.noContent().build();
    }
}
