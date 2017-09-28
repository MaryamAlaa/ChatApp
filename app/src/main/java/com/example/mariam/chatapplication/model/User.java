package com.example.mariam.chatapplication.model;

import android.content.Context;

import com.squareup.picasso.Picasso;

import java.io.Serializable;

/**
 * Created by Domtyyyyyy on 8/20/2017.
 */

public class User implements Serializable{
   public String msgUser, userImage, userId;

    public User(String msgUser, String userImage, String userId) {
        this.msgUser = msgUser;
        this.userImage = userImage;
        this.userId = userId;
    }

    public User() {
    }

    public void setMsgUser(String msgUser) {
        this.msgUser = msgUser;
    }

    public void setUserImage(String userImage, Context c) {

        this.userImage = userImage;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMsgUser() {
        return msgUser;
    }

    public String getUserImage() {
        return userImage;
    }

    public String getUserId() {
        return userId;
    }
}
