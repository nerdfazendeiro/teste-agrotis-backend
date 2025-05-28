package com.agrotis.teste_agrotis_backend.domain.laboratorio;

import com.agrotis.teste_agrotis_backend.domain.service.LaboratorioService;
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
public class LaboratorioServiceTest {

    @Mock
    private LaboratorioRepository laboratorioRepository;

    @InjectMocks
    private LaboratorioService laboratorioService;

    private Laboratorio laboratorio;
    private LaboratorioDTO laboratorioDTO;

    @BeforeEach
    void setUp() {
        laboratorio = new Laboratorio(1L, "Laboratório Teste");
        laboratorioDTO = new LaboratorioDTO(1L, "Laboratório Teste");
    }

    @Nested
    @DisplayName("Testes para o método listarTodos")
    class ListarTodosTests {
        @Test
        @DisplayName("Deve retornar lista vazia quando não há laboratórios")
        void deveRetornarListaVaziaQuandoNaoHaLaboratorios() {
            when(laboratorioRepository.findAllWithPessoaCount()).thenReturn(Collections.emptyList());

            List<LaboratorioListDTO> resultado = laboratorioService.listarTodos();

            assertTrue(resultado.isEmpty());
            verify(laboratorioRepository).findAllWithPessoaCount();
        }

        @Test
        @DisplayName("Deve retornar lista com laboratórios quando existem registros")
        void deveRetornarListaComLaboratoriosQuandoExistemRegistros() {
            Object[] lab1 = new Object[]{1L, "LAB 1", 5L};
            Object[] lab2 = new Object[]{2L, "LAB 2", 3L};

            when(laboratorioRepository.findAllWithPessoaCount()).thenReturn(Arrays.asList(lab1, lab2));

            List<LaboratorioListDTO> resultado = laboratorioService.listarTodos();

            assertEquals(2, resultado.size());
            assertEquals("LAB 1", resultado.get(0).nome());
            assertEquals("LAB 2", resultado.get(1).nome());
            verify(laboratorioRepository).findAllWithPessoaCount();
        }
    }

    @Nested
    @DisplayName("Testes para o método buscarPorId")
    class BuscarPorIdTests {
        @Test
        @DisplayName("Deve retornar laboratório quando ID existe")
        void deveRetornarLaboratorioQuandoIdExiste() {
            when(laboratorioRepository.findById(1L)).thenReturn(Optional.of(laboratorio));

            Optional<LaboratorioDTO> resultado = laboratorioService.buscarPorId(1L);

            assertTrue(resultado.isPresent());
            assertEquals("Laboratório Teste", resultado.get().nome());
            verify(laboratorioRepository).findById(1L);
        }

        @Test
        @DisplayName("Deve retornar Optional vazio quando ID não existe")
        void deveRetornarOptionalVazioQuandoIdNaoExiste() {
            when(laboratorioRepository.findById(99L)).thenReturn(Optional.empty());

            Optional<LaboratorioDTO> resultado = laboratorioService.buscarPorId(99L);

            assertFalse(resultado.isPresent());
            verify(laboratorioRepository).findById(99L);
        }
    }

    @Nested
    @DisplayName("Testes para o método criar")
    class CriarTests {
        @Test
        @DisplayName("Deve criar laboratório com sucesso")
        void deveCriarLaboratorioComSucesso() {
            LaboratorioDTO novoLabDTO = new LaboratorioDTO(null, "Novo Laboratório");
            Laboratorio novoLab = Laboratorio.builder().nome("Novo Laboratório").build();
            Laboratorio labSalvo = new Laboratorio(1L, "Novo Laboratório");

            when(laboratorioRepository.save(any(Laboratorio.class))).thenReturn(labSalvo);

            LaboratorioDTO resultado = laboratorioService.criar(novoLabDTO);

            assertNotNull(resultado);
            assertEquals(1L, resultado.id());
            assertEquals("Novo Laboratório", resultado.nome());
            verify(laboratorioRepository).save(any(Laboratorio.class));
        }
    }

    @Nested
    @DisplayName("Testes para o método atualizar")
    class AtualizarTests {
        @Test
        @DisplayName("Deve atualizar laboratório quando ID existe")
        void deveAtualizarLaboratorioQuandoIdExiste() {
            LaboratorioDTO atualizacaoDTO = new LaboratorioDTO(1L, "Laboratório Atualizado");
            Laboratorio labExistente = new Laboratorio(1L, "Laboratório Teste");
            Laboratorio labAtualizado = new Laboratorio(1L, "Laboratório Atualizado");

            when(laboratorioRepository.findById(1L)).thenReturn(Optional.of(labExistente));
            when(laboratorioRepository.save(any(Laboratorio.class))).thenReturn(labAtualizado);

            Optional<LaboratorioDTO> resultado = laboratorioService.atualizar(1L, atualizacaoDTO);

            assertTrue(resultado.isPresent());
            assertEquals("Laboratório Atualizado", resultado.get().nome());
            verify(laboratorioRepository).findById(1L);
            verify(laboratorioRepository).save(any(Laboratorio.class));
        }

