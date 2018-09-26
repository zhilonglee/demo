package com.example.demo.web;

import com.example.demo.entity.*;
import com.example.demo.service.*;
import com.example.demo.to.RabbitMessage;
import com.example.demo.to.SocketMessage;
import com.example.demo.utils.RedisUtils;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.minidev.json.writer.ArraysMapper;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
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

    @Value("${executor.file.path}")
    private String uploadPath;

    @Autowired
    private  StationService stationService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private RestTemplate restTemplate;

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

    @ResponseBody
    @PostMapping("/upload")
    public HttpEntity<Object> uploadFile(@RequestParam("file") MultipartFile file){
        if (file.isEmpty()) {
            return new ResponseEntity<>("File must be uploaded!" , HttpStatus.BAD_REQUEST);
        }else {
            String originalFilename = file.getOriginalFilename();
            String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
            String path = this.uploadPath + originalFilename;

            try {
                FileCopyUtils.copy(file.getInputStream(),new FileOutputStream(path));
            } catch (Exception e) {
                String message = ExceptionUtils.getMessage(e);
                return new ResponseEntity<>(message , HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>("Upload Successfully!",HttpStatus.OK);
    }
   
    @GetMapping(value ="/news")
    public String newsPage() {
    	return "news";
    }
    
    @ResponseBody
    @GetMapping(value ="/getNews",produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public List<News> callingNeteaseCacheJson(HttpServletResponse response){
    	
        ResponseEntity<String> forEntity = restTemplate.getForEntity("http://pic.news.163.com/photocenter/api/list/0001/00AN0001,00AO0001,00AP0001/0/10/cacheMoreData.json", String.class);
        String body = forEntity.getBody();
        body = body.replace("cacheMoreData(", "");
        body = StringUtils.removeEnd(body,")");
        
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        List<News> newsList = null;
		try {
			newsList = mapper.readValue(body,new TypeReference<List<News>>() { });
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//model.addAttribute("newsList", newsList);
        //return new ResponseEntity<>(newsList,HttpStatus.OK);
        return newsList;
    }
    

}
