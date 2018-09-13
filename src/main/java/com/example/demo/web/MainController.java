package com.example.demo.web;

import com.example.demo.entity.*;
import com.example.demo.service.*;
import com.example.demo.to.RabbitMessage;
import com.example.demo.to.SocketMessage;
import com.example.demo.utils.RedisUtils;
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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class MainController {

    private final Logger logger = LoggerFactory.getLogger(MainController.class);

    private final ExecutorService executorService;

    private final SimpMessagingTemplate messagingTemplate;

    private final RabbitMqService rabbitMqService;

    private final PersonService personService;

    private  final AccessLogService accessLogService;

    private final ProvinceService provinceService;

    @Autowired
    private  StationService stationService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RedisUtils redisUtils;

    public MainController(ExecutorService executorService, SimpMessagingTemplate messagingTemplate, RabbitMqService rabbitMqService, PersonService personService, AccessLogService accessLogService, ProvinceService provinceService) {
        this.executorService = executorService;
        this.messagingTemplate = messagingTemplate;
        this.rabbitMqService = rabbitMqService;
        this.personService = personService;
        this.accessLogService = accessLogService;
        this.provinceService = provinceService;
    }

    @RequestMapping("/")
    @ResponseBody
    public String home(){
        return "Welcome to zhilong home page!";
    }

    @RequestMapping("/hello")
    public String index(final Model model) {

        model.addAttribute("name", "helloworld");
        //final Locale locale = LocaleContextHolder.getLocale();
        //model.addAttribute("word", this.messageSource.getMessage("word", null, locale));
        logger.info("/index -- Controller");
        return "hello";
    }

    @RequestMapping("/footer")
    public String footer() {
        return "footer";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/userlogin")
    public String login() {
        return "userlogin";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/userlogin")
    public String loginPost(@ModelAttribute("province")Person model, HttpServletRequest request) {
        Person person = personService.findByName(model.getName());
        if(person != null){
            request.getSession().setAttribute("person",person);
            return "redirect:/railway";
        }else {
            return "userlogin";
        }
    }

    @RequestMapping("/railway")
    public String railwayPage(final Model model){
        List<String> labels = new ArrayList<>();
        labels.add("Station Name : ");
        labels.add("Station Code : ");
        List<Station> stations = null;
        List<Province> provinces = null;
/*        stations = (List<Station>) redisUtils.getObj("Station:stationlist");
        if (stations == null || stations.isEmpty()) {*/
            stations = this.stationService.findAll();
/*            if(null != stations && !stations.isEmpty()) {
                redisUtils.setObj("Station:stationlist", stations);
                redisUtils.expire("Station:stationlist", 30, RedisUtils.TIME_TO_MINUTES);
            }
        }*/
        provinces = (List<Province>) redisUtils.getObj("Province:provincelist");
        if (provinces == null || provinces.isEmpty()) {
            provinces = this.provinceService.findAll();
            if(null != provinces && !provinces.isEmpty()) {
                redisUtils.setObj("Province:provincelist", provinces);
                redisUtils.expire("Province:provincelist", 30, RedisUtils.TIME_TO_MINUTES);
            }
        }
        model.addAttribute("stations",stations);
        model.addAttribute("provinces",provinces);
        model.addAttribute("labels",labels);
        return "railway";
    }

    @RequestMapping("/train")
    public String trainPage(final Model model){
        return "train";
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
        rabbitMqService.sendMessage(rabbitMessage);
        return "Sent RabbitMQ Msg...";
    }

    @ResponseBody
    @GetMapping("/rabbit/access/list")
    public String sendRabbitMqBatchMsg(){

        List<AccessLog> accessLogs = accessLogService.findAll();
        for (AccessLog accessLog : accessLogs) {
            RabbitMessage rabbitMessage = new RabbitMessage("MyTopicExchange","topicQueue",accessLog);
            rabbitMqService.sendMessage(rabbitMessage);
        }
        return "Sent RabbitMQ Msg...";
    }

    @ResponseBody
    @GetMapping("/rabbit/access/one")
    public String sendRabbitMqMsgGetOne(){

        return  (String)rabbitTemplate.receiveAndConvert("topicQueue");

    }

}
