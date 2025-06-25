package com.esc2.api.estetica.controllers;

import com.esc2.api.estetica.dtos.ProfissionalRecordDto;
import com.esc2.api.estetica.models.ProfissionalModel;
import com.esc2.api.estetica.services.ProfissionalServiceAPI;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
        var profissional = profissionalService.cadastrar(dto);
        return ResponseEntity.ok(profissional);
    }

    @GetMapping
    public ResponseEntity<List<ProfissionalModel>> listarTodos() {
        return ResponseEntity.ok(profissionalService.listarTodos());
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<ProfissionalModel>> buscarPorNome(@RequestParam String nome) {
        return ResponseEntity.ok(profissionalService.buscarPorNome(nome));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfissionalModel> buscarPorId(@PathVariable UUID id) {
        Optional<ProfissionalModel> profissional = profissionalService.buscarPorId(id);
        return profissional.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfissionalModel> atualizar(@PathVariable UUID id, @RequestBody @Valid ProfissionalRecordDto dto) {
        var model = profissionalService.buscarPorId(id).orElseThrow(() -> new RuntimeException("Profissional não encontrado"));
        var atualizado = profissionalService.atualizar(dto, model);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        profissionalService.deletarPorId(id);
        return ResponseEntity.noContent().build();
    }
}
