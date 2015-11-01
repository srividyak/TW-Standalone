package com.tapwisdom.core.daos.documents;

import java.io.Serializable;

/**
 * Created by srividyak on 01/07/15.
 */
public class UserProfileView implements Serializable {
    
    private Long viewCount;
    private Long viewTimestamp;

    public Long getViewCount() {
        return viewCount;
    }

    public void setViewCount(Long viewCount) {
        this.viewCount = viewCount;
    }

    public Long getViewTimestamp() {
        return viewTimestamp;
    }

    public void setViewTimestamp(Long viewTimestamp) {
        this.viewTimestamp = viewTimestamp;
    }
}
