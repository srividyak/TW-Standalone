package com.tapwisdom.core.daos.apis;

import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.daos.documents.User;

import java.util.List;

public interface UserDao extends BaseDao<User> {
    
    Boolean updateUser(User user) throws TapWisdomException;
    
    User getUser(String id);
    
    User getUserByEmail(String email);
    
    List<User> getUsersById(List<String> ids);
    
    List<User> getUsersUpdatedWithinTimeRange(Long timestampAfter, Long timestampBefore) throws TapWisdomException;
    
    List<User> getUsers(int page);

    List<User> getUsersByCompanyIds(List<String> companyIds, int page);
    
}
