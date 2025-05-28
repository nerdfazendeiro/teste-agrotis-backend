package com.agrotis.teste_agrotis_backend.domain.laboratorio;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LaboratorioDTO(
        Long id,

        @NotBlank(message = "Nome do laboratório é obrigatório")
        @Size(max = 255, message = "Nome do laboratório deve ter no máximo 255 caracteres")
        String nome
) {}