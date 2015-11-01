package com.tapwisdom.core.daos.apis;

import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.daos.documents.UserCompanyConnection;

import java.util.List;

/**
 * Created by srividyak on 10/07/15.
 */
public interface UserCompanyConnectionDao extends BaseDao<UserCompanyConnection> {
    
    public void addToWatchList(String userId, String companyId) throws TapWisdomException;
    
    public UserCompanyConnection getConnection(String userId, String companyId) throws TapWisdomException;
    
    public Boolean isInWatchList(String userId, String companyId) throws TapWisdomException;
    
    public void incrementQuestionsAsked(String userId, String companyId, Long timestamp, int numQuestions) throws TapWisdomException;

    public void incrementQuestionsAnswered(String userId, String companyId, Long timestamp, int numQuestions) throws TapWisdomException;
    
    public List<UserCompanyConnection> getWatchedCompanies(String userId) throws TapWisdomException;
    
    public long getNumQuestionsAsked(String userId, String companyId) throws TapWisdomException;
    
    public long getNumQuestionsAnswered(String userId, String companyId) throws TapWisdomException;
    
    public Long getNumQuestionsAsked(String userId) throws TapWisdomException;
    
    public Long getNumQuestionsAnswered(String userId) throws TapWisdomException;

}
