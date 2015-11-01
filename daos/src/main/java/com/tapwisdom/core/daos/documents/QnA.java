package com.tapwisdom.core.daos.documents;

import org.springframework.data.annotation.Transient;

public class QnA {
    
    private Question question;
    private Object answer;
    private Integer questionId;
    private Integer upvotes = 0;
    private String comment;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Transient
    private Boolean isUpvoted = false;
    
    @Transient
    private Boolean isDownvoted = false;

    public Boolean getIsUpvoted() {
        return isUpvoted;
    }

    public void setIsUpvoted(Boolean isUpvoted) {
        this.isUpvoted = isUpvoted;
    }

    public Boolean getIsDownvoted() {
        return isDownvoted;
    }

    public void setIsDownvoted(Boolean isDownvoted) {
        this.isDownvoted = isDownvoted;
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public Integer getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(Integer upvotes) {
        this.upvotes = upvotes;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Object getAnswer() {
        return answer;
    }

    public void setAnswer(Object answer) {
        this.answer = answer;
    }
}
