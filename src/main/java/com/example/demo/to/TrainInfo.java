package com.example.demo.to;

import java.util.Date;

public class TrainInfo {

    String trainName;
    String departStatinName;
    String destStationName;
    String departStationdate;
    String destStationdate;

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
}
