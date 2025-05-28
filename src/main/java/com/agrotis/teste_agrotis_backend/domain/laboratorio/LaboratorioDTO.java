package com.agrotis.teste_agrotis_backend.domain.laboratorio;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LaboratorioDTO(
        Long id,

        @NotBlank
        @Size(max = 255)
        String nome
) {}