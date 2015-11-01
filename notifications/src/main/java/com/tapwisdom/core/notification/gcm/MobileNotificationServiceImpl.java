package com.tapwisdom.core.notification.gcm;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Sender;
import com.tapwisdom.core.common.util.Constants;
import com.tapwisdom.core.common.util.PropertyReader;
import com.tapwisdom.core.common.util.Shutdownable;
import com.tapwisdom.core.notification.IMobileNotificationService;
import com.tapwisdom.core.notification.UserMobileNotificationIdentity;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class MobileNotificationServiceImpl implements IMobileNotificationService, Shutdownable {
    private final ExecutorService executorService;
    private final Sender sender;

    private static final PropertyReader reader = PropertyReader.getInstance();
    private static final Integer MAX_THREADS = Integer.parseInt(reader.getProperty(Constants.MAX_MOBILE_NOTIFICATION_THREADS, "1"));
    private static final String GCM_API_KEY = reader.getProperty(Constants.GCM_API_KEY, "");
    private static final Integer GCM_MAX_NUM_RETRIES = Integer.parseInt(reader.getProperty(Constants.GCM_MAX_NUM_RETRIES, "1"));
    private static final IMobileNotificationAsyncHandler mobileNotificationHandler = new MobileNotificationAsyncHandlerImpl();
    private static final Logger LOG = Logger.getLogger(MobileNotificationServiceImpl.class);

    public MobileNotificationServiceImpl() {
        executorService = Executors.newFixedThreadPool(MAX_THREADS);
        sender = new Sender(GCM_API_KEY);
    }

    @Override
    public void shutdown() {
        LOG.info("Shutting down mobile notification service");
        executorService.shutdown();
    }

    @Override
    public void send(Map<String, String> data, UserMobileNotificationIdentity... receivers) {
        Message message = new Message.Builder().setData(data).build();
        List<String> ids = new ArrayList<String>();
        for (UserMobileNotificationIdentity receiver : receivers) {
            ids.add(receiver.getDeviceId());
        }
        executorService.submit(new MobileNotificationSender(message, ids, mobileNotificationHandler));
    }

    private class MobileNotificationSender implements Runnable {
        private Message message;
        private List<String> regIds;
        private IMobileNotificationAsyncHandler asyncHandler;


        public MobileNotificationSender(Message message, List<String> regIds, 
                                        IMobileNotificationAsyncHandler asyncHandler) {
            this.message = message;
            this.regIds = regIds;
            this.asyncHandler = asyncHandler;
        }

        @Override
        public void run() {
            try {
                MulticastResult result = sender.send(message, regIds, GCM_MAX_NUM_RETRIES);
                asyncHandler.onSuccess(result, regIds);
            } catch (IOException e) {
                e.printStackTrace();
                asyncHandler.onError(e, regIds);
            }
        }
    }
}
