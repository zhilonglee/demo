package com.example.demo.web;

import com.example.demo.entity.*;
import com.example.demo.entity.eum.TicketStatus;
import com.example.demo.entity.eum.TicketType;
import com.example.demo.exception.RabbitMqQueueNotFoundExeception;
import com.example.demo.service.*;
import com.example.demo.to.RabbitMessage;
import com.example.demo.to.ToResult;
import com.example.demo.to.TrainInfo;
import com.example.demo.to.eum.ServerStatus;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.SessionScope;


import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping(value = "/v1/train")
@SessionAttributes(value = "person",types = Person.class)
public class TrainController {

    private final Logger logger = LoggerFactory.getLogger(TrainController.class);

    @Autowired
    private TrainService trainService;

    @Autowired
    private HodometerService hodometerService;

/*    @Autowired
    private StationService stationService;*/

    @Autowired
    private InterSationService interSationService;

    @Autowired
    private RabbitMqService rabbitMqService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private PersonService personService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    public TrainController() {
        TimeZone tz = TimeZone.getTimeZone("ETC/GMT-8");
        TimeZone.setDefault(tz);
    }

    @PostMapping(value = "save")
    public String saveTrain(@RequestBody Train train){
        trainService.save(train);
        return train.toString();
    }

    @PostMapping(value = "hodometer/save")
    public String saveTrainHodometer(@RequestBody Hodometer hodometer) {
        Train train = trainService.findByName(hodometer.getTrain().getName());
        if(train == null){
            return ("The train info does not exist in DB.");
        }else{
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(hodometer.getCostTime());
            calendar.add(Calendar.HOUR_OF_DAY,8);
            hodometer.setCostTime(calendar.getTime());
            hodometer.setTrain(train);
        }
/*        Long count = this.hodometerService.countHodometerByTrainName(hodometer.getTrain().getName());*/

            InterStation interStation = hodometer.getInterStation();
            interStation = interSationService.findInterStationByPriStationNameAndDeputyStationName(interStation.getPriStation().getName(), interStation.getDeputyStation().getName());
            if(interStation == null){
                return ("The inter station info does not exist in DB.");
            }else{
                hodometer.setInterStation(interStation);
            }
/*        if(count == null || count <= 0 ){
        }*/
/*        Station station = stationService.findByName(hodometer.getStation().getName());
        if(station == null){
            return ("The station info does not exist in DB.");
        }else{
            hodometer.setStation(station);
        }*/
        Hodometer nextHodometer = hodometerService.findLastStationByTrainName(hodometer.getTrain().getName());
        if(nextHodometer != null) {
            hodometer.setPreHodometer(nextHodometer);
        }
        hodometerService.save(hodometer);
        if(nextHodometer != null) {
            nextHodometer.setNextHodometer(hodometer);
            hodometerService.save(nextHodometer);
        }
        return "finished !";
    }

    @GetMapping("/hodometer/{trainName}")
    public String searchHodometerLastStationByTrainName(@PathVariable("trainName") String trainName){
        Hodometer hodometer = hodometerService.findLastStationByTrainName(trainName);
        //return hodometer.getStation().getName();
        return hodometer.getInterStation().getPriStation().getName() + " --> " +hodometer.getInterStation().getDeputyStation().getName();
    }

    @GetMapping("/depAndDest")
    public List<TrainInfo> findTrainByDepartureAndDestination(String departureName , String destinationName){
        List<String> trainNames = hodometerService.findDirectTrainViaHodometersByDepAdnDest(departureName, destinationName);
        List<String> trainsName = hodometerService.findTrainViaHodometersByDepAdnDest(departureName, destinationName);
        if(trainNames == null || trainNames.isEmpty()) {
            trainNames = new LinkedList<String>();
        }
        trainNames.addAll(trainsName);
        List<TrainInfo> trainInfos = new ArrayList<TrainInfo>();
        for (String trainName : trainNames) {
            TrainInfo trainInfo = this.hodometerService.findHodometersByTrainNameAndInterStationPriStationNameOrDeputyStationName(trainName, departureName, destinationName);
            trainInfos.add(trainInfo);
        }
        return trainInfos;
    }

