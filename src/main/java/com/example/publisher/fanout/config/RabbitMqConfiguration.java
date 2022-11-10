package com.example.publisher.fanout.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.PooledChannelConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfiguration {

    @Bean
    Queue queueA() {
        return new Queue("queue.A", true, false, false, null);
    }

    @Bean
    Queue queueB() {
        return new Queue("queue.B", true, false, false, null);
    }

    @Bean
    FanoutExchange fanoutExchange() {
        return new FanoutExchange("exchange.Fanout", true, false, null);
    }

    @Bean
    Binding bindingA(Queue queueA, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(queueA)
                .to(fanoutExchange);
    }

    @Bean
    Binding bindingB(Queue queueB, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(queueB)
                .to(fanoutExchange);
    }

    @Bean
    MessageConverter messageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        MessageConverter messageConverter = new Jackson2JsonMessageConverter(objectMapper);

        return messageConverter;
    }

    @Bean
    PooledChannelConnectionFactory connectionFactory() {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");

        return new PooledChannelConnectionFactory(connectionFactory);
    }

    @Bean
    RabbitTemplate rabbitTemplate(MessageConverter messageConverter, PooledChannelConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);

        return rabbitTemplate;
    }

}
