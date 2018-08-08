package com.example.demo.service.impl;

import com.example.demo.dao.InterStationRepository;
import com.example.demo.dao.StationRepository;
import com.example.demo.entity.InterStation;
import com.example.demo.entity.Station;
import com.example.demo.service.InterSationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class InterSationServiceImpl implements InterSationService {


    private final Logger logger = LoggerFactory.getLogger(InterSationServiceImpl.class);

    @Autowired
    private InterStationRepository repository;

    @Autowired
    private StationRepository stationRepository;

    @Override
    public void associateOthers(String name) {
        Station station = this.stationRepository.findByName(name);

        Set<Station> allTheOthers = this.stationRepository.findAllTheOthers(station);
        for (Station deputyStation : allTheOthers) {
            InterStation interStation = null;
            interStation = this.repository.findInterStationByPriStationAndDeputyStation(station,deputyStation);
            if(interStation == null) {
                interStation = setNewInterStation(station, deputyStation);
                this.repository.saveAndFlush(interStation);
            }
        }
    }

    @Override
    public void associateOthers() {
        List<Station> stations = this.stationRepository.findAll();
        for (Station station : stations) {
            Set<Station> allTheOthers = this.stationRepository.findAllTheOthers(station);
            for (Station deputyStation : allTheOthers) {
                try {
                    InterStation interStation = null;
                    interStation = this.repository.findInterStationByPriStationAndDeputyStation(station,deputyStation);
                    if(interStation == null) {
                        interStation = setNewInterStation(station, deputyStation);
                        this.repository.saveAndFlush(interStation);
                    }
                }catch (Exception e){
                    logger.error("",e);
                }
            }
        }

    }

    @Override
    public InterStation findInterStationByPriStationNameAndDeputyStationName(String priStation, String deputyStation) {
        return this.repository.findInterStationByPriStationNameAndDeputyStationName(priStation,deputyStation);
    }

    private InterStation setNewInterStation(Station station, Station deputyStation) {
        InterStation interStation = new InterStation();
        interStation.setCreateDate(new Date());
        interStation.setPriStation(station);
        interStation.setDeputyStation(deputyStation);
        return interStation;
    }
}
