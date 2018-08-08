package com.example.demo.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "tbl_train")
@DynamicUpdate
@DynamicInsert
public class Train implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "varchar(50) comment 'The train name is unique.' ",nullable = false,unique = true)
    private String name;

    @Column(columnDefinition = "datetime")
    @DateTimeFormat(pattern = "yyyy MM dd HH:mm:ss")
    private Date createdDate;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "train")
    private List<Hodometer> hodometers;

    @Column(columnDefinition = "varchar(255) comment 'The train status info' ")
    private String info;

    @Column(columnDefinition = "varchar(5) comment 'Train type : G ,D, K, Z' ")
    private String type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public List<Hodometer> getHodometers() {
        return hodometers;
    }

    public void setHodometers(List<Hodometer> hodometers) {
        this.hodometers = hodometers;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Train() {
        this.createdDate = new Date();
    }
}
