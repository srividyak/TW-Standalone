package com.tapwisdom.core.daos.apis;

import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.daos.documents.User;

import java.util.List;

/**
 * Created by srividyak on 14/05/15.
 */
public interface SearchDao {
    
    public List<User> getUsersByCompany(String company) throws TapWisdomException;
    
    public List<User> getUsersByLocation(String location) throws TapWisdomException;
}
