package com.agrotis.teste_agrotis_backend.domain.pessoa;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
public record PessoaRequestDTO(
        @NotBlank
        @Size(max = 255)
        String nome,

        @NotNull
        LocalDateTime dataInicial,

        @NotNull
        LocalDateTime dataFinal,

        @NotNull(message = "ID da propriedade é obrigatório")
        Long propriedadeId,

        @NotNull(message = "ID do laboratório é obrigatório")
        Long laboratorioId,

        @Size(max = 1000)
        String observacoes
) {}
