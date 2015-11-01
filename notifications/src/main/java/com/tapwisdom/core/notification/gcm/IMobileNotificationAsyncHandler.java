package com.tapwisdom.core.notification.gcm;

import com.google.android.gcm.server.MulticastResult;

import java.util.List;

public interface IMobileNotificationAsyncHandler {
    void onSuccess(MulticastResult result, List<String> regIds);
    
    void onError(Exception e, List<String> regIds);
}
