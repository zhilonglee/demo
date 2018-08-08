package com.example.demo.service.impl;

import com.example.demo.dao.StationRepository;
import com.example.demo.entity.Station;
import com.example.demo.service.StationService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class StationServiceImpl implements StationService {

    private final StationRepository repository;

    public StationServiceImpl(StationRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(Station station) {
        repository.save(station);
    }

    @Override
    public List<Station> findAll() {
        return this.repository.findAll();
    }

    @Override
    public Station findByName(String name) {
        return this.repository.findByName(name);
    }

    @Override
    public void associateOthers(String name) {
        Station station = this.repository.findByName(name);
            Set<Station> allTheOthers = this.repository.findAllTheOthers(station);
            station.setStations(allTheOthers);
            this.repository.saveAndFlush(station);
            System.out.println(station.getName()+ " Others size : "+allTheOthers.size());


    }

    @Override
    public void associateOthers() {
        List<Station> all = this.repository.findAll();
        for (Station station : all) {
            Set<Station> allTheOthers = this.repository.findAllTheOthers(station);
            station.setStations(allTheOthers);
            this.repository.saveAndFlush(station);
            System.out.println(station.getName()+ " Others size : "+allTheOthers.size());
        }

    }

    @Override
    public List<Station> findStationByProvinceName(String provinceName) {
        return this.repository.findStationsByCompositeId_Province_Name(provinceName);
    }
}
