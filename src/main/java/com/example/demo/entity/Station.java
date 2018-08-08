package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.Constraint;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 */
@Table(name = "tbl_station",uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
@Entity
//@IdClass(CompositeId.class)
@DynamicUpdate
@DynamicInsert
//@JsonIgnoreProperties({ "stations" })
public class Station implements Serializable {

/*    @Id
    @Column(columnDefinition = "varchar(50) comment 'Composite private key ID.' ")
    private String provinceId;

    @Id
    @Column(columnDefinition = "varchar(50) comment 'Composite private key code.' ")
    private String cityId;*/

    @Id
    @EmbeddedId
    @JsonIgnore
    private CompositeId compositeId;

    @Column(columnDefinition = "varchar(50) comment 'station name' ")
    private String name;

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinTable(name = "tbl_inter_city_transport", joinColumns = {@JoinColumn(name = "p_city_id",referencedColumnName = "cityId"),
            @JoinColumn(name = "p_province_id",referencedColumnName = "province_id")},
            inverseJoinColumns = {@JoinColumn(referencedColumnName="cityId",name = "d_city_id"),
            @JoinColumn(referencedColumnName = "province_id",name = "d_province_id")})
    private Set<Station> stations = new HashSet<>();

  /*  public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;z
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }
*/
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public CompositeId getCompositeId() {
        return compositeId;
    }

    public void setCompositeId(CompositeId compositeId) {
        this.compositeId = compositeId;
    }

    public Set<Station> getStations() {
        return stations;
    }

    public void setStations(Set<Station> stations) {
        this.stations = stations;
    }

}
