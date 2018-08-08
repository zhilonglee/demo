package com.example.demo.service.impl;

import com.example.demo.dao.ProvinceRepository;
import com.example.demo.entity.Province;
import com.example.demo.service.ProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProvinceServiceImpl implements ProvinceService {

    @Autowired
    private ProvinceRepository repository;

    @Override
    public void save(Province province) {
        repository.save(province);
    }

    @Override
    public List<Province> findAll() {
        return repository.findAll();
    }
}
