package com.tapwisdom.core.misc.companypush;

import com.googlecode.jcsv.annotations.MapToColumn;

public class CompanyData {
    
    @MapToColumn(column = 1)
    private String name;

    @MapToColumn(column = 2)
    private String location;

    @MapToColumn(column = 4)
    private String industry;

    @MapToColumn(column = 5)
    private String logo;
    
    @MapToColumn(column = 7)
    private String website;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}
