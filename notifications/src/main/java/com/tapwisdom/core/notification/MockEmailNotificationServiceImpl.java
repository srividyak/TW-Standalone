package com.tapwisdom.core.notification;

public class MockEmailNotificationServiceImpl implements IEmailNotificationService {
    @Override
    public void send(String subject, String body, String sender, UserEmailNotificationIdentity... receiver) {
        // no op
    }
}
