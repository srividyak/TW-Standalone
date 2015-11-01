package com.tapwisdom.core.notification;

public class UserEmailNotificationIdentity extends UserNotificationIdentity {
    private String email;

    public UserEmailNotificationIdentity(String userId, String email) {
        super(userId);
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
