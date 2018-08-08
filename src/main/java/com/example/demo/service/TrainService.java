package com.example.demo.service;

import com.example.demo.entity.Train;

public interface TrainService {
    public void save(Train train);

    public Train findByName(String name);
}
