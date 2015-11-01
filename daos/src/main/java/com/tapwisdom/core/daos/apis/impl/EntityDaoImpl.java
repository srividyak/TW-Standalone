package com.tapwisdom.core.daos.apis.impl;

import com.tapwisdom.core.common.util.Constants;
import com.tapwisdom.core.common.util.PropertyReader;
import com.tapwisdom.core.daos.apis.EntityDao;
import com.tapwisdom.core.daos.documents.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class EntityDaoImpl<T extends UberEntity> implements EntityDao<T> {

    private static final Logger LOG = Logger.getLogger(EntityDaoImpl.class);

    private static final PropertyReader reader = PropertyReader.getInstance();

    private static final Integer MAX_RESULTS = Integer.parseInt(reader.getProperty(Constants.MAX_RES_IN_PAGE, "50"));

    @Autowired
    private MongoOperations operations;

    @Override
    public EntityCharacteristics<T> getEntityById(String id) {
        return operations.findById(id, EntityCharacteristics.class);
    }

    @Override
    public UserEntityRelation<T> getUserEntityRelationById(String id) {
        return operations.findById(id, UserEntityRelation.class);
    }

    @Override
    public void save(EntityCharacteristics<T> t) {
        operations.save(t);
    }

    @Override
    public void save(UserEntityRelation<T> t) {
        operations.save(t);
    }

    @Override
    public EntityViewable<T> getEntityInfoById(String id, EntityType type, String userId) {
        Query userEntityRelationQuery = new Query(new Criteria().andOperator(
                Criteria.where("entity.id").is(id), Criteria.where("userId").is(userId),
                Criteria.where("type").is(type)
        ));
        List<UserEntityRelation> userEntityRelations = operations.find(userEntityRelationQuery,
                UserEntityRelation.class);
        Query entityCharacteristicsQuery = new Query(new Criteria().andOperator(
                Criteria.where("entity.id").is(id), Criteria.where("type").is(type)
        ));
        List<EntityCharacteristics> entityCharacteristicsList = operations.find(entityCharacteristicsQuery,
                EntityCharacteristics.class);
        if (entityCharacteristicsList.size() == 0) {
            return null;
        }
        UserEntityRelation<T> userEntityRelation = userEntityRelations.size() == 0 ? new UserEntityRelation<T>() : userEntityRelations.get(0);
        EntityCharacteristics<T> entityCharacteristics = entityCharacteristicsList.get(0);
        EntityViewable<T> entityViewable = new EntityViewable<T>(entityCharacteristics, userEntityRelation);
        return entityViewable;
    }

    @Override
    public List<EntityViewable<T>> getEntityInfoByIds(List<String> ids, EntityType type, String userId) {
        Query userEntityRelationQuery = new Query(new Criteria().andOperator(
                Criteria.where("entity.id").in(ids),
                Criteria.where("userId").is(userId),
                Criteria.where("type").is(type)
        ));
        List<UserEntityRelation> userEntityRelations = operations.find(userEntityRelationQuery,
                UserEntityRelation.class);
        Query entityCharacteristicsQuery = new Query(new Criteria().andOperator(
                Criteria.where("entity.id").in(ids), Criteria.where("type").is(type)
        ));
        List<EntityCharacteristics> entityCharacteristicsList = operations.find(entityCharacteristicsQuery,
                EntityCharacteristics.class);
        List<EntityViewable<T>> entityViewableList = new ArrayList<EntityViewable<T>>();
        Map<String, UserEntityRelation<T>> entityRelationMap = new HashMap<String, UserEntityRelation<T>>();
        for (UserEntityRelation userEntityRelation : userEntityRelations) {
            entityRelationMap.put(userEntityRelation.getEntity().getId(), userEntityRelation);
        }
        for (EntityCharacteristics entityCharacteristics : entityCharacteristicsList) {
            EntityViewable<T> entityViewable;
            if (entityRelationMap.containsKey(entityCharacteristics.getId())) {
                UserEntityRelation<T> userEntityRelation = entityRelationMap.get(entityCharacteristics.getId());
                entityViewable = new EntityViewable<T>(entityCharacteristics, userEntityRelation);
            } else {
                entityViewable = new EntityViewable<T>(entityCharacteristics, new UserEntityRelation<T>());
            }
            entityViewableList.add(entityViewable);
        }
        return entityViewableList;
    }
    
    @Override
    public EntityCharacteristics<T> getEntityInfoById(String id, EntityType type) {
        Query query = new Query(new Criteria().andOperator(
                Criteria.where("entity.id").is(id), Criteria.where("type").is(type)
        ));
        List<EntityCharacteristics> entityCharacteristicsList = operations.find(query,
                EntityCharacteristics.class);
        return entityCharacteristicsList.size() == 0 ? null : entityCharacteristicsList.get(0);
    }

    private void tapEntity(T t, EntityType type, String userId, String folderName, boolean tap) {
        Query relationQuery = new Query(new Criteria().andOperator(
                Criteria.where("userId").is(userId),
                Criteria.where("entityType").is(type),
                Criteria.where("entity.id").is(t.getId())
        ));
        Update relationUpdate = new Update();
        relationUpdate.set("type", type);
        relationUpdate.set("entity", t);
        relationUpdate.inc("tapCount", 1);
        UserEntityRelation.EntityTap entityTap = new UserEntityRelation.EntityTap();
        entityTap.setFolderName(folderName);
        entityTap.setTap(tap);
        entityTap.setTimestamp(new Date().getTime());
        relationUpdate.set("entityTap", entityTap);
        relationUpdate.set("userId", userId);
        if (operations.upsert(relationQuery, relationUpdate, UserEntityRelation.class).isUpdateOfExisting()) {
            LOG.debug("updating existing user entity relation collection with userId: " + userId +
                    ", type: " + type + ", id: " + t.getId());
        } else {
            LOG.debug("inserting new user entity relation collection with userId: " + userId +
                    ", type: " + type + ", id: " + t.getId());
        }

        Query entityQuery = new Query(new Criteria().andOperator(
                Criteria.where("entity.id").is(t.getId()),
                Criteria.where("type").is(type)
        ));
        Update entityUpdate = new Update();
        entityUpdate.inc("tapCount", tap ? 1 : -1);
        entityUpdate.set("entity", t);
        if (operations.upsert(entityQuery, entityUpdate, EntityCharacteristics.class).isUpdateOfExisting()) {
            LOG.debug("updating existing entity collection with type: " + type + ", id: " + t.getId());
        } else {
            LOG.debug("inserting new entity collection with type: " + type + ", id: " + t.getId());
        }
    }

    @Override
    public void tapEntity(T t, EntityType type, String userId, String folderName) {
        tapEntity(t, type, userId, folderName, true);
    }

    @Override
    public void unTapEntity(T t, EntityType type, String userId, String folderName) {
        tapEntity(t, type, userId, folderName, false);
    }

    @Override
    public List<T> getTappedEntities(String userId, int page) {
        Query query = new Query(new Criteria().andOperator(
                Criteria.where("userId").is(userId),
                Criteria.where("entityTap.tap").is(true)
        ));
        query.with(new PageRequest(page, MAX_RESULTS));
        List<UserEntityRelation> userEntityRelations = operations.find(query, UserEntityRelation.class);
        List<T> entities = new ArrayList<T>();
        for (UserEntityRelation userEntityRelation : userEntityRelations) {
            entities.add((T) userEntityRelation.getEntity());
        }
        return entities;
    }

    @Override
    public List<T> getTappedEntities(String userId, EntityType type, int page) {
        Query query = new Query(new Criteria().andOperator(
                Criteria.where("userId").is(userId),
                Criteria.where("entityTap.tap").is(true),
                Criteria.where("type").is(type)
        ));
        query.with(new PageRequest(page, MAX_RESULTS));
        List<UserEntityRelation> userEntityRelations = operations.find(query, UserEntityRelation.class);
        List<T> entities = new ArrayList<T>();
        for (UserEntityRelation userEntityRelation : userEntityRelations) {
            entities.add((T) userEntityRelation.getEntity());
        }
        return entities;
    }

    @Override
    public List<T> getTappedEntities(String userId, EntityType type, String folderName, int page) {
        Query query = new Query(new Criteria().andOperator(
                Criteria.where("userId").is(userId),
                Criteria.where("entityTap.tap").is(true),
                Criteria.where("type").is(type),
                Criteria.where("entityTap.folderName").is(folderName)
        ));
        query.with(new PageRequest(page, MAX_RESULTS));
        List<UserEntityRelation> userEntityRelations = operations.find(query, UserEntityRelation.class);
        List<T> entities = new ArrayList<T>();
        for (UserEntityRelation userEntityRelation : userEntityRelations) {
            entities.add((T) userEntityRelation.getEntity());
        }
        return entities;
    }

    @Override
    public void viewEntity(T t, EntityType type, String userId) {
        Query userRelationsQuery = new Query(new Criteria().andOperator(
                Criteria.where("userId").is(userId),
                Criteria.where("type").is(type),
                Criteria.where("entity.id").is(t.getId())
        ));
        Update userRelationsUpdate = new Update();
        userRelationsUpdate.set("userId", userId);
        userRelationsUpdate.set("entityView.timestamp", new Date().getTime());
        userRelationsUpdate.inc("entityView.viewCount", 1);
        userRelationsUpdate.set("type", type);
        if (operations.upsert(userRelationsQuery, userRelationsUpdate, UserEntityRelation.class).isUpdateOfExisting()) {
            LOG.debug("updating existing user entity relation collection with userId: " + userId +
                    ", type: " + type + ", id: " + t.getId());
        } else {
            LOG.debug("inserting new user entity relation collection with userId: " + userId +
                    ", type: " + type + ", id: " + t.getId());
        }
        Query entityQuery = new Query(new Criteria().andOperator(
                Criteria.where("type").is(type),
                Criteria.where("entity.id").is(t.getId())
        ));
        Update entityUpdate = new Update();
        entityUpdate.inc("viewCount", 1);
        entityUpdate.set("entity", t);
        if (operations.upsert(entityQuery, entityUpdate, EntityCharacteristics.class).isUpdateOfExisting()) {
            LOG.debug("updating existing entity collection with type: " + type + ", id: " + t.getId());
        } else {
            LOG.debug("inserting new entity collection with type: " + type + ", id: " + t.getId());
        }
    }

    @Override
    public List<T> getViewedEntities(String userId, int page) {
        Query query = new Query(new Criteria().andOperator(
                Criteria.where("userId").is(userId),
                Criteria.where("entityView.viewCount").gt(0)
        ));
        query.with(new PageRequest(page, MAX_RESULTS));
        List<UserEntityRelation> userEntityRelations = operations.find(query, UserEntityRelation.class);
        List<T> entities = new ArrayList<T>();
        for (UserEntityRelation userEntityRelation : userEntityRelations) {
            entities.add((T) userEntityRelation.getEntity());
        }
        return entities;
    }

    @Override
    public List<T> getViewedEntities(String userId, EntityType type, int page) {
        Query query = new Query(new Criteria().andOperator(
                Criteria.where("userId").is(userId),
                Criteria.where("entityView.viewCount").gt(0),
                Criteria.where("type").is(type)
        ));
        query.with(new PageRequest(page, MAX_RESULTS));
        List<UserEntityRelation> userEntityRelations = operations.find(query, UserEntityRelation.class);
        List<T> entities = new ArrayList<T>();
        for (UserEntityRelation userEntityRelation : userEntityRelations) {
            entities.add((T) userEntityRelation.getEntity());
        }
        return entities;
    }

    @Override
    public void upVoteEntity(T t, EntityType type, String userId) {
        Query userRelationsQuery = new Query(new Criteria().andOperator(
                Criteria.where("userId").is(userId),
                Criteria.where("type").is(type),
                Criteria.where("entity.id").is(t.getId())
        ));
        Update userRelationsUpdate = new Update();
        userRelationsUpdate.set("userId", userId);
        userRelationsUpdate.set("entityVote.timestamp", new Date().getTime());
        userRelationsUpdate.set("entityVote.upVote", true);
        userRelationsUpdate.set("type", type);
        if (operations.upsert(userRelationsQuery, userRelationsUpdate, UserEntityRelation.class).isUpdateOfExisting()) {
            LOG.debug("updating existing user entity relation collection with userId: " + userId +
                    ", type: " + type + ", id: " + t.getId());
        } else {
            LOG.debug("inserting new user entity relation collection with userId: " + userId +
                    ", type: " + type + ", id: " + t.getId());
        }

        Query entityQuery = new Query(new Criteria().andOperator(
                Criteria.where("type").is(type),
                Criteria.where("entity.id").is(t.getId())
        ));
        Update entityUpdate = new Update();
        entityUpdate.inc("upVoteCount", 1);
        entityUpdate.set("entity", t);
        if (operations.upsert(entityQuery, entityUpdate, EntityCharacteristics.class).isUpdateOfExisting()) {
            LOG.debug("updating existing entity collection with type: " + type + ", id: " + t.getId());
        } else {
            LOG.debug("inserting new entity collection with type: " + type + ", id: " + t.getId());
        }
    }

    @Override
    public List<T> getUpVotedEntities(String userId, int page) {
        Query query = new Query(new Criteria().andOperator(
                Criteria.where("userId").is(userId),
                Criteria.where("entityVote.upVote").is(true)
        ));
        query.with(new PageRequest(page, MAX_RESULTS));
        List<UserEntityRelation> userEntityRelations = operations.find(query, UserEntityRelation.class);
        List<T> entities = new ArrayList<T>();
        for (UserEntityRelation userEntityRelation : userEntityRelations) {
            entities.add((T) userEntityRelation.getEntity());
        }
        return entities;
    }

    @Override
    public List<T> getUpVotedEntities(String userId, EntityType type, int page) {
        Query query = new Query(new Criteria().andOperator(
                Criteria.where("userId").is(userId),
                Criteria.where("entityVote.upVote").is(true),
                Criteria.where("type").is(type)
        ));
        query.with(new PageRequest(page, MAX_RESULTS));
        List<UserEntityRelation> userEntityRelations = operations.find(query, UserEntityRelation.class);
        List<T> entities = new ArrayList<T>();
        for (UserEntityRelation userEntityRelation : userEntityRelations) {
            entities.add((T) userEntityRelation.getEntity());
        }
        return entities;
    }
}
