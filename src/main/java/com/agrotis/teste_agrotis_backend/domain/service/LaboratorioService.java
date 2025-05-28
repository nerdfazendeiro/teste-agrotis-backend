package com.agrotis.teste_agrotis_backend.domain.service;

import com.agrotis.teste_agrotis_backend.domain.laboratorio.Laboratorio;
import com.agrotis.teste_agrotis_backend.domain.laboratorio.LaboratorioDTO;
import com.agrotis.teste_agrotis_backend.domain.laboratorio.LaboratorioRelatorioDTO;
import com.agrotis.teste_agrotis_backend.domain.laboratorio.LaboratorioRepository;
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

    public List<LaboratorioDTO> listarTodos() {
        return laboratorioRepository.findAll()
                .stream()
                .map(this::toDTO)
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
            Long quantidadeMinima) {

        if (quantidadeMinima == null) {
            quantidadeMinima = 0L;
        }

        List<Object[]> resultados = laboratorioRepository.findLaboratoriosComFiltros(
                dataInicialInicio, dataInicialFim, dataFinalInicio, dataFinalFim, observacoes, quantidadeMinima);

        return resultados.stream()
                .map(resultado -> new LaboratorioRelatorioDTO(
                        (Long) resultado[0],      // id
                        (String) resultado[1],    // nome
                        (Long) resultado[2]       // quantidadePessoas
                        // resultado[3] é a MIN(dataInicial) que usamos apenas para ordenação
                ))
                .toList();
    }

    private LaboratorioDTO toDTO(Laboratorio laboratorio) {
        return new LaboratorioDTO(laboratorio.getId(), laboratorio.getNome());
    }
}