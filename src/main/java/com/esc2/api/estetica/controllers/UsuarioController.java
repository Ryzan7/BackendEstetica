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
        UsuarioModel criado = usuarioService.salvar(usuarioDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }

    @GetMapping
    public ResponseEntity<List<UsuarioModel>> listarTodos() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> buscarPorId(@PathVariable UUID id) {
        return usuarioService.buscarPorId(id)
                .<ResponseEntity<Object>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado."));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> atualizar(@PathVariable UUID id, @RequestBody @Valid UsuarioRecordDto usuarioDto) {
        try {
            UsuarioModel atualizado = usuarioService.atualizar(id, usuarioDto);
            return ResponseEntity.ok(atualizado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletar(@PathVariable UUID id) {
        try {
            usuarioService.deletar(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");
        }
    }
}