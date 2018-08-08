package com.example.demo.dao;

import com.example.demo.entity.Train;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainRepository extends JpaRepository<Train,Long> {
    public Train findByName(String Name);
}