    @GetMapping("/hodometer/count/{trainName}")
    public Long countHodometersByTrainName(@PathVariable(name = "trainName") String trainName){
        return this.hodometerService.countHodometerByTrainName(trainName);
    }


    @GetMapping("/rabbit/ticket/{trainName}")
    public String sellTicketMsg(@PathVariable("trainName") String trainName){
        rabbitMqService.createQueueAndExchange(trainName,"train",trainName);
        List<Ticket> tickets = Ticket.createTickets(18l, 25l, 6l, trainName);
        for (Ticket ticket : tickets) {
            RabbitMessage rabbitMessage = new RabbitMessage("train",trainName,ticket);
            //rabbitMqService.sendMessage(rabbitMessage);
            rabbitMqService.sendMessageObj(rabbitMessage);
        }

        return "Sent RabbitMQ Msg...";
    }

    @PostMapping("/rabbit/train/{trainName}")
    public ToResult sendRabbitMqMsgGetOne(@PathVariable("trainName") String trainName,
                                          //@ModelAttribute("person") Person person,
                                          @RequestBody Ticket basicTicket, final Channel channel){
        Person person = (Person)this.httpServletRequest.getSession().getAttribute("person");
        Person personInDb = null;
        if(person == null){
            return ToResult.buildToResult(ToResult.REQUESTREDIRECT,ServerStatus.REDIRECT).addDetail("userlogin");
        }else{
            personInDb = personService.findByName(person.getName());
        }
        ToResult toResult = null;
        Ticket ticket = null;

        //byte[] bytes = (byte[]) rabbitTemplate.receiveAndConvert(trainName);
        try {
            byte[] bytes = new byte[0];
            try {
                Message message = rabbitTemplate.receive(trainName);
                bytes = message.getBody();
            } catch (Exception e) {
                throw new RabbitMqQueueNotFoundExeception(e);
            }
            List<Object> objs = (List<Object>)RabbitMessage.getObjectFromBytes(bytes);
            if(objs != null && !objs.isEmpty() ){
                ticket = (Ticket)objs.get(0);
            }
            if (ticket != null) {
                Calendar calendar = Calendar.getInstance();
                ticket.setPrice(basicTicket.getPrice());
                ticket.setDeparture(basicTicket.getDeparture());
                ticket.setDestination(basicTicket.getDestination());
                calendar.setTime(basicTicket.getDepartureTime());
                int hh1 = calendar.get(Calendar.HOUR_OF_DAY);
                int mm1 = calendar.get(Calendar.MINUTE);
                //calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
                ticket.setDepartureTime(basicTicket.getDepartureTime());
                calendar.setTime(basicTicket.getDestinationTime());
                int hh = calendar.get(Calendar.HOUR_OF_DAY);
                int mm = calendar.get(Calendar.MINUTE);
                ticket.setDestinationTime(basicTicket.getDestinationTime());
                if(mm < mm1){
                    mm = mm + 60 - mm1;
                    hh = hh - 1 -hh1;
                }else {
                    mm = mm - mm1;
                    hh = hh - hh1;
                }
                BigDecimal price = BigDecimal.valueOf(0.0);
                if(ticket.getTicketType().equals(TicketType.PRIMARYCLASS)){
                    price = BigDecimal.valueOf(((double)mm / 60) * 50 + hh * 50);
                }else if (ticket.getTicketType().equals(TicketType.FIRSTCLASS)){
                    price = BigDecimal.valueOf(((double)mm / 60) * 30 + hh * 30);
                }else if (ticket.getTicketType().equals(TicketType.SECONDCLASS)){
                    price = BigDecimal.valueOf(((double)mm / 60) * 20 + hh * 20);
                }
                ticket.setPrice(price);
                ticket.setTicketStatus(TicketStatus.INIT);
                ticket.setPerson(personInDb);
                calendar.setTime(new Date());
                calendar.add(Calendar.HOUR_OF_DAY,8);
                ticket.setPurchaseDate(calendar.getTime());
                this.ticketService.save(ticket);
                //channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
                toResult = ToResult.buildToResult().addData(ticket);
            }
        } catch (RabbitMqQueueNotFoundExeception e){
            logger.error("",e);
            toResult = ToResult.buildBadRequestToResult().addDetail(trainName + " train tickets are not yet on sale.");
        } catch (Exception e) {
            logger.error("",e);
            toResult = ToResult.buildBadRequestToResult().addDetail("Purchase failed.");
/*            try {
                channel.basicNack(message.getMessageProperties().getDeliveryTag(),false,true);
            } catch (IOException e1) {
                logger.error("",e1);
            }*/
        }

        return toResult;

    }

