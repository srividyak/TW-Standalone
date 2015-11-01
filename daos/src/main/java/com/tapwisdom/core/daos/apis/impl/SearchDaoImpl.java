package com.tapwisdom.core.daos.apis.impl;

import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.daos.apis.SearchDao;
import com.tapwisdom.core.daos.documents.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by srividyak on 14/05/15.
 */
@Component
public class SearchDaoImpl implements SearchDao {

    @Autowired
    protected MongoOperations operations;
    
    @Override
    public List<User> getUsersByCompany(String company) throws TapWisdomException {
        Query query = new Query();
        return null;
    }

    @Override
    public List<User> getUsersByLocation(String location) throws TapWisdomException {
        return null;
    }
}
