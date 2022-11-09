package com.example.publisher.simple;

import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/v1")
public class Publisher {

    private final RabbitMessagingTemplate rabbitMessagingTemplate;

    @Autowired
    public Publisher(final RabbitMessagingTemplate rabbitMessagingTemplate) {
        this.rabbitMessagingTemplate = rabbitMessagingTemplate;
    }

    @RequestMapping(method = RequestMethod.GET, value = "send/{message}")

    public String sendMessage(@PathVariable String message) {
        rabbitMessagingTemplate.convertAndSend("market-notifications", message);

        return "Message has been sent";
    }

}
