package com.example.demo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "http.proxy")
public class ProxyConfig {

    private Boolean enable;

    private String ip;

    private Integer port;

    public Boolean getEnable() {
        return enable;
    }

    public String getIp() {
        return ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPort(Integer port) {
        this.port = port;
    }
}
