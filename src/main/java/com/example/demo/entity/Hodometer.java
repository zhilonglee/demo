package com.example.demo.entity;

import com.example.demo.config.CustomJsonDateDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.context.annotation.Lazy;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "tbl_hodometer")
@DynamicInsert
@DynamicUpdate
public class Hodometer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Train train;

    @OneToOne(fetch = FetchType.LAZY)
    private InterStation interStation;

    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Hodometer nextHodometer;

    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Hodometer preHodometer;

    @JsonDeserialize(using = CustomJsonDateDeserializer.class)
    @Column(columnDefinition = "time comment 'pre station to last station spend time' ")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date costTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public InterStation getInterStation() {
        return interStation;
    }

    public void setInterStation(InterStation interStation) {
        this.interStation = interStation;
    }

    public Hodometer getNextHodometer() {
        return nextHodometer;
    }

    public void setNextHodometer(Hodometer nextHodometer) {
        this.nextHodometer = nextHodometer;
    }

    public Date getCostTime() {
        return costTime;
    }

    public void setCostTime(Date costTime) {
        this.costTime = costTime;
    }

    public Train getTrain() {
        return train;
    }

    public void setTrain(Train train) {
        this.train = train;
    }

    public Hodometer getPreHodometer() {
        return preHodometer;
    }

    public void setPreHodometer(Hodometer preHodometer) {
        this.preHodometer = preHodometer;
    }

}
