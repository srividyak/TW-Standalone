package com.tapwisdom.core.daos.documents;

import org.springframework.data.mongodb.core.index.Indexed;

/**
 * Created by sachin on 23/8/15.
 */
public class CreditMgt extends BaseEntity  {

    @Indexed
    private String userId;

    private int credits;
    private int lastRedemptionRequest;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public int getLastRedemptionRequest() {
        return lastRedemptionRequest;
    }

    public void setLastRedemptionRequest(int lastRedemptionRequest) {
        this.lastRedemptionRequest = lastRedemptionRequest;
    }
}
