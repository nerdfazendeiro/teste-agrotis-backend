package com.agrotis.teste_agrotis_backend.domain.pessoa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Long> {

    @Query("SELECT p FROM Pessoa p WHERE LOWER(p.nome) LIKE LOWER(CONCAT('%', :nome, '%'))")
    List<Pessoa> findByNomeContainingIgnoreCase(@Param("nome") String nome);

    List<Pessoa> findByLaboratorioId(Long laboratorioId);

    List<Pessoa> findByPropriedadeId(Long propriedadeId);
}