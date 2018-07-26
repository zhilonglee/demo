package com.example.demo.service;

import com.example.demo.to.RabbitMessage;

public interface RabbitMqService {
    public void sendHelloMessage(RabbitMessage msg);
}
