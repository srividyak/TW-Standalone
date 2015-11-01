package com.tapwisdom.core.daos.documents;

/**
 * Created by srividyak on 09/07/15.
 */
public class UserCompanyQuestions {
    
    private Long numQuestions;
    private Long firstQuestionTimestamp;
    private Long lastQuestionTimestamp;

    public Long getNumQuestions() {
        return numQuestions;
    }

    public void setNumQuestions(Long numQuestions) {
        this.numQuestions = numQuestions;
    }

    public Long getFirstQuestionTimestamp() {
        return firstQuestionTimestamp;
    }

    public void setFirstQuestionTimestamp(Long firstQuestionTimestamp) {
        this.firstQuestionTimestamp = firstQuestionTimestamp;
    }

    public Long getLastQuestionTimestamp() {
        return lastQuestionTimestamp;
    }

    public void setLastQuestionTimestamp(Long lastQuestionTimestamp) {
        this.lastQuestionTimestamp = lastQuestionTimestamp;
    }
}
