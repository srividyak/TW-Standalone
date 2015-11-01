package com.tapwisdom.core.daos.documents;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "company")
public class Company extends BaseEntity {
    
    @Indexed
    private String name;
    
    @Indexed
    private String industry;
    private Long numEmployees;
    private String website;
    private String logoUrl;
    private Long numQuestionsAsked;
    private Long numQuestionsAnswered;

    public Company() {
        numQuestionsAnswered = 0L;
        numQuestionsAsked = 0L;
    }

    @Indexed
    private Location[] locations;

    public Location[] getLocations() {
        return locations;
    }

    public void setLocations(Location[] locations) {
        this.locations = locations;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public Long getNumEmployees() {
        return numEmployees;
    }

    public void setNumEmployees(Long numEmployees) {
        this.numEmployees = numEmployees;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Long getNumQuestionsAnswered() {
        return numQuestionsAnswered;
    }

    public void setNumQuestionsAnswered(Long numQuestionsAnswered) {
        this.numQuestionsAnswered = numQuestionsAnswered;
    }

    public Long getNumQuestionsAsked() {
        return numQuestionsAsked;
    }

    public void setNumQuestionsAsked(Long numQuestionsAsked) {
        this.numQuestionsAsked = numQuestionsAsked;
    }
}
