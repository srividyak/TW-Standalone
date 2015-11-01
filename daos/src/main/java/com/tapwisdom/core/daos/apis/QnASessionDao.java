package com.tapwisdom.core.daos.apis;

import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.daos.documents.QnASession;

import java.util.List;

public interface QnASessionDao extends BaseDao<QnASession> {
    
    public boolean updateQnASession(QnASession qnASession) throws TapWisdomException;
    
    public List<QnASession> getQuestionsAskedBySeeker(String seekerId) throws TapWisdomException;

    public List<QnASession> getQuestionsAskedBySeeker(String seekerId, int page) throws TapWisdomException;

    public List<QnASession> getQuestionsDirectedToAdvisor(String advisorId) throws TapWisdomException;

    public List<QnASession> getQuestionsDirectedToAdvisor(String advisorId, int page) throws TapWisdomException;

    public List<QnASession> getQuestionsByCompany(String companyId) throws TapWisdomException;
    
    public List<QnASession> getQuestionsByCompany(String companyId, int page) throws TapWisdomException;
    
    public List<QnASession> getQuestionsByCompanies(List<String> companyIds, int page) throws TapWisdomException;

    public List<QnASession> getQuestionsByFilters(QnASession qnASession) throws TapWisdomException;
    
    public List<QnASession> getQuestionsByFilters(QnASession qnASession, int page) throws TapWisdomException;

}
