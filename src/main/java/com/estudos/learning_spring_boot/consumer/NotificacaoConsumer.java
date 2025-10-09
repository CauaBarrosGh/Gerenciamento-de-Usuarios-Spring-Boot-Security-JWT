package com.estudos.learning_spring_boot.consumer;

import com.estudos.learning_spring_boot.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class NotificacaoConsumer {
    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void receberMensagem(String email) {
        System.out.println("======================================");
        System.out.println("CONSUMIDOR: Mensagem recebida da fila!");
        System.out.println("Simulando envio de e-mail de boas-vindas para: " + email);
        System.out.println("======================================");
    }
}