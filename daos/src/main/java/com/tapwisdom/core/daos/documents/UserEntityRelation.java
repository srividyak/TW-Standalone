package com.tapwisdom.core.daos.documents;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by srividyak on 29/07/15.
 */
@Document(collection = "entity_relation")
public class UserEntityRelation<T extends UberEntity> extends BaseEntity {
    
    public static class EntityView {
        private Integer viewCount = 0;
        private Long timestamp;

        public Integer getViewCount() {
            return viewCount;
        }

        public void setViewCount(Integer viewCount) {
            this.viewCount = viewCount;
        }

        public Long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(Long timestamp) {
            this.timestamp = timestamp;
        }
    }
    
    public static class EntityVote {
        private Boolean upVote;
        private Long timestamp;

        public Boolean getUpVote() {
            return upVote;
        }

        public void setUpVote(Boolean upVote) {
            this.upVote = upVote;
        }

        public Long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(Long timestamp) {
            this.timestamp = timestamp;
        }
    }

    public static class EntityTap {
        private String folderName;
        private Boolean tap;
        private Long timestamp;

        public String getFolderName() {
            return folderName;
        }

        public void setFolderName(String folderName) {
            this.folderName = folderName;
        }

        public Boolean getTap() {
            return tap;
        }

        public void setTap(Boolean tap) {
            this.tap = tap;
        }

        public Long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(Long timestamp) {
            this.timestamp = timestamp;
        }
    }

    private T entity;
    private EntityType type;
    private String userId;
    private EntityView entityView;
    private EntityVote entityVote;
    private EntityTap entityTap;

    public T getEntity() {
        return entity;
    }

    public void setEntity(T entity) {
        this.entity = entity;
    }

    public EntityType getType() {
        return type;
    }

    public void setType(EntityType type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public EntityView getEntityView() {
        return entityView;
    }

    public void setEntityView(EntityView entityView) {
        this.entityView = entityView;
    }

    public EntityVote getEntityVote() {
        return entityVote;
    }

    public void setEntityVote(EntityVote entityVote) {
        this.entityVote = entityVote;
    }

    public EntityTap getEntityTap() {
        return entityTap;
    }

    public void setEntityTap(EntityTap entityTap) {
        this.entityTap = entityTap;
    }
}
