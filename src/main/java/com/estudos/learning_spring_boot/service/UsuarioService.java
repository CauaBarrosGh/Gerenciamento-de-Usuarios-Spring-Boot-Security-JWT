package com.estudos.learning_spring_boot.service;

import com.estudos.learning_spring_boot.Usuario;
import com.estudos.learning_spring_boot.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service // Marca esta classe como um Bean de Serviço
public class UsuarioService {

    @Autowired // Spring, por favor INJETE uma instância de UsuarioRepository aqui.
    private UsuarioRepository usuarioRepository;

    public Usuario criarUsuario(Usuario usuario) {
        // Aqui poderiam entrar regras de negócio, como validar o email, etc.
        return usuarioRepository.save(usuario);
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public boolean deletarUsuario(Long id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            return true; // Retorna true para indicar sucesso.
        }
        return false;
    }

    public Optional<Usuario> atualizarUsuario(Long id, Usuario usuarioAtualizado) {
        // Primeiro, verificamos se o usuário com este ID realmente existe
        return usuarioRepository.findById(id)
                .map(usuarioExistente -> {
                    // Se o usuário existir (dentro do .map), atualizamos seus campos.
                    usuarioExistente.setNome(usuarioAtualizado.getNome());
                    usuarioExistente.setEmail(usuarioAtualizado.getEmail());

                    // Salvamos o usuário atualizado. O 'save' funciona como um 'update'
                    // porque o 'usuarioExistente' já tem um ID.
                    return usuarioRepository.save(usuarioExistente);
                }); // Se findById não encontrar nada, ele retorna um Optional vazio, e o .map é ignorado.
    }
}