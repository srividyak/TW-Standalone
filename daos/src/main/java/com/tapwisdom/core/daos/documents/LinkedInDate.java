package com.tapwisdom.core.daos.documents;

import java.io.Serializable;

/**
 * Created by srividyak on 25/07/15.
 */
public class LinkedInDate implements Serializable {
    
    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    private String month;
    private String year;
    
}
