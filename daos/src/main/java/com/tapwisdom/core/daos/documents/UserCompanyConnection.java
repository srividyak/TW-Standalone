package com.tapwisdom.core.daos.documents;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by srividyak on 09/07/15.
 */
@Document(collection = "user_company_connection")
public class UserCompanyConnection extends BaseEntity {

    @Indexed
    private String userId;
    
    @Indexed
    private String companyId;

    private Boolean inWatchList = false;
    private UserCompanyQuestions questionsAsked;
    private UserCompanyQuestions questionsAnswered;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public Boolean getInWatchList() {
        return inWatchList;
    }

    public void setInWatchList(Boolean inWatchList) {
        this.inWatchList = inWatchList;
    }

    public UserCompanyQuestions getQuestionsAsked() {
        return questionsAsked;
    }

    public void setQuestionsAsked(UserCompanyQuestions questionsAsked) {
        this.questionsAsked = questionsAsked;
    }

    public UserCompanyQuestions getQuestionsAnswered() {
        return questionsAnswered;
    }

    public void setQuestionsAnswered(UserCompanyQuestions questionsAnswered) {
        this.questionsAnswered = questionsAnswered;
    }
}
