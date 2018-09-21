package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;

@Configuration
@ConditionalOnClass(ProxyConfig.class)
public class WebConfig {

        @Autowired
        private ProxyConfig proxyConfig;

        @Autowired
        private RestTemplateBuilder builder;

        @Bean
        public SimpleClientHttpRequestFactory httpClientFactory(){
            SimpleClientHttpRequestFactory httpRequestFactory = new SimpleClientHttpRequestFactory();
            // httpRequestFactory.setReadTimeout(readTimeout);
            // httpRequestFactory.setConnectTimeout(connectionTimeout);

            if(proxyConfig.getEnable()){
                SocketAddress address = new InetSocketAddress(proxyConfig.getIp(), proxyConfig.getPort());
                Proxy proxy = new Proxy(Proxy.Type.HTTP, address);
                httpRequestFactory.setProxy(proxy);
            }

            return httpRequestFactory;
        }

        @Bean
        public RestTemplate restTemplate(SimpleClientHttpRequestFactory httpClientFactory) {
            //builder.basicAuthorization("user", "password")
            //return builder.build();
            return new RestTemplate(httpClientFactory);
        }

    }
