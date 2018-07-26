package com.example.demo.web;

import com.example.demo.entity.Person;
import com.example.demo.service.ExecutorService;
import com.example.demo.service.PersonService;
import com.example.demo.service.RabbitMqService;
import com.example.demo.to.RabbitMessage;
import com.example.demo.to.SocketMessage;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class MainController {

    private final Logger logger = LoggerFactory.getLogger(MainController.class);

    private final ExecutorService executorService;

    private final SimpMessagingTemplate messagingTemplate;

    private final RabbitMqService rabbitMqService;

    private final PersonService personService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public MainController(ExecutorService executorService, SimpMessagingTemplate messagingTemplate, RabbitMqService rabbitMqService, PersonService personService) {
        this.executorService = executorService;
        this.messagingTemplate = messagingTemplate;
        this.rabbitMqService = rabbitMqService;
        this.personService = personService;
    }

    @RequestMapping("/")
    @ResponseBody
    public String home(){
        return "Welcome to zhilong home page!";
    }

    @RequestMapping("/hello")
    public String index(final Model model) {

        model.addAttribute("name", "你好");
        //final Locale locale = LocaleContextHolder.getLocale();
        //model.addAttribute("word", this.messageSource.getMessage("word", null, locale));
        logger.info("/index -- Controller");
        return "hello";
    }

    @RequestMapping("/websocket")
    public String websocket() {
        logger.info("/websocket -- Controller");
        return "webSoket";
    }

    @ResponseBody
    @RequestMapping(value = "/genfile/asyc")
    public String asycExecutor() {

        try {
            this.executorService.generateAccessLogReport();
            this.executorService.generatePersonListReport();
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return "Completed!";
    }

    @MessageMapping("/send")
    @SendTo("/topic/send")
    public SocketMessage send(SocketMessage message) throws Exception {
        //DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        message.setDate(new Date());
        return message;
    }

    @Scheduled(fixedRate = 1000)
    //@Scheduled(cron = "0 07 20 ? * *" )
    @SendTo("/topic/callback")
    public Object callback() throws Exception {
        // send WS message
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        messagingTemplate.convertAndSend("/topic/callback", df.format(new Date()));
        return "callback";
    }

    @Scheduled(fixedRate = 1000)
    @SendTo("/topic/rabbitMQ")
    public void receiveMqMessage() throws Exception {
        SocketMessage message = new SocketMessage();
        message.setDate(new Date());
        message.setMessage(((String) rabbitTemplate.receiveAndConvert("hello")));
        if(StringUtils.isNoneBlank(message.getMessage())){
            logger.info("Message : " + message.getMessage());
            messagingTemplate.convertAndSend("/topic/rabbitMQ", message);
        }
    }

    @ResponseBody
    @GetMapping("/rabbit/person/{id}")
    public String sendRabbitMqMsg(@PathVariable Long id){

        Person person = personService.findOne(id);
        RabbitMessage rabbitMessage = new RabbitMessage("Myexchange","hello",person);
        rabbitMqService.sendHelloMessage(rabbitMessage);
        return "Sent RabbitMQ Msg...";
    }
}
