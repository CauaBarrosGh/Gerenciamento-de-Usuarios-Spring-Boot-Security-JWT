package com.estudos.learning_spring_boot;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "usuarios")
@Data // Anotação do Lombok: cria getters, setters, toString, equals, hashCode
@NoArgsConstructor // Lombok: cria construtor vazio
@AllArgsConstructor // Lombok: cria construtor com todos os argumentos
public class Usuario implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    @Column(unique = true)
    private String email; // Será nosso "username"
    private String senha; // Armazenará a senha HASHED

    // --- MÉTODOS EXIGIDOS PELO UserDetails ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Define os papéis/perfis do usuário. Para este exemplo, todos são USER.
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    // Para este exemplo simples, vamos retornar true para todos.
    // Em um sistema real, você poderia ter lógica para contas expiradas, bloqueadas, etc.
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}