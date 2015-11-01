package com.tapwisdom.core.daos.apis.impl;

import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.common.util.Constants;
import com.tapwisdom.core.common.util.PropertyReader;
import com.tapwisdom.core.daos.apis.QnASessionDao;
import com.tapwisdom.core.daos.documents.QnASession;
import org.apache.log4j.Logger;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class QnASessionDaoImpl extends BaseDaoImpl<QnASession> implements QnASessionDao {
    
    private static final Logger LOG = Logger.getLogger(QnASessionDaoImpl.class);
    
    private static final PropertyReader reader = PropertyReader.getInstance();
    private static final int maxNumResultsInPage = Integer.parseInt(reader.getProperty(Constants.MAX_NUM_MESSAGES_IN_PAGE, "10"));
    
    @Override
    public boolean updateQnASession(QnASession qnASession) throws TapWisdomException {
        if (qnASession.getId() != null) {
            Query query = new Query(Criteria.where("_id").is(qnASession.getId()));
            Update update = new Update();
            if (qnASession.getAdvisorId() != null) {
                update.set("advisorId", qnASession.getAdvisorId());
            }
            if (qnASession.getIsAnswered() != null) {
                update.set("isAnswered", qnASession.getIsAnswered());
            }
            if (qnASession.getAnswerSubmittedTimestamp() != null) {
                update.set("answerSubmittedTimestamp", qnASession.getAnswerSubmittedTimestamp());
            }
            if (qnASession.getQnAList() != null) {
                update.set("qnAList", qnASession.getQnAList());
            }
            return operations.updateFirst(query, update, QnASession.class).isUpdateOfExisting();
        } else {
            throw new TapWisdomException(1, "qnaSessionId is mandatory for updating");
        }
    }

    @Override
    public List<QnASession> getQuestionsAskedBySeeker(String seekerId) throws TapWisdomException {
        return getQuestionsAskedBySeeker(seekerId, 0);
    }

    @Override
    public List<QnASession> getQuestionsAskedBySeeker(String seekerId, int page) throws TapWisdomException {
        Query query = new Query(Criteria.where("seekerId").is(seekerId));
        query.with(new PageRequest(page, maxNumResultsInPage, new Sort(Sort.Direction.DESC, "questionSubmittedTimestamp")));
        List<QnASession> qnaSessionList = operations.find(query, QnASession.class);
        return qnaSessionList;
    }

    @Override
    public List<QnASession> getQuestionsDirectedToAdvisor(String advisorId) throws TapWisdomException {
        return getQuestionsDirectedToAdvisor(advisorId, 0);
    }

    @Override
    public List<QnASession> getQuestionsDirectedToAdvisor(String advisorId, int page) throws TapWisdomException {
        Query query = new Query(Criteria.where("advisorId").is(advisorId));
        query.with(new PageRequest(page, maxNumResultsInPage, new Sort(Sort.Direction.DESC, "questionSubmittedTimestamp")));
        List<QnASession> qnaSessionList = operations.find(query, QnASession.class);
        return qnaSessionList;
    }

    @Override
    public List<QnASession> getQuestionsByCompany(String companyId) throws TapWisdomException {
        return getQuestionsByCompany(companyId, 0);
    }

    @Override
    public List<QnASession> getQuestionsByCompany(String companyId, int page) throws TapWisdomException {
        Query query = new Query(Criteria.where("companyId").is(companyId));
        query.with(new PageRequest(page, maxNumResultsInPage, new Sort(Sort.Direction.DESC, "updatedAt")));
        return operations.find(query, QnASession.class);
    }

    @Override
    public List<QnASession> getQuestionsByCompanies(List<String> companyIds, int page) throws TapWisdomException {
        Query query = new Query(Criteria.where("companyId").in(companyIds));
        query.with(new PageRequest(page, maxNumResultsInPage, new Sort(Sort.Direction.DESC, "updatedAt")));
        return operations.find(query, QnASession.class);
    }

    @Override
    public List<QnASession> getQuestionsByFilters(QnASession qnASession) throws TapWisdomException {
        return getQuestionsByFilters(qnASession, 0);
    }

    @Override
    public List<QnASession> getQuestionsByFilters(QnASession qnASession, int page) throws TapWisdomException {
        Query query = new Query();
        if (qnASession.getSeekerId() != null) {
            query.addCriteria(Criteria.where("seekerId").is(qnASession.getSeekerId()));
        }
        if (qnASession.getAdvisorId() != null) {
            query.addCriteria(Criteria.where("advisorId").is(qnASession.getAdvisorId()));
        }
        if (qnASession.getCompanyId() != null) {
            query.addCriteria(Criteria.where("company").is(qnASession.getCompanyId()));
        }
        if (qnASession.getDesignation() != null) {
            query.addCriteria(Criteria.where("designation").is(qnASession.getDesignation()));
        }
        if (qnASession.getCompanyLocation() != null) {
            query.addCriteria(Criteria.where("companyLocation").is(qnASession.getCompanyLocation()));
        }
        query.with(new PageRequest(page, maxNumResultsInPage, new Sort(Sort.Direction.DESC, "updatedAt")));
        return operations.find(query, QnASession.class);
    }

}
