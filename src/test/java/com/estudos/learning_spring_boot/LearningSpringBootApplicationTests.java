package com.estudos.learning_spring_boot;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = { "spring.amqp.rabbit.enabled=false" })
class LearningSpringBootApplicationTests {

    @Test
    void contextLoads() {
    }
}
