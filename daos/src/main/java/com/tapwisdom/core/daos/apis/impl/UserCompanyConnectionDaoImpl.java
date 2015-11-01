package com.tapwisdom.core.daos.apis.impl;

import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.daos.apis.UserCompanyConnectionDao;
import com.tapwisdom.core.daos.documents.UserCompanyConnection;
import com.tapwisdom.core.daos.documents.UserCompanyQuestions;
import org.apache.log4j.Logger;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by srividyak on 10/07/15.
 */
@Component
public class UserCompanyConnectionDaoImpl extends BaseDaoImpl<UserCompanyConnection> implements UserCompanyConnectionDao {
    
    private static final Logger LOG = Logger.getLogger(UserCompanyConnectionDaoImpl.class);
    
    @Override
    public void addToWatchList(String userId, String companyId) throws TapWisdomException {
        Update update = new Update();
        Query query = new Query(Criteria.where("userId").is(userId).andOperator(
                Criteria.where("companyId").is(companyId)));
        update.set("inWatchList", true);
        if (operations.upsert(query, update, UserCompanyConnection.class).isUpdateOfExisting()) {
            LOG.info("updating the existing user company connection document");
        } else {
            LOG.info("inserting new user company connection document");
        }
    }

    @Override
    public UserCompanyConnection getConnection(String userId, String companyId) throws TapWisdomException {
        Query query = new Query(Criteria.where("userId").is(userId).andOperator(
                Criteria.where("companyId").is(companyId)));
        List<UserCompanyConnection> connections = operations.find(query, UserCompanyConnection.class);
        if (connections != null && connections.size() > 0) {
            return connections.get(0);
        }
        return null;
    }

    @Override
    public Boolean isInWatchList(String userId, String companyId) throws TapWisdomException {
        Query query = new Query(Criteria.where("userId").is(userId).andOperator(
                Criteria.where("companyId").is(companyId)));
        List<UserCompanyConnection> userCompanyConnections = operations.find(query, UserCompanyConnection.class);
        Boolean inWatchList = false;
        if (userCompanyConnections != null && userCompanyConnections.size() > 0) {
            inWatchList = userCompanyConnections.get(0).getInWatchList();
        }
        return inWatchList;
    }
    
    private void modifyUserCompanyQuestions(String userId, String companyId, Long timestamp,
                                                            int numQuestions, boolean questionAsked) throws TapWisdomException {
        Query query = new Query(Criteria.where("userId").is(userId).andOperator(
                Criteria.where("companyId").is(companyId)));
        List<UserCompanyConnection> userCompanyConnections = operations.find(query, UserCompanyConnection.class);
        if (userCompanyConnections != null && userCompanyConnections.size() > 0) {
            UserCompanyConnection connection = userCompanyConnections.get(0);
            UserCompanyQuestions userCompanyQuestions;
            if (questionAsked) {
                userCompanyQuestions = connection.getQuestionsAsked();
            } else {
                userCompanyQuestions = connection.getQuestionsAnswered();
            }
            if (userCompanyQuestions == null) {
                userCompanyQuestions = new UserCompanyQuestions();
                userCompanyQuestions.setNumQuestions((long) numQuestions);
                userCompanyQuestions.setFirstQuestionTimestamp(timestamp);
                userCompanyQuestions.setLastQuestionTimestamp(timestamp);
                if (questionAsked) {
                    connection.setQuestionsAsked(userCompanyQuestions);
                } else {
                    connection.setQuestionsAnswered(userCompanyQuestions);
                }
            } else {
                userCompanyQuestions.setLastQuestionTimestamp(timestamp);
                userCompanyQuestions.setNumQuestions(userCompanyQuestions.getNumQuestions() + numQuestions);
            }
            save(connection);
        } else {
            UserCompanyConnection connection = new UserCompanyConnection();
            connection.setUserId(userId);
            connection.setCompanyId(companyId);
            UserCompanyQuestions userCompanyQuestions = new UserCompanyQuestions();
            userCompanyQuestions.setFirstQuestionTimestamp(timestamp);
            userCompanyQuestions.setLastQuestionTimestamp(timestamp);
            userCompanyQuestions.setNumQuestions((long) numQuestions);
            if (questionAsked) {
                connection.setQuestionsAsked(userCompanyQuestions);
            } else {
                connection.setQuestionsAnswered(userCompanyQuestions);
            }
            save(connection);
        }
    }

    @Override
    public void incrementQuestionsAsked(String userId, String companyId, Long timestamp, int numQuestions) throws TapWisdomException {
        modifyUserCompanyQuestions(userId, companyId, timestamp, numQuestions, true);
    }

    @Override
    public void incrementQuestionsAnswered(String userId, String companyId, Long timestamp, int numQuestions) throws TapWisdomException {
        modifyUserCompanyQuestions(userId, companyId, timestamp, numQuestions, false);
    }

    @Override
    public List<UserCompanyConnection> getWatchedCompanies(String userId) throws TapWisdomException {
        Query query = new Query(Criteria.where("userId").is(userId).
                andOperator(Criteria.where("inWatchList").is(true)));
        List<UserCompanyConnection> connections = operations.find(query, UserCompanyConnection.class);
        return connections;
    }
    
    private long getNumQuestions(String userId, String companyId, boolean isAsked) throws TapWisdomException {
        Query query = new Query(Criteria.where("userId").is(userId).andOperator(
                Criteria.where("companyId").is(companyId)));
        List<UserCompanyConnection> connections = operations.find(query, UserCompanyConnection.class);
        if (connections != null && connections.size() > 0) {
            UserCompanyConnection connection = connections.get(0);
            if (isAsked) {
                return connection.getQuestionsAsked() != null ? connection.getQuestionsAsked().getNumQuestions() : 0;
            } else {
                return connection.getQuestionsAnswered().getNumQuestions() != null ? connection.getQuestionsAnswered().getNumQuestions() : 0;
            }
        }
        return 0;
    }

    private long getNumQuestions(String userId, boolean isAsked) throws TapWisdomException {
        Query query = new Query(Criteria.where("userId").is(userId));
        List<UserCompanyConnection> connections = operations.find(query, UserCompanyConnection.class);
        long total = 0;
        for (UserCompanyConnection connection : connections) {
            if (isAsked) {
                if (connection.getQuestionsAsked() != null) {
                    total += connection.getQuestionsAsked().getNumQuestions();
                }
            } else {
                if (connection.getQuestionsAnswered() != null) {
                    total += connection.getQuestionsAnswered().getNumQuestions();
                }
            }
        }
        return total;
    }

    @Override
    public long getNumQuestionsAsked(String userId, String companyId) throws TapWisdomException {
        return getNumQuestions(userId, companyId, true);
    }

    @Override
    public long getNumQuestionsAnswered(String userId, String companyId) throws TapWisdomException {
        return getNumQuestions(userId, companyId, false);
    }

    @Override
    public Long getNumQuestionsAsked(String userId) throws TapWisdomException {
        return getNumQuestions(userId, true);
    }

    @Override
    public Long getNumQuestionsAnswered(String userId) throws TapWisdomException {
        return getNumQuestions(userId, false);
    }
}
