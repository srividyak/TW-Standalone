package com.tapwisdom.core.daos.apis.impl;

import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.common.util.Constants;
import com.tapwisdom.core.common.util.PropertyReader;
import com.tapwisdom.core.daos.apis.QuestionDao;
import com.tapwisdom.core.daos.documents.Question;
import org.apache.log4j.Logger;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class QuestionDaoImpl extends BaseDaoImpl<Question> implements QuestionDao {
    
    private static final Logger LOG = Logger.getLogger(QuestionDaoImpl.class);
    
    private static final PropertyReader reader = PropertyReader.getInstance();
    private static final int maxNumQuestions = Integer.parseInt(reader.getProperty(Constants.MAX_NUM_MESSAGES_IN_PAGE, "10"));
    
    @Override
    public List<Question> getQuestionsByTags(String[] tags) throws TapWisdomException {
        TextCriteria textCriteria = TextCriteria.forDefaultLanguage().matchingAny(tags);
        Query query = TextQuery.queryText(textCriteria).sortByScore();
        query.with(new PageRequest(0, maxNumQuestions));
        List<Question> questions = operations.find(query, Question.class);
        return questions;
    }

    @Override
    public List<Question> getQuestionsByCompany(String companyId) throws TapWisdomException {
        Query query = new Query(Criteria.where("companyId").is(companyId));
        query.with(new PageRequest(0, maxNumQuestions));
        List<Question> questions = operations.find(query, Question.class);
        return questions;
    }

    @Override
    public List<Question> getQuestionsByIndustries(String[] industries) throws TapWisdomException {
        Query query = new Query(Criteria.where("industries").in(industries));
        query.with(new PageRequest(0, maxNumQuestions));
        List<Question> questions = operations.find(query, Question.class);
        return questions;
    }

    @Override
    public Boolean updateQuestion(Question question) throws TapWisdomException {
        if (question.getId() != null) {
            Query query = new Query(Criteria.where("_id").is(question.getId()));
            Update update = new Update();
            if (question.getContent() != null) {
                update.set("content", question.getContent());
            }
            if (question.getIndustries() != null) {
                update.set("industries", question.getIndustries());
            }
            if (question.getTags() != null) {
                update.set("tags", question.getTags());
            }
            if (question.getType() != null) {
                update.set("type", question.getType());
            }
            if (question.getCompanyId() != null) {
                update.set("companyId", question.getCompanyId());
            }
            return operations.updateFirst(query, update, Question.class).isUpdateOfExisting();
        } else {
            throw new TapWisdomException(1, "question id is mandatory to update the same");
        }
    }
}
