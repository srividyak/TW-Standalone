package com.tapwisdom.core.daos;

import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.daos.apis.QnASessionDao;
import com.tapwisdom.core.daos.documents.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;

/**
 * Created by srividyak on 06/07/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:daosAppContext_test.xml"
})
public class QnASessionDaoTest {
    
    @Autowired
    private QnASessionDao qnASessionDao;
    
    private QnASession qnASession;

    @Autowired
    private MongoOperations operations;
    
    @Before
    public void createQnASession() {
        qnASession = new QnASession();
        qnASession.setSeekerId("seeker");
        qnASession.setCompanyId("company");
        qnASession.setDesignation("software engineer");
        qnASession.setAdvisorId("advisor");
        List<QnA> qnAs = new ArrayList<QnA>();
        
        QnA qnA = new QnA();
        
        // question 1
        Question question = new Question();
        question.setCompanyId("company");
        question.setType(QuestionType.MULTIPLE_CHOICE_SINGLE_ANSWER);
        Map questionContent = new HashMap();
        questionContent.put("question", "How is the culture?");
        String[] answers = {"Excellent", "Good", "Medium", "Bad"};
        questionContent.put("options", answers);
        question.setContent(questionContent);
        question.setTags("culture, HR policies");
        String[] industries = {"Information technology", "Technology", "Services", "Software"};
        question.setIndustries(industries);
        qnA.setQuestion(question);
        qnAs.add(qnA);
        
        // question 2
        question = new Question();
        question.setCompanyId("company");
        question.setType(QuestionType.TEXT);
        question.setContent("What technologies do they work on?");
        question.setTags("technology");
        question.setIndustries(industries);
        qnA = new QnA();
        qnA.setQuestion(question);
        
        qnAs.add(qnA);
        
        qnASession.setQuestionSubmittedTimestamp(new Date().getTime());
        qnASession.setQnAList(qnAs);

        try {
            qnASessionDao.save(qnASession);
        } catch (TapWisdomException e) {
            e.printStackTrace();
            assert false;
        }
    }
    
    @Test
    public void testQuestionsBySeeker() {
        try {
            List<QnASession> qnASessions = qnASessionDao.getQuestionsAskedBySeeker("seeker");
            assert qnASessions != null;
            assert qnASessions.size() == 1;
        } catch (TapWisdomException e) {
            e.printStackTrace();
            assert false;
        }
    }
    
    @Test
    public void testQuestionsDirectedToSeeker() {
        try {
            List<QnASession> qnASessions = qnASessionDao.getQuestionsDirectedToAdvisor("advisor");
            assert qnASessions != null;
            assert qnASessions.size() == 1;
        } catch (TapWisdomException e) {
            e.printStackTrace();
            assert false;
        }
    }
    
    @Test
    public void testQuestionsByCompany() {
        try {
            List<QnASession> qnASessions = qnASessionDao.getQuestionsByCompany("company");
            assert qnASessions != null;
            assert qnASessions.size() == 1;
        } catch (TapWisdomException e) {
            e.printStackTrace();
            assert false;
        }
    }
    
    @Test
    public void testQuestionsByFilter() {
        QnASession qnASession = new QnASession();
        qnASession.setSeekerId("seeker");
        qnASession.setAdvisorId("advisor");
        try {
            List<QnASession> qnASessions = qnASessionDao.getQuestionsByFilters(qnASession);
            assert qnASessions != null;
            assert qnASessions.size() == 1;
        } catch (TapWisdomException e) {
            e.printStackTrace();
            assert false;
        }
    }
    
    @Test
    public void testQuestionsByFilterWithNoResult() {
        QnASession qnASession = new QnASession();
        qnASession.setSeekerId("seeker");
        qnASession.setAdvisorId("advisor");
        qnASession.setCompanyId("newCompany");
        try {
            List<QnASession> qnASessions = qnASessionDao.getQuestionsByFilters(qnASession);
            assert qnASessions != null;
            assert qnASessions.size() == 0;
        } catch (TapWisdomException e) {
            e.printStackTrace();
            assert false;
        }
    }
    
    @Test
    public void testUpdateQnASession() {
        QnASession qnASession = new QnASession();
        qnASession.setId(this.qnASession.getId());
        qnASession.setAdvisorId("advisor");
        try {
            boolean updated = qnASessionDao.updateQnASession(qnASession);
            assert updated == true;
            assert qnASession.getAdvisorId().equals("advisor");
        } catch (TapWisdomException e) {
            e.printStackTrace();
            assert false;
        }
    }
    
    @After
    public void deleteQnASession() {
        Query query = new Query();
        operations.findAllAndRemove(query, QnASession.class);
    }
    
}
