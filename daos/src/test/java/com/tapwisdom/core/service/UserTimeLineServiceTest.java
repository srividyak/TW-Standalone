package com.tapwisdom.core.service;

import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.daos.apis.*;
import com.tapwisdom.core.daos.documents.*;
import com.tapwisdom.core.daos.service.IUserTimeLineService;
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
import java.util.Arrays;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:daosAppContext_test.xml"
})
public class UserTimeLineServiceTest {
    
    private static final String companyId = "55d21327214a9f111e9f72e2";
    private static final String userId = "myUser";
    private static final String second_userId = "myUser_2";

    @Autowired
    private MongoOperations operations;
    
    @Autowired
    private IUserTimeLineService service;
    
    @Autowired
    private UserCompanyConnectionDao userCompanyConnectionDao;
    
    @Autowired
    private QnASessionDao qnASessionDao;
    
    @Autowired
    private EntityDao<QnAEntity> qnAEntityDao;
    
    @Autowired
    private NewsDao newsDao;
    
    @Autowired
    private EntityDao<News> newsEntityDao;
    
    @Autowired
    private UserDao userDao;
    
    @Autowired
    private EntityDao<User> userEntityDao;
    
    private void createWatchedCompanies() {
        UserCompanyConnection userCompanyConnection = new UserCompanyConnection();
        userCompanyConnection.setCompanyId(companyId);
        userCompanyConnection.setInWatchList(true);
        userCompanyConnection.setUserId(userId);
        try {
            userCompanyConnectionDao.save(userCompanyConnection);
        } catch (TapWisdomException e) {
            e.printStackTrace();
            assert false;
        }
    }
    
    private List<QnASession> getQnASessions() {
        QnA qnAAsked = new QnA();
        Question question = new Question();
        question.setContent("How is culture");
        qnAAsked.setQuestion(question);
        qnAAsked.setAnswer("Culture is good");
        qnAAsked.setQuestionId(0);
        QnASession qnASessionAsked = new QnASession();
        qnASessionAsked.setAdvisorId(second_userId);
        qnASessionAsked.setSeekerId(userId);
        qnASessionAsked.setCompanyId(companyId);
        qnASessionAsked.setIsAnswered(true);
        qnASessionAsked.setCompanyId(companyId);
        qnASessionAsked.setQnAList(Arrays.asList(qnAAsked));

        QnA qnaAnswered = new QnA();
        qnaAnswered.setQuestionId(0);
        Question questionAnswered = new Question();
        questionAnswered.setContent("How is work life balance");
        QnASession qnASessionAnswered = new QnASession();
        qnASessionAnswered.setAdvisorId(userId);
        qnASessionAnswered.setSeekerId(second_userId);
        qnASessionAnswered.setCompanyId(companyId);
        qnASessionAnswered.setQnAList(Arrays.asList(qnaAnswered));
        return Arrays.asList(qnASessionAsked, qnASessionAnswered);
    }
    
    private List<News> getNews() {
        Company company = new Company();
        company.setId(companyId);
        News news = new News();
        news.setCompanies(new Company[]{company});
        news.setContent("Some news");
        news.setTitle("Some title");
        
        News secondNews = new News();
        secondNews.setCompanies(new Company[]{company});
        secondNews.setContent("Some more news");
        secondNews.setTitle("Some more title");
        return Arrays.asList(news, secondNews);
    }
    
    private List<User> getUsers() {
        User user = new User();
        Company company = new Company();
        company.setId(companyId);
        LiProfile liProfile = new LiProfile();
        Position position = new Position();
        position.setCompany(company);
        liProfile.setPositions(Arrays.asList(position));
        user.setLinkedInProfile(liProfile);
        user.setEmail("flipkartEmployee@gmail.com");
        user.setId("flipkartEmployee");
        return Arrays.asList(user);
    }
    
    private void createQnASessions(boolean saveEntityCharacteristics, boolean saveUserEntityRelation) {
        try {
            List<QnASession> qnASessions = getQnASessions();
            for (QnASession qnASession : qnASessions) {
                qnASessionDao.save(qnASession);
                String qnaSessionId = qnASession.getId();
                List<QnA> qnAList = qnASession.getQnAList();
                QnA qnA = qnAList.get(0);
                QnAEntity qnAEntity = new QnAEntity(qnaSessionId, qnA);
                //save qnaEntity entity characteristics
                EntityCharacteristics<QnAEntity> qnAEntityEntityCharacteristics = new EntityCharacteristics<QnAEntity>();
                qnAEntityEntityCharacteristics.setEntity(qnAEntity);
                qnAEntityEntityCharacteristics.setType(EntityType.QNA);
                if (saveEntityCharacteristics) {
                    qnAEntityDao.save(qnAEntityEntityCharacteristics);    
                }
                //save qnaEntity entity userRelation
                UserEntityRelation<QnAEntity> userEntityRelation = new UserEntityRelation<QnAEntity>();
                userEntityRelation.setEntity(qnAEntity);
                userEntityRelation.setType(EntityType.QNA);
                userEntityRelation.setUserId(userId);
                if (saveUserEntityRelation) {
                    qnAEntityDao.save(userEntityRelation);
                }
            }
        } catch (TapWisdomException e) {
            e.printStackTrace();
            assert false;
        }
    }
    
