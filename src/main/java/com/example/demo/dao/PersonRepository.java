package com.example.demo.dao;

import java.util.List;

import com.example.demo.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface PersonRepository extends JpaRepository<Person, Long> ,JpaSpecificationExecutor<Person>  {
	
    List<Person> findByAddress(String address);

    Person findByNameAndAddress(String name, String address);

    @Query("select p from Person p where p.name=:name and p.address=:address")
    Person withNameAndAddressQuery(@Param("name") String name, @Param("address") String address);
    
    @Modifying
    @Query("delete Person p where p.name = :name")
    int deleteByName(@Param("name") String name);
    
    @Modifying(clearAutomatically = true)
    @Query("update Person p set p.age = :age  where p.name = :name")
    int updateAgeByName(@Param("name") String name, @Param("age") Integer age);
    
    Person findByName(String name);


}
