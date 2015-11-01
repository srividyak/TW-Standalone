package com.tapwisdom.core.notification;

import java.util.Map;

public interface IMobileNotificationService {
    public void send(Map<String, String> data, UserMobileNotificationIdentity... receiver);
}
