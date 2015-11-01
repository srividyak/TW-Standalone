package com.tapwisdom.core.daos.service.impl;

import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.daos.apis.*;
import com.tapwisdom.core.daos.documents.*;
import com.tapwisdom.core.daos.service.IUserTimeLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserTimeLineServiceImpl implements IUserTimeLineService {
    
    @Autowired
    private QnASessionDao qnASessionDao;
    
    @Autowired
    private EntityDao<QnAEntity> qnAEntityEntityDao;
    
    @Autowired
    private EntityDao<News> newsEntityDao;
    
    @Autowired
    private NewsDao newsDao;
    
    @Autowired
    private UserTimeLineDao userTimeLineDao;
    
    @Autowired
    private UserCompanyConnectionDao userCompanyConnectionDao;
    
    @Autowired
    private UserDao userDao;
    
    @Autowired
    private EntityDao<User> userEntityDao;

    @Override
    public List<EntityViewable<News>> getNews(String userId) throws TapWisdomException {
        List<String> companyIds = getWatchedCompanyIds(userId);
        if (companyIds.size() > 0) {
            List<News> newsList = newsDao.getNewsList(companyIds);
            List<String> newsIds = new ArrayList<String>();
            for (News news : newsList) {
                newsIds.add(news.getId());
            }
            if (newsIds.size() > 0) {
                List<EntityViewable<News>> newsEntityViewableList = newsEntityDao.getEntityInfoByIds(newsIds, EntityType.NEWS, userId);
                Map<String, EntityViewable<News>> entityViewableMap = new HashMap<String, EntityViewable<News>>();
                for (EntityViewable<News> entityViewable : newsEntityViewableList) {
                    entityViewableMap.put(entityViewable.getEntityCharacteristics().getEntity().getId(), entityViewable);
                }
                for (News news : newsList) {
                    if (!entityViewableMap.containsKey(news.getId())) {
                        EntityCharacteristics<News> entityCharacteristics = new EntityCharacteristics<News>();
                        entityCharacteristics.setEntity(news);
                        entityCharacteristics.setType(EntityType.NEWS);
                        EntityViewable<News> entityViewable = new EntityViewable<News>(entityCharacteristics, new UserEntityRelation<News>());
                        entityViewable.setUserId(userId);
                        entityCharacteristics.setUpdatedAt(news.getUpdatedAt());
                        newsEntityViewableList.add(entityViewable);
                    }
                }
                return newsEntityViewableList;
            }
        }
        return new ArrayList<EntityViewable<News>>();
    }

    @Override
    public List<EntityViewable<QnAEntity>> getQuestionsOnCompaniesViewable(String userId) throws TapWisdomException {
        List<String> companyIds = getWatchedCompanyIds(userId);
        if (companyIds.size() > 0) {
            List<QnASession> qnASessions = qnASessionDao.getQuestionsByCompanies(companyIds, 0);
            List<String> qnaEntityIds = new ArrayList<String>();
            for (QnASession qnASession : qnASessions) {
                if (qnASession.getQnAList() != null) {
                    for (int i = 0, max = qnASession.getQnAList().size(); i < max; i++) {
                        qnaEntityIds.add(QnAEntity.getQnAEntityId(qnASession.getId(), i));
                    }
                }
            }
            if (qnaEntityIds.size() > 0) {
                List<EntityViewable<QnAEntity>> qnAEntityViewableList = qnAEntityEntityDao.getEntityInfoByIds(qnaEntityIds,
                        EntityType.QNA, userId);
                Map<String, EntityViewable<QnAEntity>> entityViewableMap = new HashMap<String, EntityViewable<QnAEntity>>();
                for (EntityViewable<QnAEntity> entityViewable : qnAEntityViewableList) {
                    entityViewableMap.put(entityViewable.getEntityCharacteristics().getEntity().getId(), entityViewable);
                }
                for (QnASession qnASession : qnASessions) {
                    if (qnASession.getQnAList() != null) {
                        for (int i = 0, max = qnASession.getQnAList().size(); i < max; i++) {
                            String qnaEntityId = QnAEntity.getQnAEntityId(qnASession.getId(), i);
                            if (!entityViewableMap.containsKey(qnaEntityId)) {
                                EntityCharacteristics<QnAEntity> entityCharacteristics = new EntityCharacteristics<QnAEntity>();
                                entityCharacteristics.setEntity(qnASession.buildQnAEntity(i));
                                entityCharacteristics.setType(EntityType.QNA);
                                EntityViewable<QnAEntity> entityViewable = new EntityViewable<QnAEntity>(entityCharacteristics, new UserEntityRelation<QnAEntity>());
                                entityViewable.setUserId(userId);
                                entityCharacteristics.setUpdatedAt(qnASession.getUpdatedAt());
                                qnAEntityViewableList.add(entityViewable);
                            }
                        }
                    }
                }
                return qnAEntityViewableList;
            }
        }
        return new ArrayList<EntityViewable<QnAEntity>>();
    }

    @Override
    public List<EntityViewable<User>> getRecentUsersFromWatchedCompanies(String userId) throws TapWisdomException {
        List<String> companyIds = getWatchedCompanyIds(userId);
        if (companyIds.size() > 0) {
            List<User> users = userDao.getUsersByCompanyIds(companyIds, 0);
            List<String> userIds = new ArrayList<String>();
            for (User user : users) {
                userIds.add(user.getId());
            }
            if (userIds.size() > 0) {
                List<EntityViewable<User>> userEntityViewableList = userEntityDao.getEntityInfoByIds(userIds, EntityType.USER, userId);
                Map<String, EntityViewable<User>> entityViewableMap = new HashMap<String, EntityViewable<User>>();
                for (EntityViewable<User> entityViewable : userEntityViewableList) {
                    entityViewableMap.put(entityViewable.getEntityCharacteristics().getEntity().getId(), entityViewable);
                }
                for (User user : users) {
                    if (!entityViewableMap.containsKey(user.getId())) {
                        EntityCharacteristics<User> entityCharacteristics = new EntityCharacteristics<User>();
                        entityCharacteristics.setEntity(user);
                        entityCharacteristics.setType(EntityType.USER);
                        entityCharacteristics.setUpdatedAt(user.getUpdatedAt());
                        EntityViewable<User> entityViewable = new EntityViewable<User>(entityCharacteristics, new UserEntityRelation<User>());
                        entityViewable.setUserId(userId);
                        userEntityViewableList.add(entityViewable);
                    }
                }
                return userEntityViewableList;
            }
        }
        return new ArrayList<EntityViewable<User>>();
    }

    @Override
    public <T extends UberEntity> void saveTimeLineEntities(List<EntityViewable<T>> entityViewables, String userId, UserTimeLineEntityType type) throws TapWisdomException {
        for (EntityViewable<T> entityViewable : entityViewables) {
            UserTimeLine<EntityViewable<T>> userTimeLine = new UserTimeLine<EntityViewable<T>>();
            userTimeLine.setEntity(entityViewable);
            userTimeLine.setUserId(userId);
            userTimeLine.setEntityTimestamp(entityViewable.getEntityCharacteristics().getUpdatedAt());
            userTimeLine.setEntityType(type);
            userTimeLineDao.save(userTimeLine);
        }
    }

    @Override
    public void deleteQuestions(String userId) {
        userTimeLineDao.deleteEntities(UserTimeLineEntityType.QUESTIONS, userId);
    }

    @Override
    public void deleteNews(String userId) {
        userTimeLineDao.deleteEntities(UserTimeLineEntityType.NEWS, userId);
    }

    @Override
    public void deleteUsers(String userId) {
        userTimeLineDao.deleteEntities(UserTimeLineEntityType.USERS, userId);
    }

    private List<String> getWatchedCompanyIds(String userId) throws TapWisdomException {
        List<UserCompanyConnection> userCompanyConnections = userCompanyConnectionDao.getWatchedCompanies(userId);
        List<String> companyIds = new ArrayList<String>();
        for (UserCompanyConnection userCompanyConnection : userCompanyConnections) {
            companyIds.add(userCompanyConnection.getCompanyId());
        }
        return companyIds;
    }
}
