package com.example.demo.service.impl;

import com.example.demo.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Map;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Override
    public void sendMessageMail(String emailFrom, String emailTo, String title, String templateName, Map params) {
        MimeMessage mailMessage = javaMailSender.createMimeMessage();
        try {
        //if multipart param is true, then will enable sending attachments function
        MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage, true);
        Context context = new Context();
        context.setVariables(params);
        //Fetch html template text
        String process = templateEngine.process(templateName, context);

            messageHelper.setFrom(emailFrom);
            messageHelper.setTo(emailTo);
            messageHelper.setSubject(title);
            messageHelper.setText(process, true);
        } catch (MessagingException e) {
            logger.error("",e);
        }
        javaMailSender.send(mailMessage);

    }
}
