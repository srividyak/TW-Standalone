package com.tapwisdom.core.daos.apis;

import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.daos.documents.*;

import java.util.List;

public interface EntityDao<T extends UberEntity> {
    
    EntityCharacteristics<T> getEntityById(String id);

    UserEntityRelation<T> getUserEntityRelationById(String id);
    
    void save(EntityCharacteristics<T> t) throws TapWisdomException;

    void save(UserEntityRelation<T> t) throws TapWisdomException;
    
    // get any entity's complete info wrt viewer of the entity
    EntityViewable<T> getEntityInfoById(String id, EntityType type, String userId);
    
    List<EntityViewable<T>> getEntityInfoByIds(List<String> ids, EntityType type, String userId);
    
    EntityCharacteristics<T> getEntityInfoById(String id, EntityType type);

    // tap
    void tapEntity(T entity, EntityType entityType, String userId, String folderName) 
            throws TapWisdomException;
    
    void unTapEntity(T entity, EntityType entityType, String userId, String folderName);

    List<T> getTappedEntities(String userId, int page);
    
    List<T> getTappedEntities(String userId, EntityType type, int page);

    List<T> getTappedEntities(String userId, EntityType type, String folderName, int page);

    // view
    void viewEntity(T t, EntityType type, String userId);
    
    List<T> getViewedEntities(String userId, int page);
    
    List<T> getViewedEntities(String userId, EntityType type, int page);
    
    // upVote
    void upVoteEntity(T t, EntityType type, String userId);
    
    List<T> getUpVotedEntities(String userId, int page);
    
    List<T> getUpVotedEntities(String userId, EntityType type, int page);

}
