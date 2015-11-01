package com.tapwisdom.core.daos.documents;

import java.io.Serializable;

public class Location implements Serializable {

    public static class Country implements Serializable {
        private String code;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }
    
    private String name;
    private Country country;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }
    
    @Override
    public boolean equals(Object o) {
        Location l = (Location) o;
        return name.equals(l.getName());
    }
    
    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
