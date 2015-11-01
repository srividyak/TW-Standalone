package com.tapwisdom.core.daos.service;

import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.daos.documents.*;

import java.util.List;

public interface IUserTimeLineService {

    List<EntityViewable<News>> getNews(String userId) throws TapWisdomException;
    
    List<EntityViewable<QnAEntity>> getQuestionsOnCompaniesViewable(String userId) throws TapWisdomException;

    List<EntityViewable<User>> getRecentUsersFromWatchedCompanies(String userId) throws TapWisdomException;
    
    <T extends UberEntity> void saveTimeLineEntities(List<EntityViewable<T>> entityViewables, String userId, UserTimeLineEntityType type) throws TapWisdomException;
    
    void deleteQuestions(String userId);

    void deleteNews(String userId);
    
    void deleteUsers(String userId);
}
