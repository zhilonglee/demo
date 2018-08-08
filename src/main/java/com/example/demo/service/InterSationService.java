package com.example.demo.service;

import com.example.demo.entity.InterStation;
import com.example.demo.entity.Station;
import org.springframework.data.repository.query.Param;

public interface InterSationService {
    public void associateOthers(String name);
    public void associateOthers();
    public InterStation findInterStationByPriStationNameAndDeputyStationName(String priStation, String deputyStation);
}
