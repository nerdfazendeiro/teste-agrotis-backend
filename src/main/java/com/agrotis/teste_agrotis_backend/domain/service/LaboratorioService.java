package com.agrotis.teste_agrotis_backend.domain.service;

import com.agrotis.teste_agrotis_backend.domain.laboratorio.*;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class LaboratorioService {

    private final LaboratorioRepository laboratorioRepository;

    public List<LaboratorioListDTO> listarTodos() {
        List<Object[]> resultados = laboratorioRepository.findAllWithPessoaCount();

        return resultados.stream()
                .map(resultado -> new LaboratorioListDTO(
                        (Long) resultado[0],
                        (String) resultado[1],
                        (Long) resultado[2]
                ))
                .toList();
    }

    public Optional<LaboratorioDTO> buscarPorId(Long id) {
        return laboratorioRepository.findById(id)
                .map(this::toDTO);
    }

    public LaboratorioDTO criar(LaboratorioDTO dto) {
        Laboratorio laboratorio = Laboratorio.builder()
                .nome(dto.nome())
                .build();

        Laboratorio salvo = laboratorioRepository.save(laboratorio);
        return toDTO(salvo);
    }

    public Optional<LaboratorioDTO> atualizar(Long id, LaboratorioDTO dto) {
        return laboratorioRepository.findById(id)
                .map(laboratorio -> {
                    laboratorio.setNome(dto.nome());
                    return toDTO(laboratorioRepository.save(laboratorio));
                });
    }

    public boolean deletar(Long id) {
        if (laboratorioRepository.existsById(id)) {
            laboratorioRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<LaboratorioRelatorioDTO> obterRelatorioLaboratorios(
            LocalDateTime dataInicialInicio,
            LocalDateTime dataInicialFim,
            LocalDateTime dataFinalInicio,
            LocalDateTime dataFinalFim,
            String observacoes,
            @NotNull Long quantidadeMinima) {

        if (quantidadeMinima < 0) {
            throw new IllegalArgumentException("Quantidade mínima não pode ser negativa");
        }

        List<Object[]> resultados = laboratorioRepository.findLaboratoriosComFiltros(
                dataInicialInicio, dataInicialFim,
                dataFinalInicio, dataFinalFim,
                observacoes, quantidadeMinima);

        return resultados.stream()
                .map(resultado -> new LaboratorioRelatorioDTO(
                        (Long) resultado[0],
                        (String) resultado[1],
                        ((Number) resultado[2]).longValue()
                ))
                .toList();
    }

    private LaboratorioDTO toDTO(Laboratorio laboratorio) {
        return new LaboratorioDTO(laboratorio.getId(), laboratorio.getNome());
    }
}