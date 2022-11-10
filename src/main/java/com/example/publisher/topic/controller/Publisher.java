package com.example.publisher.topic.controller;

import com.example.publisher.topic.model.Message;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v1")
public class Publisher {

    private final RabbitTemplate rabbitTemplate;

    private final TopicExchange topicExchange;

    public Publisher(RabbitTemplate rabbitTemplate, TopicExchange topicExchange) {
        this.rabbitTemplate = rabbitTemplate;
        this.topicExchange = topicExchange;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/send")
    public String send(@RequestBody Message message) {
        rabbitTemplate.convertAndSend(topicExchange.toString(), "routing.A", message);

        return "Message has been sent successfully" + message;
    }
}
