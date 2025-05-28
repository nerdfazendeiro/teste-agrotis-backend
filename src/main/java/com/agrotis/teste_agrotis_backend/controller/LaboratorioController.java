package com.agrotis.teste_agrotis_backend.controller;

import com.agrotis.teste_agrotis_backend.domain.laboratorio.LaboratorioDTO;
import com.agrotis.teste_agrotis_backend.domain.laboratorio.LaboratorioRelatorioDTO;
import com.agrotis.teste_agrotis_backend.domain.service.LaboratorioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/laboratorio")
@RequiredArgsConstructor
public class LaboratorioController {

    private final LaboratorioService laboratorioService;

    @PostMapping
    public ResponseEntity<LaboratorioDTO> criar(@Valid @RequestBody LaboratorioDTO dto) {
        LaboratorioDTO criado = laboratorioService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }

    @GetMapping
    public ResponseEntity<List<LaboratorioDTO>> listarTodos() {
        List<LaboratorioDTO> laboratorios = laboratorioService.listarTodos();
        return ResponseEntity.ok(laboratorios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LaboratorioDTO> buscarPorId(@PathVariable Long id) {
        return laboratorioService.buscarPorId(id)
                .map(laboratorio -> ResponseEntity.ok(laboratorio))
                .orElse(ResponseEntity.notFound().build());
    }


    @PutMapping("/{id}")
    public ResponseEntity<LaboratorioDTO> atualizar(@PathVariable Long id, @Valid @RequestBody LaboratorioDTO dto) {
        return laboratorioService.atualizar(id, dto)
                .map(laboratorio -> ResponseEntity.ok(laboratorio))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (laboratorioService.deletar(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/relatorio")
    public ResponseEntity<List<LaboratorioRelatorioDTO>> obterRelatorio(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicialInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicialFim,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFinalInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFinalFim,
            @RequestParam(required = false) String observacoes,
            @RequestParam(required = false) Long quantidadeMinima) {

        List<LaboratorioRelatorioDTO> relatorio = laboratorioService.obterRelatorioLaboratorios(
                dataInicialInicio, dataInicialFim, dataFinalInicio, dataFinalFim, observacoes, quantidadeMinima);

        return ResponseEntity.ok(relatorio);
    }
}
