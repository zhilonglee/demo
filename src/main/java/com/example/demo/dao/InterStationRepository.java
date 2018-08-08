package com.example.demo.dao;

import com.example.demo.entity.InterStation;
import com.example.demo.entity.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface InterStationRepository extends JpaRepository<InterStation,Long> {

    //@Query("select s from InterStation s where s.priStation = :priStation AND s.deputyStation = :deputyStation")
    InterStation findInterStationByPriStationAndDeputyStation(@Param("priStation") Station priStation,@Param("deputyStation") Station deputyStation);

    InterStation findInterStationByPriStationNameAndDeputyStationName(@Param("priStation") String priStation,@Param("deputyStation") String deputyStation);
}
