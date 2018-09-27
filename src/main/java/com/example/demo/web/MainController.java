package com.example.demo.web;

import com.example.demo.entity.*;
import com.example.demo.service.*;
import com.example.demo.to.*;
import com.example.demo.to.eum.ReqType;
import com.example.demo.utils.RedisUtils;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.data.repository.query.Param;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;




import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;


import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;

import java.text.SimpleDateFormat;
import java.util.*;

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

    @Autowired
    private EmailService emailService;

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

    @RequestMapping("/tulingchat")
    public String tulingcat() {

        return "tulingchat";
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
/*        for (AccessLog accessLog : accessLogs) {
            RabbitMessage rabbitMessage = new RabbitMessage("MyTopicExchange","topicQueue",accessLog);
            rabbitMqService.sendMessage(rabbitMessage);
        }*/
        accessLogs.forEach(accessLog -> {
            RabbitMessage rabbitMessage = new RabbitMessage("MyTopicExchange","topicQueue",accessLog);
            rabbitMqService.sendMessage(rabbitMessage);
        });
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
   
    @RequestMapping(value ="/news")
    public String newsPage() {
    	return "news";
    }
    
    @ResponseBody
    @GetMapping(value ="/newsList",produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public List<News> callingNeteaseCacheJson(){
    	
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
		    logger.error("",e);
		} catch (JsonMappingException e) {
            logger.error("",e);
		} catch (IOException e) {
            logger.error("",e);
		}
		//model.addAttribute("newsList", newsList);
        //return new ResponseEntity<>(newsList,HttpStatus.OK);
        return newsList;
    }
    

    @ResponseBody
    @GetMapping("/send/email")
    public HttpEntity<Object> sendEmail(@Param(value="toEmail") String toEmail){
        Map map = new HashMap<String,Object>();
        map.put("name","hujiabin");
        map.put("message","huajiabinshabi");
        map.put("description","hujiabinzhendeshishabima?");
        map.put("createdDate",new Date());
        map.put("status","良好");
        emailService.sendMessageMail("zhilong.li@ncs.com.sg",toEmail,"HELLO WORLD","mailTemplate",map);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @ResponseBody
    @RequestMapping(value = "/git", method = RequestMethod.GET)
    public String gitInformation() {
        return readGitProperties();
    }
    private String readGitProperties() {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("git.properties");
        try {
            return readFromInputStream(inputStream);
        } catch (IOException e) {
            logger.error("",e);
            return "Version information could not be retrieved";
        }
    }
    private String readFromInputStream(InputStream inputStream)
            throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }

    @ResponseBody
    @GetMapping(value ="/tuling",produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public HttpEntity<String> callingTulingRebot(@RequestParam(value = "text") String text){
        TulingRequestTo tulingRequestTo = new TulingRequestTo();
        tulingRequestTo.setReqType(ReqType.TEXT.ordinal());
        Perception perception = new Perception();
        Perception.InputText inputText = new Perception.InputText();
        inputText.setText(text);
        SelfInfo selfInfo = new SelfInfo();
        SelfInfo.Location location = new SelfInfo.Location();
        location.setCity("北京");
        location.setProvince("北京");
        selfInfo.setLocation(location);
        Perception.InputImage inputImage = new Perception.InputImage();
        Perception.InputMedia inputMedia = new Perception.InputMedia();
        perception.setInputText(inputText);
        perception.setInputMedia(inputMedia);
        perception.setInputImage(inputImage);
        perception.setSelfInfo(selfInfo);
        tulingRequestTo.setPerception(perception);
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId("chaochao");
        tulingRequestTo.setUserInfo(userInfo);
        ObjectMapper objectMapper = new ObjectMapper();
        String object = "";
        try {
            object = objectMapper.writeValueAsString(tulingRequestTo);
            logger.info("Tuling Request param : " + object);
        } catch (JsonProcessingException e) {
            logger.error("",e);
        }

/**
 * Create a new instance of the {@link RestTemplate} using default settings.
 * Default {@link HttpMessageConverter}s are initialized.

	public RestTemplate() {
            this.messageConverters.add(new ByteArrayHttpMessageConverter());
            this.messageConverters.add(new StringHttpMessageConverter());
            this.messageConverters.add(new ResourceHttpMessageConverter(false));
            this.messageConverters.add(new SourceHttpMessageConverter<>());
            this.messageConverters.add(new AllEncompassingFormHttpMessageConverter());
 */
    // StringHttpMessageConverter default charset is ISO-8859-1
        /**
         * A default constructor that uses {@code "ISO-8859-1"} as the default charset.
         * @see #StringHttpMessageConverter(Charset)

            public StringHttpMessageConverter() {
                    this(DEFAULT_CHARSET);
                }
         */
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));

        ResponseEntity<String> forEntity = restTemplate.postForEntity("http://openapi.tuling123.com/openapi/api/v2",object,String.class);
        String body = forEntity.getBody();
        return new ResponseEntity<>(body,HttpStatus.OK);
    }
}
