package com.example.demo.web;

import com.example.demo.entity.Person;
import com.example.demo.service.PersonService;
import com.example.demo.to.ToResult;
import com.example.demo.to.eum.ServerStatus;
import com.example.demo.utils.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.query.Param;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/person")
public class PageController {

    private final Logger logger = LoggerFactory.getLogger(PageController.class);

    @Autowired
    PersonService personService;

    @CrossOrigin
    @RequestMapping(value = "/all", method = {RequestMethod.GET})
    public List<Person> all(@Param(value = "page") Integer page, @Param(value = "size") Integer size){
        if(page == null && size == null){
            return personService.findAll();
        }else{
            return personService.findAll(new PageRequest(page, size)).getContent();
        }
    }

    @RequestMapping(value = "/save", method = {RequestMethod.POST} )
    public ToResult save(@RequestBody Person person){
        ToResult result = null;
        try {
            logger.info("Access person save method .");
            personService.save(person);
            result = ToResult.buildToResult().addDetail(person.getName()+" has registered successfully.");
        } catch (Exception e) {
            logger.error("",e);
            if(e.getMessage().contains("constraint")){
                result = ToResult.buildBadRequestToResult().addDetail(ToResult.CONSTRAINTERROR);
            }
        }

        return result;
    }

    @RequestMapping(value = "/delete/{name}", method = {RequestMethod.DELETE} )
    public ToResult deleteByName(@PathVariable("name") String name){
        ToResult result = null;
        try {
            int rows = personService.deleteByName(name);
            if(rows > 0){
                result = ToResult.buildToResult().addDetail(name+" has terminated successfully.");
            }else{
                result = ToResult.buildToResult(ToResult.BEDREQUEST, ServerStatus.NOCONTENT).addDetail("No named" + name +"user.");
            }
        } catch (Exception e) {
            logger.error("",e);
            result = ToResult.buildBadRequestToResult();
        }
        return result;

    }

    @RequestMapping(value = "/patch/{name}", method = {RequestMethod.PATCH} )
    public ToResult updateAgeByName(@PathVariable("name") String name,@RequestBody Map<String, Integer> param){
        ToResult toResult = null;
        try {
            int rows = personService.updateAgeByName(name, param.get("age"));
            if(rows > 0){
                toResult = ToResult.buildToResult().addDetail(name+"'s age has updated successfully.").addData(rows);
            }else{
                toResult = ToResult.buildToResult(ToResult.BEDREQUEST, ServerStatus.NOCONTENT).addDetail("No named" + name +"user.");
            }
        } catch (Exception e) {
            logger.error("",e);
            toResult = ToResult.buildBadRequestToResult();
        }
        return toResult;
    }

    @GetMapping(value="/search",produces = MediaType.APPLICATION_JSON_VALUE)
    public ToResult findAllByConditions(Person person,@RequestParam(value = "page",defaultValue = "0") Integer page,@RequestParam(value = "size",defaultValue = "10") Integer size,BindingResult bindingResult) {
        ToResult toResult = null;
        if(bindingResult.hasErrors()){
            toResult = ToResult.buildBadRequestToResult().addDetail(bindingResult.getAllErrors().toString());
        }
        try {
            Page<Person> pagePerson = personService.findAll(person, new PageRequest(page, size));
            if(pagePerson != null && pagePerson.getSize() > 0){
                toResult = ToResult.buildToResult().addData(pagePerson);
            }else{
                toResult = ToResult.buildToResult(ToResult.BEDREQUEST, ServerStatus.NOCONTENT).addDetail("No records By your search criteria.");
            }
        } catch (Exception e) {
            logger.error("",e);
            toResult = ToResult.buildBadRequestToResult();
        }

        return toResult;
    }

    @RequestMapping(value = "/update/{id}" ,method = {RequestMethod.PUT},produces = MediaType.APPLICATION_JSON_VALUE)
    public ToResult updatePerson(Person person , @PathVariable Long id, HttpServletRequest request, BindingResult bindingResult){

        ToResult toResult = null;
        if(bindingResult.hasErrors()){
            toResult = ToResult.buildBadRequestToResult().addDetail(bindingResult.getAllErrors().toString());
        }
        try {
            Person personInDB = personService.findOne(id);
            EntityUtils.copyPropertiesIgnoreNull(person, personInDB);
            EntityUtils.getMethodName(personInDB);
            personService.update(personInDB);
            toResult = ToResult.buildToResult().addDetail(person.getName() + " has updated successfully!");
        } catch (Exception e) {
            logger.error("",e);
            toResult = ToResult.buildBadRequestToResult();
        }

        return toResult;
    }

}
