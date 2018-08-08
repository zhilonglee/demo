package com.example.demo.service;

import com.example.demo.entity.Station;

import java.util.List;

public interface StationService {
    public void save(Station station);
    public List<Station> findAll();
    public Station findByName(String name);
    public void associateOthers(String name);
    public void associateOthers();
    public List<Station> findStationByProvinceName(String provinceName);
}
