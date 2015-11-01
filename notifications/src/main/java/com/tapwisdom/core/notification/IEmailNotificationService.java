package com.tapwisdom.core.notification;

public interface IEmailNotificationService {
    public void send(String subject, String body, String sender, UserEmailNotificationIdentity ... receiver);
}
