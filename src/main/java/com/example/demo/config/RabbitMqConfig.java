package com.example.demo.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    private String queueName = "hello";
    private String queueName2 = "hello2";

    @Bean
    public Queue queue(){
        return  new Queue(queueName);
    }

    @Bean
    public Queue queue2(){
        return  new Queue(queueName2);
    }

    @Bean
    public DirectExchange exchange(){
        return new DirectExchange("Myexchange",true,false);
    }

    @Bean
    Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(queueName);
    }

    @Bean
    Binding binding2(Queue queue2, DirectExchange exchange) {
        return BindingBuilder.bind(queue2).to(exchange).with(queueName2);
    }
}
