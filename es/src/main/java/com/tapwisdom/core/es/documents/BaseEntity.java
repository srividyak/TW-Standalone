package com.tapwisdom.core.es.documents;

import org.springframework.data.annotation.Id;

/**
 * Created by srividyak on 16/07/15.
 */
public class BaseEntity {
    
    @Id
    private String id;
    private Long createdAt;
    private Long updatedAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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
}
