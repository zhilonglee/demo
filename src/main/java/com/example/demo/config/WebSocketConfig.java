package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.websocket.server.ServerEndpoint;

@Configuration
//@EnableWebSocketMessageBroker Annotations represent opening the use of STOMP protocol to transport proxy-based messages
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer
{

    @Override
    public void registerStompEndpoints(StompEndpointRegistry stompEndpointRegistry) {

        //used to register STOMP protocol nodes and specify the use of the SockJS protocol.
        stompEndpointRegistry.addEndpoint("/endpoint").withSockJS();

    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        //configureMessageBroker Method is used to configure the message broker, where the message broker is /topic because we implement push
        registry.enableSimpleBroker("/topic");
    }

    /**
     * First, inject {@link ServerEndpointExporter},
     * which automatically registers the Websocket endpoint declared using the {@link ServerEndpoint} annotation.
     *Note that if you use a separate servlet container instead of using the built-in container of springboot,
     * do not inject {@link ServerEndpointExporter} because it will be provided and managed by the container itself.
     * @return
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

}
