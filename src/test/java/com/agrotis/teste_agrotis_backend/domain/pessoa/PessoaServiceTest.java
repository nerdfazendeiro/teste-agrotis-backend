package com.agrotis.teste_agrotis_backend.domain.pessoa;

import com.agrotis.teste_agrotis_backend.domain.laboratorio.Laboratorio;
import com.agrotis.teste_agrotis_backend.domain.laboratorio.LaboratorioDTO;
import com.agrotis.teste_agrotis_backend.domain.laboratorio.LaboratorioRepository;
import com.agrotis.teste_agrotis_backend.domain.propriedade.Propriedade;
import com.agrotis.teste_agrotis_backend.domain.propriedade.PropriedadeDTO;
import com.agrotis.teste_agrotis_backend.domain.propriedade.PropriedadeRepository;
import com.agrotis.teste_agrotis_backend.domain.service.PessoaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PessoaServiceTest {

    @Mock
    private PessoaRepository pessoaRepository;

    @Mock
    private PropriedadeRepository propriedadeRepository;

    @Mock
    private LaboratorioRepository laboratorioRepository;

    @InjectMocks
    private PessoaService pessoaService;

    private Pessoa pessoa;
    private PessoaDTO pessoaDTO;
    private PessoaRequestDTO pessoaRequestDTO;
    private Propriedade propriedade;
    private PropriedadeDTO propriedadeDTO;
    private Laboratorio laboratorio;
    private LaboratorioDTO laboratorioDTO;
    private LocalDateTime dataInicial;
    private LocalDateTime dataFinal;

    @BeforeEach
    void setUp() {
        dataInicial = LocalDateTime.now().minusDays(5);
        dataFinal = LocalDateTime.now();
        
        propriedade = new Propriedade(1L, "Propriedade Teste");
        propriedadeDTO = new PropriedadeDTO(1L, "Propriedade Teste");
        
        laboratorio = new Laboratorio(1L, "Laboratório Teste");
        laboratorioDTO = new LaboratorioDTO(1L, "Laboratório Teste");
        
        pessoa = new Pessoa(1L, "Pessoa Teste", dataInicial, dataFinal, propriedade, laboratorio, "Observações teste");
        
        pessoaDTO = new PessoaDTO(
            1L, 
            "Pessoa Teste", 
            dataInicial, 
            dataFinal, 
            propriedadeDTO, 
            laboratorioDTO, 
            "Observações teste"
        );

        pessoaRequestDTO = PessoaRequestDTO.builder()
                .nome("Pessoa Teste")
                .dataInicial(dataInicial)
                .dataFinal(dataFinal)
                .propriedadeId(propriedadeDTO.id())
                .laboratorioId(laboratorioDTO.id())
                .observacoes("Observações teste")
                .build();
    }

    @Nested
    @DisplayName("Testes para o método listarTodas")
    class ListarTodasTests {
        @Test
        @DisplayName("Deve retornar lista vazia quando não há pessoas")
        void deveRetornarListaVaziaQuandoNaoHaPessoas() {
            when(pessoaRepository.findAll()).thenReturn(Collections.emptyList());
            
            List<PessoaDTO> resultado = pessoaService.listarTodas();
            
            assertTrue(resultado.isEmpty());
            verify(pessoaRepository).findAll();
        }

        @Test
        @DisplayName("Deve retornar lista com pessoas quando existem registros")
        void deveRetornarListaComPessoasQuandoExistemRegistros() {
            Pessoa pessoa2 = new Pessoa(2L, "Pessoa 2", dataInicial, dataFinal, propriedade, laboratorio, "Obs 2");
            
            when(pessoaRepository.findAll()).thenReturn(Arrays.asList(pessoa, pessoa2));
            
            List<PessoaDTO> resultado = pessoaService.listarTodas();
            
            assertEquals(2, resultado.size());
            assertEquals("Pessoa Teste", resultado.get(0).nome());
            assertEquals("Pessoa 2", resultado.get(1).nome());
            verify(pessoaRepository).findAll();
        }
    }

    @Nested
    @DisplayName("Testes para o método buscarPorId")
    class BuscarPorIdTests {
        @Test
        @DisplayName("Deve retornar pessoa quando ID existe")
        void deveRetornarPessoaQuandoIdExiste() {
            when(pessoaRepository.findById(1L)).thenReturn(Optional.of(pessoa));
            
            Optional<PessoaDTO> resultado = pessoaService.buscarPorId(1L);
            
            assertTrue(resultado.isPresent());
            assertEquals("Pessoa Teste", resultado.get().nome());
            verify(pessoaRepository).findById(1L);
        }

        @Test
        @DisplayName("Deve retornar Optional vazio quando ID não existe")
        void deveRetornarOptionalVazioQuandoIdNaoExiste() {
            when(pessoaRepository.findById(99L)).thenReturn(Optional.empty());
            
            Optional<PessoaDTO> resultado = pessoaService.buscarPorId(99L);
            
            assertFalse(resultado.isPresent());
            verify(pessoaRepository).findById(99L);
        }
    }

    @Nested
    @DisplayName("Testes para o método buscarPorNome")
    class BuscarPorNomeTests {
        @Test
        @DisplayName("Deve retornar lista vazia quando não há pessoas com o nome")
        void deveRetornarListaVaziaQuandoNaoHaPessoasComONome() {
            when(pessoaRepository.findByNomeContainingIgnoreCase("Inexistente")).thenReturn(Collections.emptyList());
            
            List<PessoaDTO> resultado = pessoaService.buscarPorNome("Inexistente");
            
            assertTrue(resultado.isEmpty());
            verify(pessoaRepository).findByNomeContainingIgnoreCase("Inexistente");
        }

        @Test
        @DisplayName("Deve retornar lista com pessoas quando existem registros com o nome")
        void deveRetornarListaComPessoasQuandoExistemRegistrosComONome() {
            Pessoa pessoa2 = new Pessoa(2L, "Pessoa Teste 2", dataInicial, dataFinal, propriedade, laboratorio, "Obs 2");
            
            when(pessoaRepository.findByNomeContainingIgnoreCase("Teste")).thenReturn(Arrays.asList(pessoa, pessoa2));
            
            List<PessoaDTO> resultado = pessoaService.buscarPorNome("Teste");
            
            assertEquals(2, resultado.size());
            assertEquals("Pessoa Teste", resultado.get(0).nome());
            assertEquals("Pessoa Teste 2", resultado.get(1).nome());
            verify(pessoaRepository).findByNomeContainingIgnoreCase("Teste");
        }
    }

    @Nested
    @DisplayName("Testes para o método criar")
    class CriarTests {
        @Test
        @DisplayName("Deve criar pessoa com sucesso")
        void deveCriarPessoaComSucesso() {
            when(propriedadeRepository.findById(1L)).thenReturn(Optional.of(propriedade));
            when(laboratorioRepository.findById(1L)).thenReturn(Optional.of(laboratorio));
            when(pessoaRepository.save(any(Pessoa.class))).thenReturn(pessoa);
            
            PessoaDTO resultado = pessoaService.criar(pessoaRequestDTO);
            
            assertNotNull(resultado);
            assertEquals(1L, resultado.id());
            assertEquals("Pessoa Teste", resultado.nome());
            assertEquals(dataInicial, resultado.dataInicial());
            assertEquals(dataFinal, resultado.dataFinal());
            assertEquals("Propriedade Teste", resultado.infosPropriedade().nome());
            assertEquals("Laboratório Teste", resultado.laboratorio().nome());
            assertEquals("Observações teste", resultado.observacoes());
            
            verify(propriedadeRepository).findById(1L);
            verify(laboratorioRepository).findById(1L);
            verify(pessoaRepository).save(any(Pessoa.class));
        }

        @Test
        @DisplayName("Deve lançar exceção quando propriedade não existe")
        void deveLancarExcecaoQuandoPropriedadeNaoExiste() {
            when(propriedadeRepository.findById(1L)).thenReturn(Optional.empty());
            
            assertThrows(RuntimeException.class, () -> {
                pessoaService.criar(pessoaRequestDTO);
            });
            
            verify(propriedadeRepository).findById(1L);
            verify(laboratorioRepository, never()).findById(anyLong());
            verify(pessoaRepository, never()).save(any(Pessoa.class));
        }

        @Test
        @DisplayName("Deve lançar exceção quando laboratório não existe")
        void deveLancarExcecaoQuandoLaboratorioNaoExiste() {
            when(propriedadeRepository.findById(1L)).thenReturn(Optional.of(propriedade));
            when(laboratorioRepository.findById(1L)).thenReturn(Optional.empty());
            
            assertThrows(RuntimeException.class, () -> {
                pessoaService.criar(pessoaRequestDTO);
            });
            
            verify(propriedadeRepository).findById(1L);
            verify(laboratorioRepository).findById(1L);
            verify(pessoaRepository, never()).save(any(Pessoa.class));
        }
    }

    @Nested
    @DisplayName("Testes para o método atualizar")
    class AtualizarTests {
        @Test
        @DisplayName("Deve atualizar pessoa quando ID existe")
        void deveAtualizarPessoaQuandoIdExiste() {
            when(pessoaRepository.findById(1L)).thenReturn(Optional.of(pessoa));
            when(propriedadeRepository.findById(1L)).thenReturn(Optional.of(propriedade));
            when(laboratorioRepository.findById(1L)).thenReturn(Optional.of(laboratorio));
            when(pessoaRepository.save(any(Pessoa.class))).thenReturn(pessoa);
            
            Optional<PessoaDTO> resultado = pessoaService.atualizar(1L, pessoaRequestDTO);
            
            assertTrue(resultado.isPresent());
            assertEquals("Pessoa Teste", resultado.get().nome());
            verify(pessoaRepository).findById(1L);
            verify(propriedadeRepository).findById(1L);
            verify(laboratorioRepository).findById(1L);
            verify(pessoaRepository).save(any(Pessoa.class));
        }

        @Test
        @DisplayName("Deve retornar Optional vazio quando tenta atualizar ID inexistente")
        void deveRetornarOptionalVazioQuandoTentaAtualizarIdInexistente() {
            when(pessoaRepository.findById(99L)).thenReturn(Optional.empty());
            
            Optional<PessoaDTO> resultado = pessoaService.atualizar(99L, pessoaRequestDTO);
            
            assertFalse(resultado.isPresent());
            verify(pessoaRepository).findById(99L);
            verify(propriedadeRepository, never()).findById(anyLong());
            verify(laboratorioRepository, never()).findById(anyLong());
            verify(pessoaRepository, never()).save(any(Pessoa.class));
        }

        @Test
        @DisplayName("Deve lançar exceção quando propriedade não existe na atualização")
        void deveLancarExcecaoQuandoPropriedadeNaoExisteNaAtualizacao() {
            when(pessoaRepository.findById(1L)).thenReturn(Optional.of(pessoa));
            when(propriedadeRepository.findById(1L)).thenReturn(Optional.empty());
            
            assertThrows(RuntimeException.class, () -> {
                pessoaService.atualizar(1L, pessoaRequestDTO);
            });
            
            verify(pessoaRepository).findById(1L);
            verify(propriedadeRepository).findById(1L);
            verify(laboratorioRepository, never()).findById(anyLong());
            verify(pessoaRepository, never()).save(any(Pessoa.class));
        }
    }

    @Nested
    @DisplayName("Testes para o método deletar")
    class DeletarTests {
        @Test
        @DisplayName("Deve retornar true quando deleta pessoa existente")
        void deveRetornarTrueQuandoDeletaPessoaExistente() {
            when(pessoaRepository.existsById(1L)).thenReturn(true);
            doNothing().when(pessoaRepository).deleteById(1L);
            
            boolean resultado = pessoaService.deletar(1L);
            
            assertTrue(resultado);
            verify(pessoaRepository).existsById(1L);
            verify(pessoaRepository).deleteById(1L);
        }

        @Test
        @DisplayName("Deve retornar false quando tenta deletar pessoa inexistente")
        void deveRetornarFalseQuandoTentaDeletarPessoaInexistente() {
            when(pessoaRepository.existsById(99L)).thenReturn(false);
            
            boolean resultado = pessoaService.deletar(99L);
            
            assertFalse(resultado);
            verify(pessoaRepository).existsById(99L);
            verify(pessoaRepository, never()).deleteById(anyLong());
        }
    }
}