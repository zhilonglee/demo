package com.example.demo.service.impl;

import com.example.demo.entity.Person;
import com.example.demo.service.RabbitMqService;
import com.example.demo.to.RabbitMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMqServiceImpl implements RabbitMqService {

    protected static Logger logger = LoggerFactory.getLogger(RabbitMqServiceImpl.class);

    private final RabbitTemplate rabbitTemplate;

    public RabbitMqServiceImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendHelloMessage(RabbitMessage msg) {
/*        RabbitAdmin  rabbitAdmin = new RabbitAdmin(rabbitTemplate);
        Queue queue = new Queue("hello");
        Exchange exchange = new DirectExchange("Myexchange",true,false);
        Binding binding = BindingBuilder.bind(queue).to(exchange).with("route").noargs();
        rabbitAdmin.declareBinding(binding);
        rabbitAdmin.declareExchange(exchange);
        rabbitAdmin.declareQueue(queue);*/

        logger.info("Send RabbitMQ message...");
        ObjectMapper mapper = new ObjectMapper();
        try {
            this.rabbitTemplate.convertAndSend(msg.getExchange(),msg.getRouteKey(),mapper.writeValueAsString(msg.getParams()));
        } catch (JsonProcessingException e) {
            logger.error("",e);
        }
    }
}
