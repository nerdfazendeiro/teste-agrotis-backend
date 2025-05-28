package com.agrotis.teste_agrotis_backend.domain.propriedade;

import com.agrotis.teste_agrotis_backend.domain.service.PropriedadeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PropriedadeServiceTest {

    @Mock
    private PropriedadeRepository propriedadeRepository;

    @InjectMocks
    private PropriedadeService propriedadeService;

    private Propriedade propriedade;
    private PropriedadeDTO propriedadeDTO;

    @BeforeEach
    void setUp() {
        propriedade = new Propriedade(1L, "Propriedade Teste");
        propriedadeDTO = new PropriedadeDTO(1L, "Propriedade Teste");
    }

    @Nested
    @DisplayName("Testes para o método criar")
    class CriarTests {
        @Test
        @DisplayName("Deve criar propriedade com sucesso")
        void deveCriarPropriedadeComSucesso() {
            PropriedadeDTO novaPropriedadeDTO = new PropriedadeDTO(null, "Nova Propriedade");
            Propriedade novaPropriedade = Propriedade.builder().nome("Nova Propreidade").build();
            Propriedade propriedadeSalva = new Propriedade(1L, "Nova Propriedade");
            
            when(propriedadeRepository.save(any(Propriedade.class))).thenReturn(propriedadeSalva);
            
            PropriedadeDTO resultado = propriedadeService.criar(novaPropriedadeDTO);
            
            assertNotNull(resultado);
            assertEquals(1L, resultado.id());
            assertEquals("Nova Propriedade", resultado.nome());
            verify(propriedadeRepository).save(any(Propriedade.class));
        }
    }

    @Nested
    @DisplayName("Testes para o método listarTodas")
    class ListarTodasTests {
        @Test
        @DisplayName("Deve retornar lista vazia quando não há propriedades")
        void deveRetornarListaVaziaQuandoNaoHaPropriedades() {
            when(propriedadeRepository.findAll()).thenReturn(Collections.emptyList());
            
            List<PropriedadeDTO> resultado = propriedadeService.listarTodas();
            
            assertTrue(resultado.isEmpty());
            verify(propriedadeRepository).findAll();
        }

        @Test
        @DisplayName("Deve retornar lista com propriedades quando existem registros")
        void deveRetornarListaComPropriedadesQuandoExistemRegistros() {
            Propriedade propriedade2 = new Propriedade(2L, "Propriedade 2");
            
            when(propriedadeRepository.findAll()).thenReturn(Arrays.asList(propriedade, propriedade2));
            
            List<PropriedadeDTO> resultado = propriedadeService.listarTodas();
            
            assertEquals(2, resultado.size());
            assertEquals("Propriedade Teste", resultado.get(0).nome());
            assertEquals("Propriedade 2", resultado.get(1).nome());
            verify(propriedadeRepository).findAll();
        }
    }

    @Nested
    @DisplayName("Testes para o método buscarPorId")
    class BuscarPorIdTests {
        @Test
        @DisplayName("Deve retornar propriedade quando ID existe")
        void deveRetornarPropriedadeQuandoIdExiste() {
            when(propriedadeRepository.findById(1L)).thenReturn(Optional.of(propriedade));
            
            Optional<PropriedadeDTO> resultado = propriedadeService.buscarPorId(1L);
            
            assertTrue(resultado.isPresent());
            assertEquals("Propriedade Teste", resultado.get().nome());
            verify(propriedadeRepository).findById(1L);
        }

        @Test
        @DisplayName("Deve retornar Optional vazio quando ID não existe")
        void deveRetornarOptionalVazioQuandoIdNaoExiste() {
            when(propriedadeRepository.findById(99L)).thenReturn(Optional.empty());
            
            Optional<PropriedadeDTO> resultado = propriedadeService.buscarPorId(99L);
            
            assertFalse(resultado.isPresent());
            verify(propriedadeRepository).findById(99L);
        }
    }
}