package com.tapwisdom.core.notification.gcm;

import com.google.android.gcm.server.MulticastResult;
import org.apache.log4j.Logger;

import java.util.List;

public class MobileNotificationAsyncHandlerImpl implements IMobileNotificationAsyncHandler {
    private static final Logger LOG = Logger.getLogger(MobileNotificationAsyncHandlerImpl.class);
    
    @Override
    public void onSuccess(MulticastResult result, List<String> regIds) {
        LOG.info("message was successfully sent to: " + regIds);
        if (LOG.isDebugEnabled()) {
            LOG.debug(result);
        }
    }

    @Override
    public void onError(Exception e, List<String> regIds) {
        LOG.error("error sending message to: " + regIds, e);
    }
}
