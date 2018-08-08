package com.example.demo.service;

import com.example.demo.entity.Hodometer;
import com.example.demo.to.TrainInfo;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HodometerService {
    public void save(Hodometer hodometer);

    public Hodometer findLastStationByTrainName(String trainName);

    public List<String> findTrainViaHodometersByDepAdnDest(String departName,String destName);

    public List<String> findDirectTrainViaHodometersByDepAdnDest(String departName,String destName);

    public Long countHodometerByTrainName(String trainName);

    public TrainInfo findHodometersByTrainNameAndInterStationPriStationNameOrDeputyStationName(String trainName, String departName, String destName);

}
