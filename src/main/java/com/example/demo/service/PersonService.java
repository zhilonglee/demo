package com.example.demo.service;

import com.example.demo.entity.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.List;

public interface PersonService extends  BaseService<Person>{

    Page<Person> findAll(Person t, Pageable pageable)  throws Exception ;

    List<Person> findByAddress(String address);

    Person findByName(String name);

    Person findByNameAndAddress(String name, String address);

    Person withNameAndAddressQuery(String name, String address);

    int deleteByName(String name);

    int updateAgeByName(String name,Integer age);

    void update(Person person )  throws Exception;

}
