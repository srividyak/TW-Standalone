package com.tapwisdom.core.daos.documents;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by srividyak on 29/07/15.
 */
@Document(collection = "entity_characteristics")
public class EntityCharacteristics<T extends UberEntity> extends BaseEntity {

    private T entity;
    private EntityType type;
    private Integer upVoteCount = 0;
    private Integer downVoteCount = 0;
    private Integer tapCount = 0;
    private Integer viewCount = 0;

    public EntityType getType() {
        return type;
    }

    public void setType(EntityType type) {
        this.type = type;
    }

    public T getEntity() {
        return entity;
    }

    public void setEntity(T entity) {
        this.entity = entity;
    }

    public Integer getUpVoteCount() {
        return upVoteCount;
    }

    public void setUpVoteCount(Integer upVoteCount) {
        this.upVoteCount = upVoteCount;
    }

    public Integer getDownVoteCount() {
        return downVoteCount;
    }

    public void setDownVoteCount(Integer downVoteCount) {
        this.downVoteCount = downVoteCount;
    }

    public Integer getTapCount() {
        return tapCount;
    }

    public void setTapCount(Integer tapCount) {
        this.tapCount = tapCount;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }
}
