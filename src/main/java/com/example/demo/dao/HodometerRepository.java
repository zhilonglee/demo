package com.example.demo.dao;

import com.example.demo.entity.Hodometer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HodometerRepository extends JpaRepository<Hodometer,Long> {
    Hodometer findByTrainNameAndNextHodometerIsNull(String trainName);
    //@Query("SELECT a.train.name from Hodometer a where a.station.name = :departName or a.station.name = :destName GROUP BY a.train.id Having count(a) >= 2 ")
    //List<String> findTrainViaHodometersByDepAdnDest(@Param("departName") String departName, @Param("destName")String destName);

    Long countHodometerByTrainName(String trainName);

    @Query("SELECT a.train.name from Hodometer a where a.interStation.priStation.name = :departName or a.interStation.deputyStation.name = :destName GROUP BY a.train.id Having count(a) >= 2 ")
    List<String> findTrainViaHodometersByDepAdnDest(@Param("departName") String departName, @Param("destName")String destName);

    @Query("SELECT a.train.name from Hodometer a where a.interStation.priStation.name = :departName AND a.interStation.deputyStation.name = :destName")
    List<String> findDirectTrainViaHodometersByDepAdnDest(@Param("departName") String departName, @Param("destName")String destName);

    @Query("SELECT a FROM Hodometer as a LEFT JOIN FETCH a.train LEFT JOIN FETCH a.interStation as b LEFT JOIN FETCH b.priStation LEFT JOIN FETCH b.deputyStation where a.train.name = :trainName AND (a.interStation.priStation.name = :departName or a.interStation.deputyStation.name = :destName)")
    List<Hodometer> findHodometersByTrainNameAndInterStationPriStationNameOrDeputyStationName(@Param("trainName") String trainName,@Param("departName") String departName, @Param("destName")String destName);


}
