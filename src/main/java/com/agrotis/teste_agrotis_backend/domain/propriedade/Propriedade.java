package com.agrotis.teste_agrotis_backend.domain.propriedade;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "propriedade")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Propriedade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "nome", nullable = false)
    private String nome;

}
