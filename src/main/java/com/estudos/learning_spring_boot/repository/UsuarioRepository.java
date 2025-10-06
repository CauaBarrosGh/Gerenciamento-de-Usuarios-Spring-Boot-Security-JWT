package com.estudos.learning_spring_boot.repository;


import com.estudos.learning_spring_boot.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository // Opcional para interfaces que estendem JpaRepository, mas boa prática.
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Spring Data vai entender este método e criar a query:
    // "SELECT u FROM Usuario u WHERE u.email = ?"
    Optional<Usuario> findByEmail(String email);
}
