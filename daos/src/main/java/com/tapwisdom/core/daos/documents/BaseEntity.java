package com.tapwisdom.core.daos.documents;

import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Date;

public class BaseEntity extends UberEntity implements Serializable {
    
    private Long createdAt;
    private Long updatedAt;
    
    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        return this.getId().equals(((BaseEntity) o).getId());
    }
    
    @Override
    public int hashCode() {
        return this.getId().hashCode();
    }
}
