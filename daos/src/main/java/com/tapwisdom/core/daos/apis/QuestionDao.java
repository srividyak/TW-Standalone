package com.tapwisdom.core.daos.apis;

import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.daos.documents.Question;

import java.util.List;

/**
 * Created by srividyak on 04/07/15.
 */
public interface QuestionDao extends BaseDao<Question> {
    
    public List<Question> getQuestionsByTags(String[] tags) throws TapWisdomException;
    
    public List<Question> getQuestionsByCompany(String companyId) throws TapWisdomException;
    
    public List<Question> getQuestionsByIndustries(String[] industries) throws TapWisdomException;
    
    public Boolean updateQuestion(Question question) throws TapWisdomException;
    
}
