package com.example.demo.web;

import com.example.demo.entity.Province;
import com.example.demo.entity.Station;
import com.example.demo.service.InterSationService;
import com.example.demo.service.ProvinceService;
import com.example.demo.service.StationService;
import com.example.demo.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/railway")
public class RailWayController {

    private final StationService stationService;

    private final ProvinceService provinceService;

    private final InterSationService interSationService;

    @Autowired
    private RedisUtils redisUtils;

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
        List<Province> provincelist = null;
        provincelist = (List<Province>) redisUtils.getObj("Province:provincelist");
        if (null == provincelist || provincelist.isEmpty()){
            provincelist =  provinceService.findAll();
/*            if(null != provincelist && !provincelist.isEmpty()){
                redisUtils.setObj("Province:provincelist",provincelist);
                redisUtils.expire("Province:provincelist",5,RedisUtils.TIME_TO_MINUTES);
            }*/
        }
        return provincelist;
    }

    @GetMapping(value = "/station/{provinceName}")
    @ResponseBody
    public List<Station> findStation(@PathVariable("provinceName")String provinceName){
        List<Station> stations = stationService.findStationByProvinceName(provinceName);
        return stations;
    }
}
