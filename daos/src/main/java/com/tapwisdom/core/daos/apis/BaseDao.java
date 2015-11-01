package com.tapwisdom.core.daos.apis;

import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.daos.documents.BaseEntity;
import com.tapwisdom.core.daos.documents.UberEntity;

import java.util.List;
import java.util.Map;

public interface BaseDao<T extends BaseEntity> {
    
    public void save(T t) throws TapWisdomException;
    
    public void delete(T t) throws TapWisdomException;
    
    public T getById(String id, Class<T> clazz) throws TapWisdomException;
    
    public List<T> getByIds(List<String> ids, Class<T> clazz) throws TapWisdomException;
    
    public Map<String, T> getIdsToObjectMap(List<String> ids, Class<T> clazz) throws TapWisdomException;
}
