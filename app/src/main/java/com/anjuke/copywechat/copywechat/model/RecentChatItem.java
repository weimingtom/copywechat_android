package com.anjuke.copywechat.copywechat.model;

/**
 * desc:最近聊天
 * author: sishuiye
 * email: sishuiye@anjuke.com
 * date: 2016/4/27
 */
public class RecentChatItem {
    private String userName;
    private String recentMsg;
    private String userIcon;
    private String recentChatTime;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRecentMsg() {
        return recentMsg;
    }

    public void setRecentMsg(String recentMsg) {
        this.recentMsg = recentMsg;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    public String getRecentChatTime() {
        return recentChatTime;
    }

    public void setRecentChatTime(String recentChatTime) {
        this.recentChatTime = recentChatTime;
    }
}
