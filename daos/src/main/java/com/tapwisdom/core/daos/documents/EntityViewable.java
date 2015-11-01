package com.tapwisdom.core.daos.documents;

public class EntityViewable<T extends UberEntity> {

    private String userId;
    private UserEntityRelation.EntityView entityView;
    private UserEntityRelation.EntityVote entityVote;
    private UserEntityRelation.EntityTap entityTap;
    private EntityCharacteristics<T> entityCharacteristics;

    public EntityViewable() {
    }

    public EntityViewable(EntityCharacteristics<T> entityCharacteristics, UserEntityRelation<T> userEntityRelation) {
        this.entityCharacteristics = entityCharacteristics;
        entityTap = userEntityRelation.getEntityTap();
        entityView = userEntityRelation.getEntityView();
        entityVote = userEntityRelation.getEntityVote();
        userId = userEntityRelation.getUserId();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public UserEntityRelation.EntityView getEntityView() {
        return entityView;
    }

    public void setEntityView(UserEntityRelation.EntityView entityView) {
        this.entityView = entityView;
    }

    public UserEntityRelation.EntityVote getEntityVote() {
        return entityVote;
    }

    public void setEntityVote(UserEntityRelation.EntityVote entityVote) {
        this.entityVote = entityVote;
    }

    public UserEntityRelation.EntityTap getEntityTap() {
        return entityTap;
    }

    public void setEntityTap(UserEntityRelation.EntityTap entityTap) {
        this.entityTap = entityTap;
    }

    public EntityCharacteristics<T> getEntityCharacteristics() {
        return entityCharacteristics;
    }

    public void setEntityCharacteristics(EntityCharacteristics<T> entityCharacteristics) {
        this.entityCharacteristics = entityCharacteristics;
    }
}
