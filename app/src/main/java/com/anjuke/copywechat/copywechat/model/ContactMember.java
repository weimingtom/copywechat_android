package com.anjuke.copywechat.copywechat.model;

/**
 * desc:
 * author: sishuiye
 * email: sishuiye@anjuke.com
 * date: 2016/4/7
 */
public class ContactMember {
    private String contactCat;  //用户类别
    private String contactName;  //用户类别
    private String contactID;  //用户类别
    private int contactCatID=0;

    public String getContactCat() {
        return contactCat;
    }

    public void setContactCat(String contactCat) {
        this.contactCat = contactCat;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactID() {
        return contactID;
    }

    public void setContactID(String contactID) {
        this.contactID = contactID;
    }

    public int getContactCatID() {
        return contactCatID;
    }

    public void setContactCatID(int contactCatID) {
        this.contactCatID = contactCatID;
    }
}
