package com.tapwisdom.core.daos.documents;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "qna_session")
public class QnASession extends BaseEntity implements Cloneable {

    @Indexed
    protected String seekerId;
    
    @Indexed
    protected String advisorId;
    protected Boolean isAnswered = false;
    
    @Indexed
    protected String companyId;
    
    @Indexed
    protected String designation;
    
    @Indexed
    protected String companyLocation;
    protected Long questionSubmittedTimestamp;

    public String getCompanyLocation() {
        return companyLocation;
    }

    public void setCompanyLocation(String companyLocation) {
        this.companyLocation = companyLocation;
    }

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

    private List<QnA> qnAList;

    public List<QnA> getQnAList() {
        return qnAList;
    }

    public void setQnAList(List<QnA> qnAList) {
        this.qnAList = qnAList;
    }
    
    public Object clone() {
        QnASession qnASession = new QnASession();
        qnASession.setAdvisorId(advisorId);
        qnASession.setIsAnswered(isAnswered);
        qnASession.setAnswerSubmittedTimestamp(answerSubmittedTimestamp);
        qnASession.setCompanyId(companyId);
        qnASession.setDesignation(designation);
        qnASession.setQnAList(qnAList);
        qnASession.setQuestionSubmittedTimestamp(questionSubmittedTimestamp);
        qnASession.setSeekerId(seekerId);
        qnASession.setId(getId());
        qnASession.setCompanyLocation(companyLocation);
        qnASession.setCreatedAt(getCreatedAt());
        qnASession.setUpdatedAt(getUpdatedAt());
        return qnASession;
    }
    
    public List<QnAEntity> buildQnAEntities() {
        List<QnAEntity> qnAEntities = new ArrayList<QnAEntity>();
        if (qnAList != null) {
            int index = 0;
            for (QnA qnA : qnAList) {
                QnAEntity qnAEntity = buildQnAEntity(index++);
                qnAEntities.add(qnAEntity);
            }
        }
        return qnAEntities;
    }
    
    public QnAEntity buildQnAEntity(int index) {
        if (qnAList != null && index < qnAList.size()) {
            QnA qnA = qnAList.get(index);
            QnAEntity qnAEntity = new QnAEntity(getId(), index);
            qnAEntity.setQuestion(qnA.getQuestion());
            qnAEntity.setAnswer(qnA.getAnswer());
            return qnAEntity;
        }
        return null;
    }
}
