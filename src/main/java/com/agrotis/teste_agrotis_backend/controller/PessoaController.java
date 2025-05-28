package com.agrotis.teste_agrotis_backend.controller;

import com.agrotis.teste_agrotis_backend.domain.pessoa.PessoaDTO;
import com.agrotis.teste_agrotis_backend.domain.pessoa.PessoaRequestDTO;
import com.agrotis.teste_agrotis_backend.domain.service.PessoaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/pessoa")
@RequiredArgsConstructor
public class PessoaController {

    private final PessoaService pessoaService;

    @PostMapping
    public ResponseEntity<PessoaDTO> criar(@Valid @RequestBody PessoaRequestDTO dto) {
        PessoaDTO criada = pessoaService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(criada);
    }

    @GetMapping
    public ResponseEntity<List<PessoaDTO>> listarTodas(@RequestParam(required = false) String nome) {
        List<PessoaDTO> pessoas;

        if (nome != null && !nome.trim().isEmpty()) {
            pessoas = pessoaService.buscarPorNome(nome.trim());
        } else {
            pessoas = pessoaService.listarTodas();
        }

        return ResponseEntity.ok(pessoas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PessoaDTO> buscarPorId(@PathVariable Long id) {
        return pessoaService.buscarPorId(id)
                .map(pessoa -> ResponseEntity.ok(pessoa))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PessoaDTO> atualizar(@PathVariable Long id, @Valid @RequestBody PessoaRequestDTO dto) {
        return pessoaService.atualizar(id, dto)
                .map(pessoa -> ResponseEntity.ok(pessoa))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (pessoaService.deletar(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}