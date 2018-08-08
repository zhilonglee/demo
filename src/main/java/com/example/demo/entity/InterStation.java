package com.example.demo.entity;

import org.hibernate.annotations.Cascade;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "tbl_station_station"
        ,uniqueConstraints = {@UniqueConstraint(columnNames={"p_city_id", "p_province_id","d_city_id","d_province_id"})}
        )
public class InterStation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "int(11) comment 'station to staion table primary key ID' ")
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy MM dd HH:mm:ss")
    @Column(columnDefinition = "datetime comment 'station transport create date'")
    private Date createDate;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumns({@JoinColumn(name = "p_city_id",referencedColumnName = "cityId"),
            @JoinColumn(name = "p_province_id",referencedColumnName = "province_id")})
    private Station priStation;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumns({@JoinColumn(name = "d_city_id",referencedColumnName = "cityId"),
            @JoinColumn(name = "d_province_id",referencedColumnName = "province_id")})
    private Station deputyStation;

    @Column(columnDefinition = "int(11) comment 'popularity degree' default 0")
    private Long popularity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Station getPriStation() {
        return priStation;
    }

    public void setPriStation(Station priStation) {
        this.priStation = priStation;
    }

    public Station getDeputyStation() {
        return deputyStation;
    }

    public void setDeputyStation(Station deputyStation) {
        this.deputyStation = deputyStation;
    }

    public Long getPopularity() {
        return popularity;
    }

    public void setPopularity(Long popularity) {
        this.popularity = popularity;
    }
}
