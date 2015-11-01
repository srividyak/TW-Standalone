package com.tapwisdom.core.es;

/**
 * Created by srividyak on 22/07/15.
 */
public class UserSearchCriteria {
    
    private String companyName;
    private String location;
    private String designation;
    private String industry;

    public String getIndustry() {
        return industry;
    }

    public UserSearchCriteria setIndustry(String industry) {
        this.industry = industry;
        return this;
    }

    public String getDesignation() {
        return designation;
    }

    public UserSearchCriteria setDesignation(String designation) {
        this.designation = designation;
        return this;
    }

    public String getCompanyName() {
        return companyName;
    }

    public UserSearchCriteria setCompanyName(String companyName) {
        this.companyName = companyName;
        return this;
    }

    public String getLocation() {
        return location;
    }

    public UserSearchCriteria setLocation(String location) {
        this.location = location;
        return this;
    }

}
