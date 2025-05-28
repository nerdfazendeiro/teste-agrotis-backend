package com.agrotis.teste_agrotis_backend.domain.laboratorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LaboratorioRepository extends JpaRepository<Laboratorio, Long> {

    @Query("""
        SELECT l.id as id,
               UPPER(l.nome) as nome,
               COUNT(p.id) as quantidadePessoas
        FROM Laboratorio l
        LEFT JOIN Pessoa p ON p.laboratorio.id = l.id
        GROUP BY l.id, l.nome
        ORDER BY l.nome
        """)
    List<Object[]> findAllWithPessoaCount();

    @Query("""
        SELECT l.id as id,
               UPPER(l.nome) as nome,
               COUNT(p.id) as quantidadePessoas
        FROM Laboratorio l
        LEFT JOIN Pessoa p ON p.laboratorio.id = l.id
        WHERE (:dataInicialInicio IS NULL OR p.dataInicial >= :dataInicialInicio)
          AND (:dataInicialFim IS NULL OR p.dataInicial <= :dataInicialFim)
          AND (:dataFinalInicio IS NULL OR p.dataFinal >= :dataFinalInicio)
          AND (:dataFinalFim IS NULL OR p.dataFinal <= :dataFinalFim)
          AND (:observacoes IS NULL OR LOWER(p.observacoes) LIKE LOWER(CONCAT('%', :observacoes, '%')))
        GROUP BY l.id, l.nome
        HAVING COUNT(p.id) >= :quantidadeMinima
        ORDER BY COUNT(p.id) DESC, UPPER(l.nome) ASC
        """)
    List<Object[]> findLaboratoriosComFiltros(
            @Param("dataInicialInicio") LocalDateTime dataInicialInicio,
            @Param("dataInicialFim") LocalDateTime dataInicialFim,
            @Param("dataFinalInicio") LocalDateTime dataFinalInicio,
            @Param("dataFinalFim") LocalDateTime dataFinalFim,
            @Param("observacoes") String observacoes,
            @Param("quantidadeMinima") Long quantidadeMinima
    );
}
