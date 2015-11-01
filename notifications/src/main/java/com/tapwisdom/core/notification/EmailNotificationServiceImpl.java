package com.tapwisdom.core.notification;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.handlers.AsyncHandler;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceAsyncClient;
import com.amazonaws.services.simpleemail.model.*;
import com.tapwisdom.core.common.util.Constants;
import com.tapwisdom.core.common.util.PropertyReader;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmailNotificationServiceImpl implements IEmailNotificationService {
    private static final PropertyReader reader = PropertyReader.getInstance();
    private static final Logger LOG = Logger.getLogger(EmailNotificationServiceImpl.class);
    private static final AsyncHandler<SendEmailRequest, SendEmailResult> asyncHandler = new AmazonSESAsyncHandler();
    
    @Autowired
    private AmazonSimpleEmailServiceAsyncClient client;
    
    @Override
    public void send(String subject, String body, String sender, UserEmailNotificationIdentity... receivers) {
        try {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Sending email from => " + sender + " to => ");
                for (UserEmailNotificationIdentity receiver : receivers) {
                    LOG.debug("receiver => " + receiver.getEmail());
                }
            }
            String fromAddress = sender;
            String[] toAddresses = new String[receivers.length];
            int index = 0;
            for (UserEmailNotificationIdentity receiver : receivers) {
                toAddresses[index++] = receiver.getEmail();
            }
            SendEmailRequest request = new SendEmailRequest();
            request.withSource(fromAddress);
            Destination destination = new Destination().withToAddresses(toAddresses);
            request.setDestination(destination);
            Content subjectContent = new Content(subject);
            Content bodyContent = new Content(body);
            Body emailBody = new Body().withHtml(bodyContent);
            Message message = new Message(subjectContent, emailBody);
            request.setMessage(message);
            client.sendEmailAsync(request, asyncHandler);
        } catch (AmazonClientException e) {
            LOG.error("Error occurred while sending email" , e);
        }
    }
}
