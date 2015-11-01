package com.tapwisdom.core.es.repositories;

import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.es.UserSearchCriteria;
import com.tapwisdom.core.es.documents.User;

import java.util.List;

/**
 * Created by srividyak on 16/07/15.
 */
public interface IUserSearchRepository {
    
    public User getUserByUserId(String userId) throws TapWisdomException;

    public List<User> getUsers(UserSearchCriteria criteria, int page) throws TapWisdomException;
    
}
