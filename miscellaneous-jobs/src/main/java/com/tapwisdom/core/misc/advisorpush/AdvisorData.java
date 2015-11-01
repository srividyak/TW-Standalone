package com.tapwisdom.core.misc.advisorpush;

import com.googlecode.jcsv.annotations.MapToColumn;

public class AdvisorData {

    @MapToColumn(column = 1)
    private String name;
    
    @MapToColumn(column = 2)
    private String email;

    @MapToColumn(column = 2)
    private String phoneNumber;

    @MapToColumn(column = 4)
    private String company;

    @MapToColumn(column = 5)
    private String headline;

    @MapToColumn(column = 6)
    private String linkedInPublicProfileUrl;

    @MapToColumn(column = 7)
    private String linkedInPictureUrl;

    @MapToColumn(column = 8)
    private String location;

    @MapToColumn(column = 9)
    private String industry;

    @MapToColumn(column = 10)
    private String designation;

    @MapToColumn(column = 11)
    private String summary;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getLinkedInPublicProfileUrl() {
        return linkedInPublicProfileUrl;
    }

    public void setLinkedInPublicProfileUrl(String linkedInPublicProfileUrl) {
        this.linkedInPublicProfileUrl = linkedInPublicProfileUrl;
    }

    public String getLinkedInPictureUrl() {
        return linkedInPictureUrl;
    }

    public void setLinkedInPictureUrl(String linkedInPictureUrl) {
        this.linkedInPictureUrl = linkedInPictureUrl;
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

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
