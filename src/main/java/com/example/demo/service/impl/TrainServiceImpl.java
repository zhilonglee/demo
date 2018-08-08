package com.example.demo.service.impl;

import com.example.demo.dao.TrainRepository;
import com.example.demo.entity.Train;
import com.example.demo.service.TrainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrainServiceImpl implements TrainService {

    @Autowired
    private TrainRepository trainRepository;

    @Override
    public void save(Train train) {
        trainRepository.save(train);
    }

    @Override
    public Train findByName(String name) {
        return trainRepository.findByName(name);
    }
}
