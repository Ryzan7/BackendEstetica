package com.esc2.api.estetica.controllers;

import com.esc2.api.estetica.dtos.UsuarioRecordDto;
import com.esc2.api.estetica.models.UsuarioModel;
import com.esc2.api.estetica.services.UsuarioServiceAPI;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioServiceAPI usuarioService;

    @PostMapping
    public ResponseEntity<UsuarioModel> criarUsuario(@RequestBody @Valid UsuarioRecordDto usuarioDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.salvar(usuarioDto));
    }

    @GetMapping
    public ResponseEntity<List<UsuarioModel>> listarTodos() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioModel> buscarPorId(@PathVariable UUID id) {
        UsuarioModel usuario = usuarioService.buscarPorId(id).get(); // já lança erro se não achar
        return ResponseEntity.ok(usuario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioModel> atualizar(@PathVariable UUID id, @RequestBody @Valid UsuarioRecordDto usuarioDto) {
        return ResponseEntity.ok(usuarioService.atualizar(id, usuarioDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}