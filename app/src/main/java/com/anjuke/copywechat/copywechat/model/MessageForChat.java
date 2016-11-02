package com.anjuke.copywechat.copywechat.model;

import com.anjuke.copywechat.copywechat.util.ConstantCls;

import java.io.Serializable;

/**
 * desc:
 * author: sishuiye
 * email: sishuiye@anjuke.com
 * date: 2016/4/6
 */
public class MessageForChat implements Serializable{
    private int userId;
    private String userName;
    private String iconPath;
    private String messageContent;
    private String messagePicPath;
    private int messageType;

    public MessageForChat(){
        userId=0;
        userName="";
        messagePicPath="";
        messageType= ConstantCls.MESSAGE_TYPE_TEXT;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getMessagePicPath() {
        return messagePicPath;
    }

    public void setMessagePicPath(String messagePicPath) {
        this.messagePicPath = messagePicPath;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }
}
