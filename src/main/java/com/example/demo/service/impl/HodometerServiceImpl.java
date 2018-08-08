package com.example.demo.service.impl;

import com.example.demo.dao.HodometerRepository;
import com.example.demo.entity.Hodometer;
import com.example.demo.entity.InterStation;
import com.example.demo.entity.Station;
import com.example.demo.entity.Train;
import com.example.demo.service.HodometerService;
import com.example.demo.to.TrainInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

@Service
public class HodometerServiceImpl implements HodometerService {

    @Autowired
    private HodometerRepository hodometerRepository;

    @Override
    public void save(Hodometer hodometer) {
        hodometerRepository.save(hodometer);
    }

    @Override
    public Hodometer findLastStationByTrainName(String trainName) {
        return hodometerRepository.findByTrainNameAndNextHodometerIsNull(trainName);
    }

    @Override
    public List<String> findTrainViaHodometersByDepAdnDest(String departName, String destName) {
        return this.hodometerRepository.findTrainViaHodometersByDepAdnDest(departName,destName);
    }

    @Override
    public List<String> findDirectTrainViaHodometersByDepAdnDest(String departName, String destName) {
        return this.hodometerRepository.findDirectTrainViaHodometersByDepAdnDest(departName,destName);
    }

    @Override
    public Long countHodometerByTrainName(String trainName) {
        return this.hodometerRepository.countHodometerByTrainName(trainName);
    }

    @Override
    public TrainInfo findHodometersByTrainNameAndInterStationPriStationNameOrDeputyStationName(String trainName, String departName, String destName) {
        List<Hodometer> hodometers = this.hodometerRepository.findHodometersByTrainNameAndInterStationPriStationNameOrDeputyStationName(trainName, departName, destName);
        TrainInfo trainInfo = new TrainInfo();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
        for (Hodometer hodometer : hodometers) {
            InterStation interStation = hodometer.getInterStation();
            Station priStation = interStation.getPriStation();
            Station deputyStation = interStation.getDeputyStation();
            Train train = hodometer.getTrain();
            if (priStation.getName().equals(departName)) {
                trainInfo.setDepartStatinName(priStation.getName());
                trainInfo.setDepartStationdate(simpleDateFormat.format(hodometer.getCostTime()));
                trainInfo.setTrainName(train.getName());
            }
            if(deputyStation.getName().equals(destName)){
                trainInfo.setDestStationName(deputyStation.getName());
                trainInfo.setDestStationdate(simpleDateFormat.format(hodometer.getCostTime()));
            }
        }
        return trainInfo;
    }
}
