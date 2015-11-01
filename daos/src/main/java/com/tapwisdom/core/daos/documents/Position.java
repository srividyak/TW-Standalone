package com.tapwisdom.core.daos.documents;

import java.io.Serializable;

/**
 * Created by srividyak on 25/07/15.
 */
public class Position implements Serializable {
    
    private Company company;
    private Boolean isCurrent = false;
    private LinkedInDate startDate;
    private LinkedInDate endDate;
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Boolean getIsCurrent() {
        return isCurrent;
    }

    public void setIsCurrent(Boolean isCurrent) {
        this.isCurrent = isCurrent;
    }

    public LinkedInDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LinkedInDate startDate) {
        this.startDate = startDate;
    }

    public LinkedInDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LinkedInDate endDate) {
        this.endDate = endDate;
    }
}
