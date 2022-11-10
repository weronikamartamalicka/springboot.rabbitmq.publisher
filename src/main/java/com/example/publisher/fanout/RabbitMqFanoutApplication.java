package com.example.publisher.fanout;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RabbitMqFanoutApplication {
    public static void main(String[] args) {
        SpringApplication.run(RabbitMqFanoutApplication.class, args);
    }
}
