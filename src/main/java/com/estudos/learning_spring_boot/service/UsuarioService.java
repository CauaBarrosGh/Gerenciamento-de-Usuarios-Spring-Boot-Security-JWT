package com.estudos.learning_spring_boot.service;

import com.estudos.learning_spring_boot.Usuario;
import com.estudos.learning_spring_boot.repository.UsuarioRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public Usuario criarUsuario(Usuario usuario) {
        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        rabbitTemplate.convertAndSend("usuarios.novos", usuarioSalvo.getEmail());
        return usuarioSalvo;
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
            return true;
        }
        return false;
    }

    public Optional<Usuario> atualizarUsuario(Long id, Usuario usuarioAtualizado) {
        return usuarioRepository.findById(id)
                .map(usuarioExistente -> {
                    usuarioExistente.setNome(usuarioAtualizado.getNome());
                    usuarioExistente.setEmail(usuarioAtualizado.getEmail());
                    return usuarioRepository.save(usuarioExistente);
                });
    }
}