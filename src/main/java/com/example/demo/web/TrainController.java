package com.example.demo.web;

import com.example.demo.entity.Hodometer;
import com.example.demo.entity.InterStation;
import com.example.demo.entity.Station;
import com.example.demo.entity.Train;
import com.example.demo.service.HodometerService;
import com.example.demo.service.InterSationService;
import com.example.demo.service.StationService;
import com.example.demo.service.TrainService;
import com.example.demo.to.TrainInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping(value = "/v1/train")
public class TrainController {

    @Autowired
    private TrainService trainService;

    @Autowired
    private HodometerService hodometerService;

    @Autowired
    private StationService stationService;

    @Autowired
    private InterSationService interSationService;

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
}
