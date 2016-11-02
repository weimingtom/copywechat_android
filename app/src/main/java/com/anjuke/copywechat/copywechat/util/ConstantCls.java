package com.anjuke.copywechat.copywechat.util;

import android.util.Log;

/**
 * desc:
 * author: sishuiye
 * email: sishuiye@anjuke.com
 * date: 2016/4/1
 */
public class ConstantCls {
    public static final String LOG_DEBUG_TAG="ThisIsCopyWeChatTag";
    public static final String FRAGMENT_LOG_DEBUG_TAG="ThisIsTagfragement";

    //Activity间传递
    public static final String USERNAME_FOR_CHATACTIVITY = "usernameforchatactivity";
    public static final String URL_ADDRESS_FOR_WEBACTIVITY = "urladdressforwebactivity";

    //消息类型
    public static final int MESSAGE_TYPE_PIC = 2;
    public static final int MESSAGE_TYPE_URL = 3;
    public static final int MESSAGE_TYPE_TEXT = 1;

    //推送相关
    public static final String PUSH_SERVER_FOR_CHAT_IP = "10.249.9.44";
    public static final int PUSH_SERVER_FOR_CHAT_PORT = 8001;
    public static final int PUSH_SERVER_FOR_RESTFUL_PORT = 8080;

    //用户在线关系
    public static final String LOGIN_CODE_STORED_IN_SHAREPREFERENCE="LOGIN_CODE_STORED_IN_SHAREPREFERENCE";

    //与服务器间交互指令 指令间以分号进行分隔 默认解析两级，无两级第二级默认为空
    public static final String MESSAGE_TYPE_LOGIN="LOGIN";   //登录信息
    // LOGIN;;USERNAME;PASSWORD
    public static final String MESSAGE_TYPE_MSG="MSG";   //聊天信息
    // MSG;MSG_TYPE;TOUSER/FROMUSER;CONTENT eg;MSG;TEXT/PIC;1;Hello
    public static final String MESSAGE_TYPE_PUSH="PUSH";   //推送信息
    //PUSH;PUSH_TYPE;CONTENT

    public static void DebugY(String str){
        Log.d("sishui","+++++++======---->>>   "+str);
    }
}
