package com.example.demo.service;


import com.example.demo.entity.Province;

import java.util.List;

public interface ProvinceService {
    public void save(Province province);
    public List<Province> findAll();
}
