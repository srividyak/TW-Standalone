package com.tapwisdom.core.daos.documents;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by srividyak on 12/07/15.
 */
@Document(collection = "es_config_settings")
public class EsConfigSettings extends BaseEntity {
    
    private Long lastUserToIndexUpdateTime;

    public Long getLastUserToIndexUpdateTime() {
        return lastUserToIndexUpdateTime;
    }

    public void setLastUserToIndexUpdateTime(Long lastUserToIndexUpdateTime) {
        this.lastUserToIndexUpdateTime = lastUserToIndexUpdateTime;
    }
}
