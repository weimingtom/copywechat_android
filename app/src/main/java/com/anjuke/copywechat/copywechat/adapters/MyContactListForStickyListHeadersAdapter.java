package com.anjuke.copywechat.copywechat.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.anjuke.copywechat.copywechat.R;
import com.anjuke.copywechat.copywechat.model.ContactMember;

import java.util.ArrayList;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * desc:
 * author: sishuiye
 * email: sishuiye@anjuke.com
 * date: 2016/4/7
 */
public class MyContactListForStickyListHeadersAdapter extends BaseAdapter implements StickyListHeadersAdapter {
    //private String[] contactArray;
    private ArrayList<ContactMember> contactMemberList;
    private LayoutInflater inflater;

    public MyContactListForStickyListHeadersAdapter(Context context, ArrayList<ContactMember> contactMemberList1) {
        inflater = LayoutInflater.from(context);
        contactMemberList = contactMemberList1;
    }

    @Override
    public int getCount() {
        return contactMemberList.size();
    }

    @Override
    public Object getItem(int position) {
        return contactMemberList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.test_list_item_layout, parent, false);
            holder.text = (TextView) convertView.findViewById(R.id.text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.text.setText(contactMemberList.get(position).getContactName());

        return convertView;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;
        if (convertView == null) {
            holder = new HeaderViewHolder();
            convertView = inflater.inflate(R.layout.header, parent, false);
            holder.text = (TextView) convertView.findViewById(R.id.text1);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }
        //set header text as first char in name
        String headerText = "" + contactMemberList.get(position).getContactCat();
        holder.text.setText(headerText);
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        //return the first character of the country as ID because this is what headers are based upon
        return contactMemberList.get(position).getContactCatID();
    }

    class HeaderViewHolder {
        TextView text;
    }

    class ViewHolder {
        TextView text;
    }
}
