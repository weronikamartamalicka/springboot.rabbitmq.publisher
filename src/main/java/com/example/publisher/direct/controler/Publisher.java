package com.example.publisher.direct.controler;

import com.example.publisher.direct.config.RabbitMqConfiguration;
import com.example.publisher.direct.model.Message;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v1")
public class Publisher {

    private final RabbitTemplate rabbitTemplate;

    private final DirectExchange directExchange;

    public Publisher(RabbitTemplate rabbitTemplate, DirectExchange directExchange) {
        this.rabbitTemplate = rabbitTemplate;
        this.directExchange = directExchange;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/send")
    public String send(@RequestBody Message message) {
        rabbitTemplate.convertAndSend(directExchange.getName(), RabbitMqConfiguration.ROUTING_KEY_A, message);

        return "Message has been sent successfully";
    }
}
