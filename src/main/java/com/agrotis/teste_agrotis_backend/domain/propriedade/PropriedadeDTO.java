package com.agrotis.teste_agrotis_backend.domain.propriedade;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PropriedadeDTO (
        Long id,
        @NotBlank
        @Size(min = 3, max = 255)
        String nome
) {}