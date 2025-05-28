package com.agrotis.teste_agrotis_backend.controller;

import com.agrotis.teste_agrotis_backend.domain.propriedade.PropriedadeDTO;
import com.agrotis.teste_agrotis_backend.domain.service.PropriedadeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/propriedade")
@RequiredArgsConstructor
public class PropriedadeController {

    private final PropriedadeService propriedadeService;

    @PostMapping
    public ResponseEntity<PropriedadeDTO> criar(@Valid @RequestBody PropriedadeDTO dto) {
        PropriedadeDTO criada = propriedadeService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(criada);
    }
    @GetMapping
    public ResponseEntity<List<PropriedadeDTO>> listarTodas() {
        List<PropriedadeDTO> propriedades = propriedadeService.listarTodas();
        return ResponseEntity.ok(propriedades);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PropriedadeDTO> buscarPorId(@PathVariable Long id) {
        return propriedadeService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
