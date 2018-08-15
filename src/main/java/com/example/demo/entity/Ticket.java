package com.example.demo.entity;

import com.example.demo.config.CustomJsonDateDeserializer;
import com.example.demo.config.CustomJsonTimeDeserializer;
import com.example.demo.entity.eum.TicketStatus;
import com.example.demo.entity.eum.TicketType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

@Entity
@Table(name = "tbl_train_ticket",uniqueConstraints = {@UniqueConstraint(columnNames={"trainName","creationDay","p_id"})})
public class Ticket implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private String trainName;

    @Column
    private TicketType ticketType;

    @Column
    private TicketStatus ticketStatus;

    @Column
    private Long carriageNo;

    @Column
    private Long row;

    @Column(name = "r_column")
    private Long column;

    @JsonFormat(timezone = "ETC/GMT-8",pattern = "yyyy-MM-dd HH:mm:ss")
    @Column
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date creationDay;

    @Column(precision = 6, scale = 2)
    private BigDecimal price;

    @JsonFormat(timezone = "ETC/GMT-8",pattern = "HH:mm:ss")
    @Column(columnDefinition = "time")
    //@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = CustomJsonTimeDeserializer.class)
    @DateTimeFormat(pattern = "HH:mm:ss")
    private Date departureTime;

    @JsonFormat(timezone = "ETC/GMT-8",pattern = "HH:mm:ss")
    @Column(columnDefinition = "time")
    //@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = CustomJsonTimeDeserializer.class)
    @DateTimeFormat(pattern = "HH:mm:ss")
    private Date destinationTime;

    @Column
    private String departure;

    @Column
    private String destination;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "p_id",referencedColumnName = "id")
    private Person person;

    @JsonFormat(timezone = "ETC/GMT-8",pattern = "yyyy-MM-dd HH:mm:ss")
    @Column
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date purchaseDate;

    @JsonFormat(timezone = "ETC/GMT-8",pattern = "yyyy-MM-dd HH:mm:ss")
    @Column
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date paymentDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TicketType getTicketType() {
        return this.ticketType;
    }

    public void setTicketType(TicketType ticketType) {
        this.ticketType = ticketType;
    }

    public Long getCarriageNo() {
        return carriageNo;
    }

    public void setCarriageNo(Long carriageNo) {
        this.carriageNo = carriageNo;
    }

    public Long getRow() {
        return row;
    }

    public void setRow(Long row) {
        this.row = row;
    }

    public Long getColumn() {
        return column;
    }

    public void setColumn(Long column) {
        this.column = column;
    }

    public String getTrainName() {
        return trainName;
    }

    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Date getCreationDay() {
        return creationDay;
    }

    public void setCreationDay(Date creationDay) {
        this.creationDay = creationDay;
    }

    public Date getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Date departureTime) {
        this.departureTime = departureTime;
    }

    public Date getDestinationTime() {
        return destinationTime;
    }

    public void setDestinationTime(Date destinationTime) {
        this.destinationTime = destinationTime;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public TicketStatus getTicketStatus() {
        return ticketStatus;
    }

    public void setTicketStatus(TicketStatus ticketStatus) {
        this.ticketStatus = ticketStatus;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Ticket(Long carriageNo, Long row, Long column, String trainName) {
        this.carriageNo = carriageNo;
        this.row = row;
        this.column = column;
        this.trainName = trainName;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR_OF_DAY,8);
        this.creationDay = calendar.getTime();
    }

    public Ticket() {
    }


    public static List<Ticket> createTickets(Long carriageNo, Long carriageRow, Long carriageColumn,String trainName){
        List<Ticket> tickets = new ArrayList<>();
        for (long i = 0; i < carriageNo; i++) {
            for (long r = 0; r < carriageRow; r++) {
                for (long c = 0; c < carriageColumn; c++) {
                    Ticket ticket = new Ticket(i+1,r+1,c+1,trainName);
                    if(r == 0 || r == 1){
                        ticket.setTicketType(TicketType.PRIMARYCLASS);
                    }else if((c == 0) || (c == carriageColumn-1)
                            //|| (c == carriageColumn/2 + 1 ) || (c == carriageColumn/2 - 1 )
                            ){
                        ticket.setTicketType(TicketType.FIRSTCLASS);
                    }else{
                        ticket.setTicketType(TicketType.SECONDCLASS);
                    }
                    tickets.add(ticket);
                }
            }
        }
        return  tickets;
    }
}
