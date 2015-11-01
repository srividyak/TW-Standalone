package com.tapwisdom.core.daos.documents;

/**
 * Created by sachin on 7/27/15.
 */
public class BaseQnASession extends BaseEntity {
    
    protected String seekerId;
    protected String advisorId;
    protected Boolean isAnswered = false;
    protected String companyId;
    protected String designation;
    protected Long questionSubmittedTimestamp;
    protected Long answerSubmittedTimestamp;

    public String getSeekerId() {
        return seekerId;
    }

    public void setSeekerId(String seekerId) {
        this.seekerId = seekerId;
    }

    public String getAdvisorId() {
        return advisorId;
    }

    public void setAdvisorId(String advisorId) {
        this.advisorId = advisorId;
    }

    public Boolean getIsAnswered() {
        return isAnswered;
    }

    public void setIsAnswered(Boolean isAnswered) {
        this.isAnswered = isAnswered;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public Long getQuestionSubmittedTimestamp() {
        return questionSubmittedTimestamp;
    }

    public void setQuestionSubmittedTimestamp(Long questionSubmittedTimestamp) {
        this.questionSubmittedTimestamp = questionSubmittedTimestamp;
    }

    public Long getAnswerSubmittedTimestamp() {
        return answerSubmittedTimestamp;
    }

    public void setAnswerSubmittedTimestamp(Long answerSubmittedTimestamp) {
        this.answerSubmittedTimestamp = answerSubmittedTimestamp;
    }
}
