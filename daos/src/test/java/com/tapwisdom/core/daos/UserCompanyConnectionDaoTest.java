package com.tapwisdom.core.daos;

import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.daos.apis.CompanyDao;
import com.tapwisdom.core.daos.apis.UserCompanyConnectionDao;
import com.tapwisdom.core.daos.apis.UserDao;
import com.tapwisdom.core.daos.documents.Company;
import com.tapwisdom.core.daos.documents.User;
import com.tapwisdom.core.daos.documents.UserCompanyConnection;
import com.tapwisdom.core.daos.documents.UserCompanyQuestions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;

/**
 * Created by srividyak on 11/07/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:daosAppContext_test.xml"
})
public class UserCompanyConnectionDaoTest {
    
    @Autowired
    private UserCompanyConnectionDao dao;
    
    @Autowired
    private UserDao userDao;
    
    @Autowired
    private CompanyDao companyDao;
    
    @Autowired
    private MongoOperations operations;
    
    private String userId;
    private String companyId;
    private String connectionId;
    
    @Before
    public void createUserAndCompany() {
        User user = new User();
        user.setEmail("vidya@gmail.com");
        Company company = new Company();
        company.setName("tapwisdom");
        try {
            userDao.save(user);
            companyDao.save(company);
            userId = user.getId();
            companyId = company.getId();
            UserCompanyConnection connection = new UserCompanyConnection();
            connection.setCompanyId(companyId);
            connection.setUserId(userId);
            dao.save(connection);
            connectionId = connection.getId();
        } catch (TapWisdomException e) {
            e.printStackTrace();
            assert false;
        }
    }
    
    @Test
    public void testAddToWatchList() {
        try {
            dao.addToWatchList(userId, companyId);
            UserCompanyConnection connection = dao.getById(connectionId, UserCompanyConnection.class);
            assert connection.getInWatchList();
        } catch (TapWisdomException e) {
            e.printStackTrace();
            assert false;
        }
    }
    
    @Test
    public void testIncrementQuestionsAsked() {
        try {
            UserCompanyConnection connection = dao.getById(connectionId, UserCompanyConnection.class);
            long numQuestionsAskedBefore = connection.getQuestionsAsked() != null ? connection.getQuestionsAsked().getNumQuestions() : 0;
            dao.incrementQuestionsAsked(userId, companyId, new Date().getTime(), 1);
            connection = dao.getById(connectionId, UserCompanyConnection.class);
            long numQuestionsAskedAfter = connection.getQuestionsAsked().getNumQuestions();
            assert numQuestionsAskedAfter == numQuestionsAskedBefore + 1;
        } catch (TapWisdomException e) {
            e.printStackTrace();
            assert false;
        }
    }
    
    @Test
    public void testIncrementQuestionsAnswered() {
        try {
            UserCompanyConnection connection = dao.getById(connectionId, UserCompanyConnection.class);
            long numQuestionsAnsweredBefore = connection.getQuestionsAnswered() != null ? connection.getQuestionsAnswered().getNumQuestions() : 0;
            dao.incrementQuestionsAnswered(userId, companyId, new Date().getTime(), 1);
            connection = dao.getById(connectionId, UserCompanyConnection.class);
            long numQuestionsAnsweredAfter = connection.getQuestionsAnswered().getNumQuestions();
            assert numQuestionsAnsweredAfter == numQuestionsAnsweredBefore + 1;
        } catch (TapWisdomException e) {
            e.printStackTrace();
            assert false;
        }
    }
    
    @Test
    public void testGetWatchedCompaniesWithoutWatching() {
        try {
            List<UserCompanyConnection> connections = dao.getWatchedCompanies(userId);
            assert connections != null;
            assert connections.size() == 0;
        } catch (TapWisdomException e) {
            e.printStackTrace();
            assert false;
        }
    }

    @Test
    public void testGetWatchedCompaniesWithSingleCompany() {
        try {
            dao.addToWatchList(userId, companyId);
            List<UserCompanyConnection> connections = dao.getWatchedCompanies(userId);
            assert connections != null;
            assert connections.size() == 1;
        } catch (TapWisdomException e) {
            e.printStackTrace();
            assert false;
        }
    }

    @Test
    public void testGetWatchedCompaniesWithMultipleCompanies() {
        try {
            dao.addToWatchList(userId, companyId);
            dao.addToWatchList(userId, "companyId");
            List<UserCompanyConnection> connections = dao.getWatchedCompanies(userId);
            assert connections != null;
            assert connections.size() == 2;
        } catch (TapWisdomException e) {
            e.printStackTrace();
            assert false;
        }
    }
    
    @Test
    public void testIsInWatchListFalse() {
        try {
            assert !dao.isInWatchList(userId, companyId);
        } catch (TapWisdomException e) {
            e.printStackTrace();
            assert false;
        }
    }

    @Test
    public void testIsInWatchListTrue() {
        try {
            dao.addToWatchList(userId, companyId);
            assert dao.isInWatchList(userId, companyId);
        } catch (TapWisdomException e) {
            e.printStackTrace();
            assert false;
        }
    }
    
    @Test
    public void testNumQuestionsAsked() {
        try {
            dao.incrementQuestionsAsked(userId, companyId, new Date().getTime(), 1);
            long numQuestions = dao.getNumQuestionsAsked(userId, companyId);
            assert numQuestions == 1;
        } catch (TapWisdomException e) {
            e.printStackTrace();
            assert false;
        }
    }

    @Test
    public void testNumQuestionsAnswered() {
        try {
            dao.incrementQuestionsAnswered(userId, companyId, new Date().getTime(), 1);
            long numQuestions = dao.getNumQuestionsAnswered(userId, companyId);
            assert numQuestions == 1;
        } catch (TapWisdomException e) {
            e.printStackTrace();
            assert false;
        }
    }
    
    @After
    public void deleteAll() {
        Query query = new Query();
        operations.findAllAndRemove(query, Company.class);
        operations.findAllAndRemove(query, User.class);
        operations.findAllAndRemove(query, UserCompanyConnection.class);
    }
    
}
