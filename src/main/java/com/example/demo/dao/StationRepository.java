package com.example.demo.dao;

import com.example.demo.entity.CompositeId;
import com.example.demo.entity.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface StationRepository extends JpaRepository<Station,CompositeId> {
    Station findByName(String name);

    @Query("select s from Station s where s <> :station")
    Set<Station> findAllTheOthers(@Param("station") Station station);

    List<Station> findStationsByCompositeId_Province_Name(String provinceName);
}
