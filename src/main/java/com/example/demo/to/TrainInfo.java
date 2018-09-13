package com.example.demo.to;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;

public class TrainInfo {

    String trainName;
    String departStatinName;
    String destStationName;
    String departStationdate;
    String destStationdate;
    String detail;
    String type;
    @JsonIgnore
    Date departDate;
    @JsonIgnore
    Date destDate;
    Long costTime;
    Integer ticketCount;

    public String getDepartStatinName() {
        return departStatinName;
    }

    public void setDepartStatinName(String departStatinName) {
        this.departStatinName = departStatinName;
    }

    public String getDestStationName() {
        return destStationName;
    }

    public void setDestStationName(String destStationName) {
        this.destStationName = destStationName;
    }

    public String getTrainName() {
        return trainName;
    }

    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }

    public String getDepartStationdate() {
        return departStationdate;
    }

    public void setDepartStationdate(String departStationdate) {
        this.departStationdate = departStationdate;
    }

    public String getDestStationdate() {
        return destStationdate;
    }

    public void setDestStationdate(String destStationdate) {
        this.destStationdate = destStationdate;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDepartDate() {
        return departDate;
    }

    public void setDepartDate(Date departDate) {
        this.departDate = departDate;
    }

    public Date getDestDate() {
        return destDate;
    }

    public void setDestDate(Date destDate) {
        this.destDate = destDate;
    }

    public Long getCostTime() {
        return costTime;
    }

    public void setCostTime(Long costTime) {
        this.costTime = costTime;
    }

    public Integer getTicketCount() {
        return ticketCount;
    }

    public void setTicketCount(Integer ticketCount) {
        this.ticketCount = ticketCount;
    }
}
