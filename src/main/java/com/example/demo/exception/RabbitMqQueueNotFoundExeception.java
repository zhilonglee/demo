package com.example.demo.exception;

public class RabbitMqQueueNotFoundExeception extends Exception {
    public RabbitMqQueueNotFoundExeception() {
        super();
    }

    public RabbitMqQueueNotFoundExeception(String message) {
        super(message);
    }

    public RabbitMqQueueNotFoundExeception(Throwable cause) {
        super(cause);
    }
}
