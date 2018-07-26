package com.example.demo.service.impl;

import com.example.demo.dao.PersonRepository;
import com.example.demo.entity.Person;
import com.example.demo.service.PersonService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class PersonServiceImpl implements PersonService {

    @Autowired
    PersonRepository repository;


    public List<Person> findAll() {

        return repository.findAll();
    }

    public Page<Person> findAll(Pageable pageable) {

        return repository.findAll(pageable);
    }

    public Person findOne(Serializable id) {

        return repository.findById((Long)id).get();
    }

    @Transactional
    public void save(Person t) {
        repository.save(t);

    }

    @Transactional
    public void delete(Person t) {
        repository.delete(t);

    }

    public List<Person> findByAddress(String address) {

        return repository.findByAddress(address);

    }

    public Person findByNameAndAddress(String name, String address) {

        return repository.findByNameAndAddress(name, address);
    }

    public Person withNameAndAddressQuery(String name, String address) {

        return repository.withNameAndAddressQuery(name, address);

    }

    @Transactional
    public int deleteByName(String name) {

        return repository.deleteByName(name);
    }

    @Transactional
    public int updateAgeByName(String name, Integer age) {

        return repository.updateAgeByName(name, age);
    }

    @Transactional
    public void update(Person person) throws Exception {
        if(person.getId() == null){
            throw new Exception("update operation must contain id.");
        }
        repository.save(person);

    }

    public Page<Person> findAll(final Person person, Pageable pageable) throws Exception {

        if(person == null){
            throw new Exception("Person object can not be null !");
        }
        return repository.findAll(new Specification<Person>() {

            @Override
            public Predicate toPredicate(Root<Person> root, CriteriaQuery<?> query,
                                         CriteriaBuilder cb) {

                List<Predicate> predicateList = new ArrayList<Predicate>();

                if(StringUtils.isNotEmpty(person.getName())){
                    Predicate predicateByName = cb.like(root.get("name").as(String.class), "%" + person.getName() + "%");
                    predicateList.add(predicateByName);
                }
                if( null != person.getAge()){
                    Predicate predicateByAge = cb.equal(root.get("age").as(Integer.class), person.getAge());
                    predicateList.add(predicateByAge);
                }
                if(StringUtils.isNotEmpty(person.getAddress())){
                    Predicate predicateByAddress = cb.like(root.get("address").as(String.class), "%" + person.getAddress() + "%");
                    predicateList.add(predicateByAddress);
                }
                Date birthDate = person.getBirthDay();
                if(null != birthDate){
                    Calendar calendar = DateUtils.toCalendar(birthDate);
                    calendar.set(Calendar.HOUR_OF_DAY, 0);
                    calendar.set(Calendar.MINUTE, 0);
                    calendar.set(Calendar.SECOND, 0);
                    Date beginDate = calendar.getTime();

                    calendar.set(Calendar.HOUR_OF_DAY, 23);
                    calendar.set(Calendar.MINUTE, 59);
                    calendar.set(Calendar.SECOND, 59);
                    Date endDate = calendar.getTime();

                    Predicate predicateByBirthDate = cb.between(root.get("birthDay").as(Date.class), beginDate, endDate);
                    predicateList.add(predicateByBirthDate);
                }
                Predicate[] predicates = new Predicate[predicateList.size()];
                predicateList.toArray(predicates);
                return cb.and(predicates);
            }
        } ,pageable);
    }

    public Person findByName(String name) {
        return repository.findByName(name);
    }

}
