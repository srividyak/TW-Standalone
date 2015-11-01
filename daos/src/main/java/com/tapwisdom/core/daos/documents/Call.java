package com.tapwisdom.core.daos.documents;


import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Map;

/**
 * Created by srividyak on 03/05/15.
 */
@Document(collection = "call")
public class Call extends BaseEntity {
    
    private String giverId;
    private String seekerId;
    private String conferenceId;
    private Integer duration;
    private Long startTime;
    private Long endTime;
    private Map<String, Double> ratings;
    private CallStatus status = CallStatus.waiting;

    public CallStatus getStatus() {
        return status;
    }

    public void setStatus(CallStatus status) {
        this.status = status;
    }

    public String getGiverId() {
        return giverId;
    }

    public void setGiverId(String giverId) {
        this.giverId = giverId;
    }

    public String getSeekerId() {
        return seekerId;
    }

    public void setSeekerId(String seekerId) {
        this.seekerId = seekerId;
    }

    public String getConferenceId() {
        return conferenceId;
    }

    public void setConferenceId(String conferenceId) {
        this.conferenceId = conferenceId;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Map<String, Double> getRatings() {
        return ratings;
    }

    public void setRatings(Map<String, Double> ratings) {
        this.ratings = ratings;
    }
}
