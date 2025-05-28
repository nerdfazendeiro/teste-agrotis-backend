package com.agrotis.teste_agrotis_backend.domain.service;

import com.agrotis.teste_agrotis_backend.domain.propriedade.Propriedade;
import com.agrotis.teste_agrotis_backend.domain.propriedade.PropriedadeDTO;
import com.agrotis.teste_agrotis_backend.domain.propriedade.PropriedadeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PropriedadeService {

    private final PropriedadeRepository propriedadeRepository;

    public PropriedadeDTO criar(PropriedadeDTO dto) {
        Propriedade propriedade = Propriedade.builder()
                .nome(dto.nome())
                .build();

        Propriedade salva = propriedadeRepository.save(propriedade);
        return toDTO(salva);
    }

    public List<PropriedadeDTO> listarTodas() {
        return propriedadeRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public Optional<PropriedadeDTO> buscarPorId(Long id) {
        return propriedadeRepository.findById(id)
                .map(this::toDTO);
    }

    private PropriedadeDTO toDTO(Propriedade propriedade) {
        return new PropriedadeDTO(propriedade.getId(), propriedade.getNome());
    }
}
