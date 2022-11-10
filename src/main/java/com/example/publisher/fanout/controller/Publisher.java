package com.example.publisher.fanout.controller;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.example.publisher.fanout.model.Message;

@RestController
@RequestMapping(value = "/v1")
public class Publisher {

    private final RabbitTemplate rabbitTemplate;

    private final FanoutExchange fanoutExchange;

    public Publisher(RabbitTemplate rabbitTemplate, FanoutExchange fanoutExchange) {
        this.rabbitTemplate = rabbitTemplate;
        this.fanoutExchange = fanoutExchange;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/send")
    public String send(@RequestBody Message message) {
        rabbitTemplate.convertAndSend(fanoutExchange.toString(), "", message);

        return "Message has been sent successfully" + message;
    }
}