        @Test
        @DisplayName("Deve retornar Optional vazio quando tenta atualizar ID inexistente")
        void deveRetornarOptionalVazioQuandoTentaAtualizarIdInexistente() {
            LaboratorioDTO atualizacaoDTO = new LaboratorioDTO(99L, "Laboratório Atualizado");

            when(laboratorioRepository.findById(99L)).thenReturn(Optional.empty());

            Optional<LaboratorioDTO> resultado = laboratorioService.atualizar(99L, atualizacaoDTO);

            assertFalse(resultado.isPresent());
            verify(laboratorioRepository).findById(99L);
            verify(laboratorioRepository, never()).save(any(Laboratorio.class));
        }
    }

    @Nested
    @DisplayName("Testes para o método deletar")
    class DeletarTests {
        @Test
        @DisplayName("Deve retornar true quando deleta laboratório existente")
        void deveRetornarTrueQuandoDeletaLaboratorioExistente() {
            when(laboratorioRepository.existsById(1L)).thenReturn(true);
            doNothing().when(laboratorioRepository).deleteById(1L);

            boolean resultado = laboratorioService.deletar(1L);

            assertTrue(resultado);
            verify(laboratorioRepository).existsById(1L);
            verify(laboratorioRepository).deleteById(1L);
        }

        @Test
        @DisplayName("Deve retornar false quando tenta deletar laboratório inexistente")
        void deveRetornarFalseQuandoTentaDeletarLaboratorioInexistente() {
            when(laboratorioRepository.existsById(99L)).thenReturn(false);

            boolean resultado = laboratorioService.deletar(99L);

            assertFalse(resultado);
            verify(laboratorioRepository).existsById(99L);
            verify(laboratorioRepository, never()).deleteById(anyLong());
        }
    }

    @Nested
    @DisplayName("Testes para o método obterRelatorioLaboratorios")
    class ObterRelatorioLaboratoriosTests {
        @Test
        @DisplayName("Deve retornar relatório vazio quando não há resultados")
        void deveRetornarRelatorioVazioQuandoNaoHaResultados() {
            when(laboratorioRepository.findLaboratoriosComFiltros(
                    any(), any(), any(), any(), any(), anyLong()
            )).thenReturn(Collections.emptyList());

            List<LaboratorioRelatorioDTO> resultado = laboratorioService.obterRelatorioLaboratorios(
                    null, null, null, null, null, 0L
            );

            assertTrue(resultado.isEmpty());
            verify(laboratorioRepository).findLaboratoriosComFiltros(
                    null, null, null, null, null, 0L
            );
        }

        @Test
        @DisplayName("Deve retornar relatório com dados quando há resultados")
        void deveRetornarRelatorioComDadosQuandoHaResultados() {
            Object[] resultado1 = new Object[]{1L, "LAB 1", 5L};
            Object[] resultado2 = new Object[]{2L, "LAB 2", 3L};

            when(laboratorioRepository.findLaboratoriosComFiltros(
                    any(), any(), any(), any(), any(), anyLong()
            )).thenReturn(Arrays.asList(resultado1, resultado2));

            LocalDateTime dataInicio = LocalDateTime.now().minusDays(30);
            LocalDateTime dataFim = LocalDateTime.now();

            List<LaboratorioRelatorioDTO> resultado = laboratorioService.obterRelatorioLaboratorios(
                    dataInicio, dataFim, null, null, "observação", 2L
            );

            assertEquals(2, resultado.size());
            assertEquals(1L, resultado.get(0).codigo());
            assertEquals("LAB 1", resultado.get(0).nome());
            assertEquals(5L, resultado.get(0).quantidadePessoas());

            assertEquals(2L, resultado.get(1).codigo());
            assertEquals("LAB 2", resultado.get(1).nome());
            assertEquals(3L, resultado.get(1).quantidadePessoas());

            verify(laboratorioRepository).findLaboratoriosComFiltros(
                    dataInicio, dataFim, null, null, "observação", 2L
            );
        }

        @Test
        @DisplayName("Deve usar quantidadeMinima 0 quando parâmetro é 0")
        void deveUsarQuantidadeMinima0QuandoParametroE0() {
            when(laboratorioRepository.findLaboratoriosComFiltros(
                    any(), any(), any(), any(), any(), anyLong()
            )).thenReturn(Collections.emptyList());

            List<LaboratorioRelatorioDTO> resultado = laboratorioService.obterRelatorioLaboratorios(
                    null, null, null, null, null, 0L
            );

            assertNotNull(resultado);
            assertTrue(resultado.isEmpty());
            verify(laboratorioRepository).findLaboratoriosComFiltros(
                    null, null, null, null, null, 0L
            );
        }

        @Test
        @DisplayName("Deve lançar IllegalArgumentException quando quantidadeMinima é negativa")
        void deveLancarIllegalArgumentExceptionQuandoQuantidadeMinimaENegativa() {
            assertThrows(IllegalArgumentException.class, () -> {
                laboratorioService.obterRelatorioLaboratorios(
                        null, null, null, null, null, -1L
                );
            });

            verify(laboratorioRepository, never()).findLaboratoriosComFiltros(
                    any(), any(), any(), any(), any(), anyLong()
            );
        }

    }
}
