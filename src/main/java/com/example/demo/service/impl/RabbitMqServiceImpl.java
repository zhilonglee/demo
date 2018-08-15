package com.example.demo.service.impl;

import com.example.demo.entity.Person;
import com.example.demo.service.RabbitMqService;
import com.example.demo.to.RabbitMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public class RabbitMqServiceImpl implements RabbitMqService {

    protected static Logger logger = LoggerFactory.getLogger(RabbitMqServiceImpl.class);

    private final RabbitTemplate rabbitTemplate;

    private final RabbitAdmin rabbitAdmin;

    public RabbitMqServiceImpl(RabbitTemplate rabbitTemplate, RabbitAdmin rabbitAdmin) {
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitAdmin = rabbitAdmin;
    }

    public void createQueueAndExchange(String queueName,String exchangeName,String routeingKey){
        Queue trainQueue = new Queue(queueName);
        DirectExchange trainDirectExchange = new DirectExchange(exchangeName, true, false);
        this.rabbitAdmin.declareQueue(trainQueue);
        this.rabbitAdmin.declareExchange(trainDirectExchange);
        this.rabbitAdmin.declareBinding(BindingBuilder.bind(trainQueue).to(trainDirectExchange).with(routeingKey));

    }

    public void sendMessage(RabbitMessage msg) {
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

    public void sendMessageObj(RabbitMessage msg) {
        logger.info("Send RabbitMQ message...");
        this.rabbitTemplate.setReturnCallback(this);
        this.rabbitTemplate.setConfirmCallback(((correlationData, ack, cause) -> {
            if(!ack){
                logger.error("",new Exception("Message sending failure : " + cause + correlationData.toString()));
            }else{
                logger.info("Send message successfully.");
            }
        }));
        this.rabbitTemplate.convertAndSend(msg.getExchange(),msg.getRouteKey(), RabbitMessage.getSerialBytes((Serializable)msg.getParams()));
    }

    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        logger.info("sender return success" + message.toString()+"==="+replyCode+"==="+replyText+"==="+routingKey);
    }
}
