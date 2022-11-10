package com.example.publisher.topic.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.PooledChannelConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfiguration {

    //* "*-oznacza dokładnie jedno słowo" *//
    //* "#-oznacza dowolną ilość słów oddzielonych kropkami" *//

    public static final String ROUTING_KEY_A = "routing.A";
    public static final String ROUTING_KEY_B = "routing.B";

    @Bean
    Queue queueA() {
        return new Queue("queue.A", true, false, false, null);
    }

    @Bean
    Queue queueB() {
        return new Queue("queue.B", true, false, false, null);
    }

    @Bean
    Queue queueC() {
        return new Queue("queue.C", true, false, false, null);
    }

    @Bean
    TopicExchange topicExchange() {
        return new TopicExchange("exchange.Topic", true, false, null);
    }

    @Bean
    Binding bindingA(Queue queueA, TopicExchange topicExchange) {
        return BindingBuilder.bind(queueA)
                .to(topicExchange)
                .with(ROUTING_KEY_A);
    }

    @Bean
    Binding bindingB(Queue queueB, TopicExchange topicExchange) {
        return BindingBuilder.bind(queueB)
                .to(topicExchange)
                .with(ROUTING_KEY_B);
    }

    @Bean
    Binding bindingC(Queue queueC, TopicExchange topicExchange) {
        return BindingBuilder.bind(queueC)
                .to(topicExchange)
                .with("routing.*");
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
