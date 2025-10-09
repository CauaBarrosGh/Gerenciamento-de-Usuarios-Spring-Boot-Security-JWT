package com.estudos.learning_spring_boot.service;

import com.estudos.learning_spring_boot.Usuario;
import com.estudos.learning_spring_boot.repository.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    @DisplayName("Deve criar um usuário com sucesso")
    void deveCriarUsuarioComSucesso() {
        Usuario novoUsuario = new Usuario(null, "João Teste", "joao@teste.com", "senha123");
        Usuario usuarioSalvo = new Usuario(1L, "João Teste", "joao@teste.com", "senha123");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioSalvo);
        Usuario resultado = usuarioService.criarUsuario(novoUsuario);
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("João Teste", resultado.getNome());
        verify(usuarioRepository, times(1)).save(novoUsuario);
    }

    @Test
    @DisplayName("Deve encontrar um usuário por ID")
    void deveEncontrarUsuarioPorId() {
        Long id = 1L;
        Usuario usuarioMock = new Usuario(id, "Maria Teste", "maria@teste.com", "senhaHashed");
        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuarioMock));
        Optional<Usuario> resultado = usuarioService.buscarPorId(id);
        assertTrue(resultado.isPresent()); // O Optional deve conter um valor.
        assertEquals(id, resultado.get().getId());
        verify(usuarioRepository).findById(id); // Verifica se o método foi chamado.
    }

    @Test
    @DisplayName("Deve Listar Todos os Usuarios")
    void deveListarTodos(){
        Usuario user1 = new Usuario(1L, "Caua Barros","Caua@email.com", "123456");
        Usuario user2 = new Usuario(2L, "João Teste","joao@teste.com", "senha123");
        var listaDeUsuarios = List.of(user1, user2);
        when(usuarioRepository.findAll()).thenReturn(listaDeUsuarios);
        List<Usuario> resultado = usuarioService.listarTodos();
        assertThat(resultado).isNotNull();
        assertThat(resultado.size()).isEqualTo(2);
        assertThat(resultado.get(0).getNome()).isEqualTo("Caua Barros");
        assertThat(resultado.get(1).getId()).isEqualTo(2L);
        assertThat(resultado).isEqualTo(listaDeUsuarios);
    }

}

