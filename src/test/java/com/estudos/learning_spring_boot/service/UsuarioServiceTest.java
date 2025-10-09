package com.estudos.learning_spring_boot.service;

import com.estudos.learning_spring_boot.Usuario;
import com.estudos.learning_spring_boot.repository.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Habilita a integração com Mockito
class UsuarioServiceTest {

    @Mock // 1. Diz ao Mockito para criar um "dublê" (mock) desta dependência.
    private UsuarioRepository usuarioRepository;

    @InjectMocks // 2. Cria uma instância real de UsuarioService e INJETA os mocks (@Mock) nela.
    private UsuarioService usuarioService;

    @Test // 3. Marca este método como um caso de teste.
    @DisplayName("Deve criar um usuário com sucesso")
    void deveCriarUsuarioComSucesso() {
        // Padrão de Teste: Arrange, Act, Assert (AAA)

        // ARRANGE (Arranjar / Preparar)
        // Criamos os objetos que vamos usar no teste.
        Usuario novoUsuario = new Usuario(null, "João Teste", "joao@teste.com", "senha123");
        Usuario usuarioSalvo = new Usuario(1L, "João Teste", "joao@teste.com", "senha123");

        // Ensinamos o Mockito como o "dublê" deve se comportar:
        // "QUANDO (when) o método 'save' do nosso repositório for chamado com QUALQUER objeto
        // da classe Usuario, ENTÃO RETORNE (thenReturn) o objeto 'usuarioSalvo'."
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioSalvo);

        // ACT (Agir)
        // Executamos o método que queremos testar.
        Usuario resultado = usuarioService.criarUsuario(novoUsuario);

        // ASSERT (Afirmar / Verificar)
        // Verificamos se o resultado foi o esperado.
        assertNotNull(resultado); // O resultado não deve ser nulo.
        assertEquals(1L, resultado.getId()); // O ID deve ser o que definimos no 'usuarioSalvo'.
        assertEquals("João Teste", resultado.getNome());

        // Também podemos verificar se o mock foi chamado como esperado:
        // "Verifique (verify) que o 'usuarioRepository' teve seu método 'save' chamado
        // exatamente 1 vez com o objeto 'novoUsuario'."
        verify(usuarioRepository, times(1)).save(novoUsuario);
    }

    @Test
    @DisplayName("Deve encontrar um usuário por ID")
    void deveEncontrarUsuarioPorId() {
        // ARRANGE
        Long id = 1L;
        Usuario usuarioMock = new Usuario(id, "Maria Teste", "maria@teste.com", "senhaHashed");

        // Ensinando o mock: quando findById(1L) for chamado, retorne um Optional com nosso usuário.
        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuarioMock));

        // ACT
        Optional<Usuario> resultado = usuarioService.buscarPorId(id);

        // ASSERT
        assertTrue(resultado.isPresent()); // O Optional deve conter um valor.
        assertEquals(id, resultado.get().getId());
        verify(usuarioRepository).findById(id); // Verifica se o método foi chamado.
    }

    @Test
    @DisplayName("Deve Listar Todos os Usuarios")
    void deveListarTodos(){

        //ARRANGE - CRIAR OS OBJETOS PARA O TESTE
        Usuario user1 = new Usuario(1L, "Caua Barros","Caua@email.com", "123456");
        Usuario user2 = new Usuario(2L, "João Teste","joao@teste.com", "senha123");
        var listaDeUsuarios = List.of(user1, user2);

        // ENSINANDO O MOCK QUANDO O FINDALL FOR CHAMADO RETORNAR NOSSA LISTA DE USUARIOS
        when(usuarioRepository.findAll()).thenReturn(listaDeUsuarios);

        //ACT AGIR EXECUTAMOS O METODO QUE QUEREMOS TESTAR E COLOCAMOS NA NOSSA VARIAVEL
        List<Usuario> resultado = usuarioService.listarTodos();

        //ASSERT VERIFICAR SE O RESULTADO FOI COMO ESPERADO NO CASO RETORNAR TODOS OS USUARIOS
        assertThat(resultado).isNotNull(); // A lista não deve ser nula
        assertThat(resultado.size()).isEqualTo(2); // O tamanho da lista deve ser 2
        assertThat(resultado.get(0).getNome()).isEqualTo("Caua Barros"); // O primeiro usuário deve ser o Caua
        assertThat(resultado.get(1).getId()).isEqualTo(2L); // O ID do segundo usuário deve ser 2
        assertThat(resultado).isEqualTo(listaDeUsuarios); // A lista resultante é exatamente a que esperamos
    }

}

