package com.agrotis.teste_agrotis_backend.domain.laboratorio;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "laboratorio")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Laboratorio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 255)
    @Column(name = "nome", nullable = false)
    private String nome;
}
