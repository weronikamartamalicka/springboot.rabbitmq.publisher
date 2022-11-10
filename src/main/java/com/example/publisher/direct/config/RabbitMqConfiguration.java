package com.example.publisher.direct.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.PooledChannelConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfiguration {

    public static final String ROUTING_KEY_A = "routing.A";
    public static final String ROUTING_KEY_B = "routing.B";


    @Bean
    public Queue queueA() {
        return new Queue("queue.A", true, false, false, null);
    }

    @Bean
    public Queue queueB() {
        return new Queue("queue.B", true, false, false, null);
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange("exchange.Direct", true, false, null);
    }

    @Bean
    public Binding bindingA(Queue queueA, DirectExchange directExchange) {
        return BindingBuilder.bind(queueA)
                .to(directExchange)
                .with(ROUTING_KEY_A);
    }

    @Bean
    public Binding bindingB(Queue queueB, DirectExchange directExchange) {
        return BindingBuilder.bind(queueB)
                .to(directExchange)
                .with(ROUTING_KEY_B);
    }

    @Bean
    MessageConverter messageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter(objectMapper);

        return converter;
    }

    @Bean
    PooledChannelConnectionFactory connectionFactory() {
        ConnectionFactory rabbitConnectionFactory = new ConnectionFactory();
        rabbitConnectionFactory.setHost("localhost");
        PooledChannelConnectionFactory pooledChannelConnectionFactory =
                new PooledChannelConnectionFactory(rabbitConnectionFactory);

        return  pooledChannelConnectionFactory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(PooledChannelConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());

        return rabbitTemplate;
    }
}
