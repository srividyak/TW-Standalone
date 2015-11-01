package com.tapwisdom.core.daos.apis.impl;

import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.daos.apis.BaseDao;
import com.tapwisdom.core.daos.documents.BaseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.*;

public class BaseDaoImpl<T extends BaseEntity> implements BaseDao<T> {
    
    @Autowired
    protected MongoOperations operations;
    
    @Override
    public void save(T o) throws TapWisdomException {
        Long now = new Date().getTime();
        if (o.getId() == null) {
            o.setCreatedAt(now);
        }
        o.setUpdatedAt(now);
        operations.save(o);
    }

    @Override
    public void delete(T t) throws TapWisdomException {
        operations.remove(t);
    }

    @Override
    public T getById(String id, Class<T> clazz) throws TapWisdomException {
        return operations.findById(id, clazz);
    }

    @Override
    public List<T> getByIds(List<String> ids, Class<T> clazz) throws TapWisdomException {
        List<T> list = new ArrayList<T>();
        for (String id : ids) {
            T t = operations.findById(id, clazz);
            list.add(t);
        }
        return list;
    }

    @Override
    public Map<String, T> getIdsToObjectMap(List<String> ids, Class<T> clazz) throws TapWisdomException {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").in(ids));
        List<T> list = operations.find(query, clazz);
        Map<String, T> map = new HashMap<String, T>();
        for (T t : list) {
            map.put(t.getId(), t);
        }
        return map;
    }
}
