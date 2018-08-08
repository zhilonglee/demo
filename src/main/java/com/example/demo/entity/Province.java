package com.example.demo.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "tbl_province")
public class Province implements Serializable {

    @Id
    @GenericGenerator(name = "province-uuid", strategy = "uuid")
    @GeneratedValue(generator = "province-uuid")
    private String id;

    @NotNull
    @Column(columnDefinition = "varchar(50) comment 'province name' ")
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Province province = (Province) o;
        return Objects.equals(id, province.id) &&
                Objects.equals(name, province.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Province{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
