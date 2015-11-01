package com.tapwisdom.core.daos.documents;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "user_timeline")
public class UserTimeLine<T> extends BaseEntity {
    
    private String userId;
    private T entity;
    private Long entityTimestamp;
    private UserTimeLineEntityType entityType;

    public UserTimeLineEntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(UserTimeLineEntityType entityType) {
        this.entityType = entityType;
    }

    public Long getEntityTimestamp() {
        return entityTimestamp;
    }

    public void setEntityTimestamp(Long entityTimestamp) {
        this.entityTimestamp = entityTimestamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public T getEntity() {
        return entity;
    }

    public void setEntity(T entity) {
        this.entity = entity;
    }
}
