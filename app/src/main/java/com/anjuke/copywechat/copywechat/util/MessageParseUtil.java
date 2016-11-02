package com.anjuke.copywechat.copywechat.util;

import com.anjuke.copywechat.copywechat.model.MessageParsed;

/**
 * desc:解析
 * author: sishuiye
 * email: sishuiye@anjuke.com
 * date: 2016/4/22
 */
public class MessageParseUtil {
    public static MessageParsed ParseMsg(String content){
        String[] messageItems = content.split(";");
        MessageParsed messageParsed = new MessageParsed();
        if(messageItems.length<3) return  null; //格式不正确，返回空
        messageParsed.setMessageTypeLevel1(messageItems[0]);
        messageParsed.setMessageTypeLevel2(messageItems[1]);
        messageParsed.setMessageContent(messageItems[2]);
        return messageParsed;
    }
}
