package com.estudos.learning_spring_boot.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_NAME = "usuarios.novos";

    @Bean
    public Queue FilaDeNovosUsuarios() {
        return new Queue(QUEUE_NAME, true);
    }
}