    private void createNews(boolean saveEntityCharacteristics, boolean saveUserEntityRelation) {
        try {
            List<News> newsList = getNews();
            for (News news : newsList) {
                newsDao.save(news);
                // save news entity characteristics
                EntityCharacteristics<News> newsEntityCharacteristics = new EntityCharacteristics<News>();
                newsEntityCharacteristics.setEntity(news);
                newsEntityCharacteristics.setType(EntityType.NEWS);
                if (saveEntityCharacteristics) {
                    newsEntityDao.save(newsEntityCharacteristics);
                }
                // save news entity user relation
                UserEntityRelation<News> userEntityRelation = new UserEntityRelation<News>();
                userEntityRelation.setEntity(news);
                userEntityRelation.setType(EntityType.NEWS);
                userEntityRelation.setUserId(userId);
                if (saveUserEntityRelation) {
                    newsEntityDao.save(userEntityRelation);
                }
            }
        } catch (TapWisdomException e) {
            e.printStackTrace();
            assert false;
        }
    }
    
    private void createUsers(boolean saveEntityCharacteristics, boolean saveUserEntityRelation) {
        try {
            List<User> userList = getUsers();
            for (User user : userList) {
                userDao.save(user);
                // save user entity characteristics
                EntityCharacteristics<User> userEntityCharacteristics = new EntityCharacteristics<User>();
                userEntityCharacteristics.setEntity(user);
                userEntityCharacteristics.setType(EntityType.USER);
                if (saveEntityCharacteristics) {
                    userEntityDao.save(userEntityCharacteristics);
                }
                // save user entity user relation
                UserEntityRelation<User> userEntityRelation = new UserEntityRelation<User>();
                userEntityRelation.setEntity(user);
                userEntityRelation.setType(EntityType.USER);
                userEntityRelation.setUserId(userId);
                if (saveUserEntityRelation) {
                    userEntityDao.save(userEntityRelation);
                }
            }
        } catch (TapWisdomException e) {
            e.printStackTrace();
            assert false;
        }
    }
    
    @Before
    public void createEntities() {
        createWatchedCompanies();
    }
    
    @Test
    public void testGetQuestionsOnCompaniesViewable() {
        createQnASessions(true, true);
        try {
            List<EntityViewable<QnAEntity>> qnaEntityViewables = service.getQuestionsOnCompaniesViewable(userId);
            assert qnaEntityViewables.size() == 2;
        } catch (TapWisdomException e) {
            e.printStackTrace();
            assert false;
        }
        assert true;
    }

    @Test
    public void testGetQuestionsOnCompaniesViewableWithNoEntityRelation() {
        createQnASessions(true, false);
        try {
            List<EntityViewable<QnAEntity>> qnaEntityViewables = service.getQuestionsOnCompaniesViewable(userId);
            assert qnaEntityViewables.size() == 2;
        } catch (TapWisdomException e) {
            e.printStackTrace();
            assert false;
        }
        assert true;
    }

    @Test
    public void testGetQuestionsOnCompaniesViewableWithNoEntityCharacteristics() {
        createQnASessions(false, false);
        try {
            List<EntityViewable<QnAEntity>> qnaEntityViewables = service.getQuestionsOnCompaniesViewable(userId);
            assert qnaEntityViewables.size() == 2;
        } catch (TapWisdomException e) {
            e.printStackTrace();
            assert false;
        }
        assert true;
    }
    
    @Test
    public void testGetNewsEntityViewable() {
        createNews(true, true);
        try {
            List<EntityViewable<News>> newsEntityViewables = service.getNews(userId);
            assert newsEntityViewables.size() == 2;
        } catch (TapWisdomException e) {
            e.printStackTrace();
            assert false;
        }
    }

    @Test
    public void testGetNewsEntityViewableWithNoEntityUserEntityRelation() {
        createNews(true, false);
        try {
            List<EntityViewable<News>> newsEntityViewables = service.getNews(userId);
            assert newsEntityViewables.size() == 2;
        } catch (TapWisdomException e) {
            e.printStackTrace();
            assert false;
        }
    }

    @Test
    public void testGetNewsEntityViewableWithNoEntityCharacteristics() {
        createNews(false, false);
        try {
            List<EntityViewable<News>> newsEntityViewables = service.getNews(userId);
            assert newsEntityViewables.size() == 2;
        } catch (TapWisdomException e) {
            e.printStackTrace();
            assert false;
        }
    }

    @Test
    public void testGetUserEntityViewable() {
        createUsers(true, true);
        try {
            List<EntityViewable<User>> usersEntityViewables = service.getRecentUsersFromWatchedCompanies(userId);
            assert usersEntityViewables.size() == 1;
        } catch (TapWisdomException e) {
            e.printStackTrace();
            assert false;
        }
    }

    @Test
    public void testGetUserEntityViewableWithNoUserRelation() {
        createUsers(true, false);
        try {
            List<EntityViewable<User>> usersEntityViewables = service.getRecentUsersFromWatchedCompanies(userId);
            assert usersEntityViewables.size() == 1;
        } catch (TapWisdomException e) {
            e.printStackTrace();
            assert false;
        }
    }

    @Test
    public void testGetUserEntityViewableWithNoEntityCharacteristics() {
        createUsers(false, false);
        try {
            List<EntityViewable<User>> usersEntityViewables = service.getRecentUsersFromWatchedCompanies(userId);
            assert usersEntityViewables.size() == 1;
        } catch (TapWisdomException e) {
            e.printStackTrace();
            assert false;
        }
    }
    
    @After
    public void deleteEntities() {
        Query query = new Query();
        operations.findAllAndRemove(query, QnASession.class);
        operations.findAllAndRemove(query, UserCompanyConnection.class);
        operations.findAllAndRemove(query, EntityCharacteristics.class);
        operations.findAllAndRemove(query, UserEntityRelation.class);
        operations.findAllAndRemove(query, News.class);
        operations.findAllAndRemove(query, User.class);
    }
}
