package com.agrotis.teste_agrotis_backend.domain.service;

import com.agrotis.teste_agrotis_backend.domain.laboratorio.Laboratorio;
import com.agrotis.teste_agrotis_backend.domain.laboratorio.LaboratorioDTO;
import com.agrotis.teste_agrotis_backend.domain.laboratorio.LaboratorioRepository;
import com.agrotis.teste_agrotis_backend.domain.pessoa.Pessoa;
import com.agrotis.teste_agrotis_backend.domain.pessoa.PessoaDTO;
import com.agrotis.teste_agrotis_backend.domain.pessoa.PessoaRepository;
import com.agrotis.teste_agrotis_backend.domain.pessoa.PessoaRequestDTO;
import com.agrotis.teste_agrotis_backend.domain.propriedade.Propriedade;
import com.agrotis.teste_agrotis_backend.domain.propriedade.PropriedadeDTO;
import com.agrotis.teste_agrotis_backend.domain.propriedade.PropriedadeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PessoaService {

    private final PessoaRepository pessoaRepository;
    private final PropriedadeRepository propriedadeRepository;
    private final LaboratorioRepository laboratorioRepository;

    public List<PessoaDTO> listarTodas() {
        return pessoaRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public Optional<PessoaDTO> buscarPorId(Long id) {
        return pessoaRepository.findById(id)
                .map(this::toDTO);
    }

    public List<PessoaDTO> buscarPorNome(String nome) {
        return pessoaRepository.findByNomeContainingIgnoreCase(nome)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public PessoaDTO criar(PessoaRequestDTO dto) {
        Propriedade propriedade = propriedadeRepository.findById(dto.propriedadeId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Propriedade não encontrada com ID: " + dto.propriedadeId()));

        Laboratorio laboratorio = laboratorioRepository.findById(dto.laboratorioId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Laboratório não encontrado com ID: " + dto.laboratorioId()));

        Pessoa pessoa = Pessoa.builder()
                .nome(dto.nome())
                .dataInicial(dto.dataInicial())
                .dataFinal(dto.dataFinal())
                .observacoes(dto.observacoes())
                .propriedade(propriedade)
                .laboratorio(laboratorio)
                .build();

        Pessoa salva = pessoaRepository.save(pessoa);
        return toDTO(salva);
    }

    public Optional<PessoaDTO> atualizar(Long id, PessoaRequestDTO dto) {
        return pessoaRepository.findById(id)
                .map(pessoa -> {
                    Propriedade propriedade = propriedadeRepository.findById(dto.propriedadeId())
                            .orElseThrow(() -> new IllegalArgumentException(
                                    "Propriedade não encontrada com ID: " + dto.propriedadeId()));

                    Laboratorio laboratorio = laboratorioRepository.findById(dto.laboratorioId())
                            .orElseThrow(() -> new IllegalArgumentException(
                                    "Laboratório não encontrado com ID: " + dto.laboratorioId()));

                    pessoa.setNome(dto.nome());
                    pessoa.setDataInicial(dto.dataInicial());
                    pessoa.setDataFinal(dto.dataFinal());
                    pessoa.setObservacoes(dto.observacoes());
                    pessoa.setPropriedade(propriedade);
                    pessoa.setLaboratorio(laboratorio);

                    return toDTO(pessoaRepository.save(pessoa));
                });
    }


    public boolean deletar(Long id) {
        if (pessoaRepository.existsById(id)) {
            pessoaRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private PessoaDTO toDTO(Pessoa pessoa) {
        PropriedadeDTO propriedadeDTO = null;
        if (pessoa.getPropriedade() != null) {
            propriedadeDTO = new PropriedadeDTO(
                    pessoa.getPropriedade().getId(),
                    pessoa.getPropriedade().getNome()
            );
        }

        LaboratorioDTO laboratorioDTO = null;
        if (pessoa.getLaboratorio() != null) {
            laboratorioDTO = new LaboratorioDTO(
                    pessoa.getLaboratorio().getId(),
                    pessoa.getLaboratorio().getNome()
            );
        }

        return new PessoaDTO(
                pessoa.getId(),
                pessoa.getNome(),
                pessoa.getDataInicial(),
                pessoa.getDataFinal(),
                propriedadeDTO,
                laboratorioDTO,
                pessoa.getObservacoes()
        );
    }
}
