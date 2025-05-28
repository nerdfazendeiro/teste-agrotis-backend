package com.agrotis.teste_agrotis_backend.domain.pessoa;

import com.agrotis.teste_agrotis_backend.domain.laboratorio.LaboratorioDTO;
import com.agrotis.teste_agrotis_backend.domain.propriedade.PropriedadeDTO;

import java.time.LocalDateTime;

public record PessoaDTO(
        Long id,
        String nome,
        LocalDateTime dataInicial,
        LocalDateTime dataFinal,
        PropriedadeDTO infosPropriedade,
        LaboratorioDTO laboratorio,
        String observacoes
) {}