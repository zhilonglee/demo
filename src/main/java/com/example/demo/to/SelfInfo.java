package com.example.demo.to;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class SelfInfo implements Serializable {

    private Location location;

    public static class Location {

        private String city;

        private String province = "";

        private String street = "";

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
