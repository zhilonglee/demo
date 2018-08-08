package com.example.demo.listener;

import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RabbitListener(queues = {"hello2"})
public class HelloQueListener {
    private final Logger logger = LoggerFactory.getLogger(HelloQueListener.class);

    @RabbitHandler
    public void process(String rabbitString, final Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {
        try {
            logger.info("Consumer : " + rabbitString.toString());
        }catch (Exception e){
            logger.error("",e);
        }finally {
            channel.basicAck(deliveryTag,false);
        }


    }
}
