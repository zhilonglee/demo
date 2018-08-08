package com.example.demo.web;

import com.example.demo.entity.Province;
import com.example.demo.entity.Station;
import com.example.demo.service.InterSationService;
import com.example.demo.service.ProvinceService;
import com.example.demo.service.StationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/railway")
public class RailWayController {

    private final StationService stationService;

    private final ProvinceService provinceService;

    private final InterSationService interSationService;

    public RailWayController(StationService stationService, ProvinceService provinceService, InterSationService interSationService) {
        this.stationService = stationService;
        this.provinceService = provinceService;
        this.interSationService = interSationService;
    }

    @RequestMapping(method = RequestMethod.POST,value = "/station/save")
    @ResponseBody
    public String  saveStation(@ModelAttribute("station")Station station){
        stationService.save(station);
        return "New Station has been saved ! ";
    }

    @RequestMapping(method = RequestMethod.POST,value = "/province/save")
    public String saveProvince(@ModelAttribute("province")Province province){
        provinceService.save(province);
        return "New Province has been saved ! ";
    }

    @RequestMapping(method = RequestMethod.GET,value = "/station/associate/{name}")
    public String stationAssociate(@PathVariable("name") String name){
        stationService.associateOthers(name);
        return name + " station has been associate with others ! ";
    }

    @RequestMapping(method = RequestMethod.GET,value = "/station/associate")
    public String stationAssociateAll(){
        stationService.associateOthers();
        return "Stations have been associate with each others ! ";
    }

    @RequestMapping(method = RequestMethod.GET,value = "/inter/station/associate/{name}")
    public String InterstationAssociate(@PathVariable("name") String name){
        interSationService.associateOthers(name);
        return name + " station has been associate with others ! ";
    }

    @RequestMapping(method = RequestMethod.GET,value = "/inter/station/associate")
    public String InterstationAssociateAll(){
        interSationService.associateOthers();
        return "Stations have been associate with each others ! ";
    }

    @GetMapping(value = "/province/list")
    @ResponseBody
    public List<Province> findAllProvinces(){
        return provinceService.findAll();
    }

    @GetMapping(value = "/station/{provinceName}")
    @ResponseBody
    public List<Station> findStation(@PathVariable("provinceName")String provinceName){
        List<Station> stations = stationService.findStationByProvinceName(provinceName);
        return stations;
    }
}