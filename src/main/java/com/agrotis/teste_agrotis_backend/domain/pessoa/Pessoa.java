package com.agrotis.teste_agrotis_backend.domain.pessoa;

import com.agrotis.teste_agrotis_backend.domain.laboratorio.Laboratorio;
import com.agrotis.teste_agrotis_backend.domain.propriedade.Propriedade;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "pessoas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 255, message = "Nome deve ter no máximo 255 caracteres")
    @Column(name = "nome",nullable = false)
    private String nome;

    @NotNull(message = "Data inicial é obrigatória")
    @Column(name = "data_inicial", nullable = false)
    private LocalDateTime dataInicial;

    @NotNull(message = "Data final é obrigatória")
    @Column(name = "data_final", nullable = false)
    private LocalDateTime dataFinal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "propriedade_id")
    private Propriedade propriedade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "laboratorio_id")
    private Laboratorio laboratorio;

    @Size(max = 1000, message = "Observações deve ter no máximo 1000 caracteres")
    @Column(length = 1000)
    private String observacoes;
}
