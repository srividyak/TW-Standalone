package com.tapwisdom.core.notification;

public class UserMobileNotificationIdentity extends UserNotificationIdentity {
    private String deviceId;

    public UserMobileNotificationIdentity(String userId, String deviceId) {
        super(userId);
        this.deviceId = deviceId;
    }

    public String getDeviceId() {
        return deviceId;
    }
}
