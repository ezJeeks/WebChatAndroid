package com.example.webchat.models;

import java.util.Date;

public class ChatMessage {
    public String dateTime;
    public String senderId;
    public String receiverId;
    public String conversionImage;
    public String conversionName;
    public String conversionId;
    public String message;
    public Date dateObject;

    public boolean isOnline;

    public boolean isOnline() {
        return isOnline;
    }
}
