package com.tapwisdom.core.notification;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;

import java.io.IOException;

public class GCMSample {
    public static void main(String[] args) {
        Sender sender = new Sender("AIzaSyDIFuPE55ZCSVt6Xkh0LNCqPXi04-wVB5M");
        Message message = new Message.Builder().addData("name", "value").build();
        try {
            Result result = sender.send(message, "c1EsUMwUL2M:APA91bGdL99XHRF1b5kozJj7tqshSUdmNQ9QkhWwFIEG1uOZZJurHwIk-o3eDuYlfShJbWnF7_CazXoo9UGaJEbzDDdksqchJLWrehEL2lWkH94m6mZS6xc6iGn6f0P8dXRa2MB4h0eZ", 1);
            System.out.println(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
