package com.example.demo.service;

import com.example.demo.to.RabbitMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

public interface RabbitMqService extends RabbitTemplate.ReturnCallback {
    public void createQueueAndExchange(String queueName,String exchangeName,String routeingKey);
    public void sendMessage(RabbitMessage msg);
    public void sendMessageObj(RabbitMessage msg);
}
