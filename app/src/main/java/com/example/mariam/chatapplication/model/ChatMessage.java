package com.example.mariam.chatapplication.model;

import java.util.Date;

/**
 * Created by Domtyyyyyy on 8/8/2017.
 */

public class ChatMessage {
    private String msgUser;
    private String emailUser;
    private String msgText;
    private String userImage;
    private long time;

    public ChatMessage(String msgUser, String emailUser, String msgText, String userImage) {
        this.msgUser = msgUser;
        this.emailUser = emailUser;
        this.msgText = msgText;
        this.userImage = userImage;
    }

    public ChatMessage(String msgText, String userImage) {
        this.msgText = msgText;
        this.userImage = userImage;
        time = new Date().getTime();
    }

    public long getTime() {
        return time;
    }

    public ChatMessage() {
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setMsgUser(String msgUser) {
        this.msgUser = msgUser;
    }

    public void setMsgText(String msgText) {
        this.msgText = msgText;
    }

    public String getMsgUser() {
        return msgUser;
    }

    public String getMsgText() {
        return msgText;
    }

    public String getEmailUser() {
        return emailUser;
    }

    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
    }
}
