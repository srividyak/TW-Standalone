package com.tapwisdom.core.daos;

import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.daos.apis.QuestionDao;
import com.tapwisdom.core.daos.documents.Question;
import com.tapwisdom.core.daos.documents.QuestionType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by srividyak on 06/07/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:daosAppContext_test.xml"
})
public class QuestionDaoTest {
    
    @Autowired
    private QuestionDao questionDao;
    
    private List<Question> questions = new ArrayList<Question>();

    @Autowired
    private MongoOperations operations;
    
    @Before
    public void addQuestion() {
        Question question = new Question();
        question.setCompanyId("companyId");
        question.setType(QuestionType.MULTIPLE_CHOICE_SINGLE_ANSWER);
        Map questionContent = new HashMap();
        questionContent.put("question", "How is the culture?");
        String[] answers = {"Excellent", "Good", "Medium", "Bad"};
        questionContent.put("options", answers);
        question.setContent(questionContent);
        question.setTags("culture, HR policies");
        String[] industries = {"Information technology", "Technology", "Services", "Software"};
        question.setIndustries(industries);
        questions.add(question);
        
        question = new Question();
        question.setCompanyId("secondCompanyId");
        question.setContent(questionContent);
        question.setTags("work life balance");
        String[] industries2 = {"civil", "construction", "Software"};
        question.setIndustries(industries2);
        question.setType(QuestionType.MULTIPLE_CHOICE_SINGLE_ANSWER);
        questions.add(question);
        try {
            for (Question q : questions) {
                questionDao.save(q);    
            }
        } catch (TapWisdomException e) {
            e.printStackTrace();
            assert false;
        }
    }
    
    @Test
    public void testGetQuestionsByTagWithExistingTag() {
        String[] tags = {"culture"};
        try {
            List<Question> questions = questionDao.getQuestionsByTags(tags);
            assert questions != null;
            assert questions.size() == 1;
        } catch (TapWisdomException e) {
            e.printStackTrace();
            assert false;
        }
    }
    
    @Test
    public void testGetQuestionsByTagWithNonExistentTag() {
        String[] tags = {"IT"};
        try {
            List<Question> questions = questionDao.getQuestionsByTags(tags);
            assert questions != null;
            assert questions.size() == 0;
        } catch (TapWisdomException e) {
            e.printStackTrace();
            assert false;
        }
    }
    
    @Test
    public void testGetQuestionsByMultipleTags() {
        String[] tags = {"balance", "culture"};
        try {
            List<Question> questions = questionDao.getQuestionsByTags(tags);
            assert questions != null;
            assert questions.size() == 2;
        } catch (TapWisdomException e) {
            e.printStackTrace();
            assert false;
        }
    }
    
    @Test
    public void testGetQuestionsByCompanyWithExistingCompany() {
        String companyId = "companyId";
        try {
            List<Question> questions = questionDao.getQuestionsByCompany(companyId);
            assert questions != null;
            assert questions.size() == 1;
        } catch (TapWisdomException e) {
            e.printStackTrace();
            assert false;
        }
    }
    
    @Test
    public void testGetQuestionsByCompanyWithNonExistentCompany() {
        String companyId = "nonExistentCompany";
        try {
            List<Question> questions = questionDao.getQuestionsByCompany(companyId);
            assert questions != null;
            assert questions.size() == 0;
        } catch (TapWisdomException e) {
            e.printStackTrace();
            assert false;
        }
    }
   
    @Test
    public void testGetQuestionsByIndustriesWithExistingIndustries() {
        String[] industries = {"Software"};
        try {
            List<Question> questions = questionDao.getQuestionsByIndustries(industries);
            assert questions != null;
            assert questions.size() == 2;
        } catch (TapWisdomException e) {
            e.printStackTrace();
            assert false;
        }
    }
    
    @Test
    public void testUpdateQuestion() {
        Question question = questions.get(0);
        String[] industries = {"Information technology", "Technology", "Services", "Software", "IT"};
        question.setIndustries(industries);
        try {
            Boolean updated = questionDao.updateQuestion(question);
            assert updated == true;
            assert question.getIndustries().length == 5;
        } catch (TapWisdomException e) {
            e.printStackTrace();
            assert false;
        }
    }
    
    @After
    public void deleteQuestion() {
        Query query = new Query();
        operations.findAllAndRemove(query, Question.class);
    }
}
