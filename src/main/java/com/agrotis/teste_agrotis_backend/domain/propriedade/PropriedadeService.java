package com.agrotis.teste_agrotis_backend.domain.propriedade;

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
        Propriedade propriedade = new Propriedade(dto.nome());
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
