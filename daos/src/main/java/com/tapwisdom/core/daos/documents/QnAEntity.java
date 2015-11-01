package com.tapwisdom.core.daos.documents;

import org.springframework.data.annotation.Transient;

public class QnAEntity extends UberEntity {

    private Question question;
    private Object answer;
    
    @Transient
    private String qnaSessionId;
    
    @Transient
    private Integer questionId;
    
    private static final String ID_SEPARATOR = "__";
    
    public static String getQnAEntityId(String qnaSessionId, int questionId) {
        return qnaSessionId + ID_SEPARATOR + questionId;
    }

    public QnAEntity(String qnaSessionId, int questionId) {
        this.setId(getQnAEntityId(qnaSessionId, questionId));
        this.qnaSessionId = qnaSessionId;
        this.questionId = questionId;
    }
    
    public QnAEntity(String qnaSessionId, QnA qnA) {
        this(qnaSessionId, qnA.getQuestionId());
        question = qnA.getQuestion();
        answer = qnA.getAnswer();
        this.qnaSessionId = qnaSessionId;
    }

    public QnAEntity(){

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

    public String getQnaSessionId() {
        if (qnaSessionId == null) {
            if (getId() != null) {
                String[] splits = getId().split(ID_SEPARATOR);
                if (splits.length == 2) {
                    qnaSessionId = splits[0];
                }
            }
        }
        return qnaSessionId;
    }

    public void setQnaSessionId(String qnaSessionId) {
        this.qnaSessionId = qnaSessionId;
    }

    public Integer getQuestionId() {
        if (questionId == null) {
            if (getId() != null) {
                String[] splits = getId().split(ID_SEPARATOR);
                if (splits.length == 2) {
                    questionId = Integer.parseInt(splits[1]);
                }
            }
        }
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }
}
