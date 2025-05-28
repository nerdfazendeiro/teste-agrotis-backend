package com.agrotis.teste_agrotis_backend.domain.pessoa;

import com.agrotis.teste_agrotis_backend.domain.laboratorio.Laboratorio;
import com.agrotis.teste_agrotis_backend.domain.laboratorio.LaboratorioDTO;
import com.agrotis.teste_agrotis_backend.domain.laboratorio.LaboratorioRepository;
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
        Pessoa pessoa = toEntity(dto);
        Pessoa salva = pessoaRepository.save(pessoa);
        return toDTO(salva);
    }

    public Optional<PessoaDTO> atualizar(Long id, PessoaRequestDTO dto) {
        return pessoaRepository.findById(id)
                .map(pessoa -> {
                    atualizarPessoa(pessoa, dto);
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

    private Pessoa toEntity(PessoaRequestDTO dto) {
        Pessoa pessoa = new Pessoa();
        pessoa.setNome(dto.nome());
        pessoa.setDataInicial(dto.dataInicial());
        pessoa.setDataFinal(dto.dataFinal());
        pessoa.setObservacoes(dto.observacoes());

        if (dto.infosPropriedade() != null && dto.infosPropriedade().id() != null) {
            Propriedade propriedade = propriedadeRepository.findById(dto.infosPropriedade().id())
                    .orElseThrow(() -> new RuntimeException("Propriedade não encontrada"));
            pessoa.setPropriedade(propriedade);
        }

        if (dto.laboratorio() != null && dto.laboratorio().id() != null) {
            Laboratorio laboratorio = laboratorioRepository.findById(dto.laboratorio().id())
                    .orElseThrow(() -> new RuntimeException("Laboratório não encontrado"));
            pessoa.setLaboratorio(laboratorio);
        }

        return pessoa;
    }

    private void atualizarPessoa(Pessoa pessoa, PessoaRequestDTO dto) {
        pessoa.setNome(dto.nome());
        pessoa.setDataInicial(dto.dataInicial());
        pessoa.setDataFinal(dto.dataFinal());
        pessoa.setObservacoes(dto.observacoes());

        if (dto.infosPropriedade() != null && dto.infosPropriedade().id() != null) {
            Propriedade propriedade = propriedadeRepository.findById(dto.infosPropriedade().id())
                    .orElseThrow(() -> new RuntimeException("Propriedade não encontrada"));
            pessoa.setPropriedade(propriedade);
        } else {
            pessoa.setPropriedade(null);
        }

        if (dto.laboratorio() != null && dto.laboratorio().id() != null) {
            Laboratorio laboratorio = laboratorioRepository.findById(dto.laboratorio().id())
                    .orElseThrow(() -> new RuntimeException("Laboratório não encontrado"));
            pessoa.setLaboratorio(laboratorio);
        } else {
            pessoa.setLaboratorio(null);
        }
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
