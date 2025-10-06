package com.estudos.learning_spring_boot.controllers;

import com.estudos.learning_spring_boot.Usuario;
import com.estudos.learning_spring_boot.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios") // Prefixo para todos os endpoints neste controller
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // Endpoint para criar um novo usuário
    // Ex: POST http://localhost:8080/usuarios
    @PostMapping
    public Usuario criar(@RequestBody Usuario usuario) {
        return usuarioService.criarUsuario(usuario);
    }

    // Endpoint para listar todos os usuários
    // Ex: GET http://localhost:8080/usuarios
    @GetMapping
    public List<Usuario> listar() {
        return usuarioService.listarTodos();
    }

    // Endpoint para buscar um usuário por ID
    // Ex: GET http://localhost:8080/usuarios/1
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Long id) {
        return usuarioService.buscarPorId(id) // O método buscarPorId estaria no service
                .map(ResponseEntity::ok) // Se encontrou, retorna 200 OK com o usuário
                .orElse(ResponseEntity.notFound().build()); // Se não, retorna 404 Not Found
    }

    @DeleteMapping("/{id}") // Mapeia para requisições HTTP DELETE na URL /usuarios/{id}
    public ResponseEntity<Void> deletarPorId(@PathVariable Long id) {
        // Pede ao Service para tentar deletar o usuário
        boolean sucesso = usuarioService.deletarUsuario(id);

        if (sucesso) {
            // Se o service retornou true, a deleção foi bem-sucedida.
            // Retornamos 204 No Content.
            return ResponseEntity.noContent().build();
        } else {
            // Se o service retornou false, o usuário não foi encontrado.
            // Retornamos 404 Not Found.
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}") // Mapeia para requisições HTTP PUT na URL /usuarios/{id}
    public ResponseEntity<Usuario> atualizar(@PathVariable Long id, @RequestBody Usuario usuarioAtualizado) {
        // Pede ao Service para tentar atualizar, passando o ID e os novos dados
        Optional<Usuario> usuario = usuarioService.atualizarUsuario(id, usuarioAtualizado);

        // O 'map' e 'orElse' funciona exatamente como no 'buscarPorId'
        return usuario
                .map(ResponseEntity::ok) // Se o Optional contiver um usuário, retorna 200 OK com ele
                .orElse(ResponseEntity.notFound().build()); // Se o Optional estiver vazio, retorna 404 Not Found
    }
}
