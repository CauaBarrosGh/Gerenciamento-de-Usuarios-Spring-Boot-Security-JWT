package com.estudos.learning_spring_boot;

import com.estudos.learning_spring_boot.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class LearningSpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(LearningSpringBootApplication.class, args);
	}

    @Bean
    public CommandLineRunner initialData(UsuarioRepository repository, PasswordEncoder passwordEncoder) {
        return (args) -> {
            if (repository.findByEmail("admin@email.com").isEmpty()) {
                Usuario admin = new Usuario(null, "Admin", "admin@email.com", passwordEncoder.encode("123456"));
                repository.save(admin);
                System.out.println("Usuário 'admin' criado com senha '123456'");
            }
        };
    }

}