    @GetMapping("/ticket/list")
    public ToResult tickets(){
        Person person = (Person)this.httpServletRequest.getSession().getAttribute("person");
        Person personInDb = null;
        ToResult toResult = null;
        try {
            if(person == null){
                return ToResult.buildToResult(ToResult.REQUESTREDIRECT,ServerStatus.REDIRECT).addDetail("userlogin");
            }else{
                personInDb = personService.findByName(person.getName());
            }
            List<Ticket> tickets = this.ticketService.findTicketsByPersonName(personInDb.getName());
            toResult = ToResult.buildToResult().addData(tickets);
        } catch (Exception e) {
            logger.error("",e);
            toResult = ToResult.buildBadRequestToResult().addDetail("Ticket search failed.Unknown Exception.");
        }
        return toResult;
    }

    @PostMapping("/ticket/payment")
    public ToResult payTicket(@RequestBody Ticket ticket){
        ToResult toResult = null;
        try {
            Ticket ticketById = this.ticketService.findTicketById(ticket.getId());
            if(ticketById == null){
                toResult = ToResult.buildBadRequestToResult().addDetail("No Ticket Info.");
            }else{
                if(ticketById.getTicketStatus().equals(TicketStatus.PAID)){
                    return toResult = ToResult.buildBadRequestToResult().addDetail("Paid Duplicate Ticket.");
                }else if(ticketById.getTicketStatus().equals(TicketStatus.REFUND)){
                    return toResult = ToResult.buildBadRequestToResult().addDetail("Refunded Ticket.");
                }
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                calendar.add(Calendar.HOUR_OF_DAY,8);
                ticketById.setPaymentDate(calendar.getTime());
                ticketById.setTicketStatus(TicketStatus.PAID);
                this.ticketService.save(ticketById);
                toResult = ToResult.buildToResult().addData(ticketById);
            }
        } catch (Exception e) {
            logger.error("",e);
            toResult = ToResult.buildBadRequestToResult().addDetail("Sorry,payment failed!");
        }

        return toResult;
    }


    @PostMapping("/ticket/refund")
    public ToResult refundTicket(@RequestBody Ticket ticket){
        ToResult toResult = null;
        try {
            Ticket ticketById = this.ticketService.findTicketById(ticket.getId());
            if(ticketById == null){
                toResult = ToResult.buildBadRequestToResult().addDetail("No Ticket Info.");
            }else {
                if(ticketById.getTicketStatus().equals(TicketStatus.REFUND)){
                    return toResult = ToResult.buildBadRequestToResult().addDetail("Refund Duplicate Ticket.");
                }
                ticketById.setTicketStatus(TicketStatus.REFUND);
                this.ticketService.save(ticketById);
                ticket = new Ticket(ticketById.getCarriageNo(),ticketById.getRow() ,ticketById.getColumn(),ticketById.getTrainName());
                ticket.setTicketType(ticketById.getTicketType());
                RabbitMessage rabbitMessage = new RabbitMessage("train",ticket.getTrainName(),ticket);
                //rabbitMqService.sendMessage(rabbitMessage);
                rabbitMqService.sendMessageObj(rabbitMessage);
                toResult = ToResult.buildToResult().addData(ticket);
            }
        } catch (Exception e) {
            logger.error("",e);
            toResult = ToResult.buildBadRequestToResult().addDetail("Sorry,Refunds failed!");
        }
        return toResult;
    }
}
