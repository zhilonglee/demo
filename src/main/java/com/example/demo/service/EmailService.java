package com.example.demo.service;

import java.util.Map;

public interface EmailService {
    void sendMessageMail(String emailFrom, String emailTo, String title, String templateName, Map params);

}
