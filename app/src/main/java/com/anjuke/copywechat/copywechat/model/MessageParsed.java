package com.anjuke.copywechat.copywechat.model;

import com.anjuke.copywechat.copywechat.util.ConstantCls;

import java.io.Serializable;

/**
 * desc:
 * author: sishuiye
 * email: sishuiye@anjuke.com
 * date: 2016/4/6
 */
public class MessageParsed implements Serializable{
    private String messageTypeLevel1;
    private String messageTypeLevel2;
    private String messageContent;

    public String getMessageTypeLevel1() {
        return messageTypeLevel1;
    }

    public void setMessageTypeLevel1(String messageTypeLevel1) {
        this.messageTypeLevel1 = messageTypeLevel1;
    }

    public String getMessageTypeLevel2() {
        return messageTypeLevel2;
    }

    public void setMessageTypeLevel2(String messageTypeLevel2) {
        this.messageTypeLevel2 = messageTypeLevel2;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }
}
