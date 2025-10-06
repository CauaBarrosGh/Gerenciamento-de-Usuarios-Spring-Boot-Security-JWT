package com.estudos.learning_spring_boot;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usuarios")
@Data // Anotação do Lombok: cria getters, setters, toString, equals, hashCode
@NoArgsConstructor // Lombok: cria construtor vazio
@AllArgsConstructor // Lombok: cria construtor com todos os argumentos
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String email;
}
