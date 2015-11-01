package com.tapwisdom.core.daos.documents;

/**
 * Created by srividyak on 20/04/15.
 */
public class Message {

    String messageText;
    String id;
    MessageStatus status = MessageStatus.active;

    public MessageStatus getStatus() {
        return status;
    }

    public void setStatus(MessageStatus status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }
}
