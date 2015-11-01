package com.tapwisdom.core.notification;

import com.amazonaws.handlers.AsyncHandler;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.amazonaws.services.simpleemail.model.SendEmailResult;
import org.apache.log4j.Logger;

public class AmazonSESAsyncHandler implements AsyncHandler<SendEmailRequest, SendEmailResult> {
    private static final Logger LOG = Logger.getLogger(AmazonSESAsyncHandler.class);
    
    @Override
    public void onError(Exception exception) {
        LOG.error("An exception occurred while sending email", exception);
    }

    @Override
    public void onSuccess(SendEmailRequest request, SendEmailResult sendEmailResult) {
        LOG.info("Email sent from => " + request + " to => " + sendEmailResult);
    }
}
