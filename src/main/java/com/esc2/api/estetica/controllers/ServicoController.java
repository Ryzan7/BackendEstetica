package com.esc2.api.estetica.controllers;

import com.esc2.api.estetica.dtos.ServicosRecordDto;
import com.esc2.api.estetica.models.ServicoModel;
import com.esc2.api.estetica.services.ServicoServiceAPI;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/servicos")
public class ServicoController {

    private final ServicoServiceAPI servicoService;

    public ServicoController(ServicoServiceAPI servicoService) {
        this.servicoService = servicoService;
    }

    @PostMapping
    public ResponseEntity<ServicoModel> createServico(@RequestBody @Valid ServicosRecordDto servicosRecordDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(servicoService.save(servicosRecordDto));
    }

    @GetMapping
    public ResponseEntity<List<ServicoModel>> findAllServicos(){
        return ResponseEntity.status(HttpStatus.OK).body(servicoService.findAll());
    }

    @GetMapping("/{servicoId}")
    public ResponseEntity<ServicoModel> findServicoById(@PathVariable UUID servicoId){
        return ResponseEntity.status(HttpStatus.OK).body(servicoService.findById(servicoId));
    }

    @PutMapping("/{servicoId}")
    public ResponseEntity<ServicoModel> updateServico(@PathVariable UUID servicoId, @RequestBody @Valid ServicosRecordDto servicosRecordDto){
        return ResponseEntity.status(HttpStatus.OK).body(servicoService.update(servicoId, servicosRecordDto));
    }

    @DeleteMapping("/{servicoId}")
    public ResponseEntity<Void> deleteServico(@PathVariable UUID servicoId ){
        servicoService.deleteById(servicoId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping(params = "nomeServico")
    public ResponseEntity<ServicoModel> findServicoByNome(@RequestParam String nomeServico){
        return ResponseEntity.status(HttpStatus.OK).body(servicoService.findByNome(nomeServico));
    }
}
