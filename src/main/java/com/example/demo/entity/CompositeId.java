package com.example.demo.entity;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class CompositeId implements Serializable {

/*    @NotNull
    @Size(max = 50)
    @Column(columnDefinition = "varchar(50) comment 'Composite private key provinceId.' ")
    @ManyToOne(targetEntity = Province.class)
    @JoinColumn(name="province_id",  nullable = false)
    private String provinceId;*/

    @ManyToOne(targetEntity = Province.class,cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name="province_id",  nullable = false)
    private Province province;


    @NotNull
    @Size(max = 50)
    @GenericGenerator(name = "city-uuid", strategy = "uuid")
    @GeneratedValue(generator = "city-uuid")
    @Column(columnDefinition = "varchar(50) comment 'Composite private key cityId.' ")
    private String cityId;

/*    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }*/

    public Province getProvince() {
        return province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

  /*  @Override
    public int hashCode() {
        final int prime = 31;
        int result = 7;
        result = prime * result + ((this.province == null) ? 0 : this.province.hashCode());
        result = prime * result
                + ((this.cityId == null) ? 0 : this.cityId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj){
            return true;
        }
        if(obj == null){
            return false;
        }
        if(this.getClass() != obj.getClass()){
            return  false;
        }else{
            if( ((CompositeId)obj).getProvince() != null){

                if( (((CompositeId)obj).getProvince().getId() == this.getProvince().getId() ) &&
                (((CompositeId) obj).getCityId() == this.getCityId()) ){
                    return true;
                }
            }
        }
        return  false;
    }*/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompositeId that = (CompositeId) o;
        return Objects.equals(province, that.province) &&
                Objects.equals(cityId, that.cityId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(province, cityId);
    }

    public CompositeId() {
        this.cityId = UUID.randomUUID().toString().replace("-","");
    }

    @Override
    public String toString() {
        return "CompositeId{" +
                "province=" + province +
                ", cityId='" + cityId + '\'' +
                '}';
    }
}
