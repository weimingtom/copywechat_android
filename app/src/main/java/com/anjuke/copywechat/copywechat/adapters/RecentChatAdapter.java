package com.anjuke.copywechat.copywechat.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.anjuke.copywechat.copywechat.R;
import com.anjuke.copywechat.copywechat.model.RecentChatItem;
import com.anjuke.copywechat.copywechat.util.ConstantCls;
import com.anjuke.copywechat.view.BannerAdAutoScrollView;

import java.util.List;
import java.util.Random;
import java.util.zip.Inflater;

/**
 * desc:    最近聊天列表的适配器
 * author: sishuiye
 * email: sishuiye@anjuke.com
 * date: 2016/4/26
 */
public class RecentChatAdapter extends BaseAdapter{

    private int[] userIcons = {R.drawable.p1,R.drawable.p2,R.drawable.p3,R.drawable.p4,R.drawable.p5,R.drawable.p6,R.drawable.p7,R.drawable.p8};
    private Context mContext;
    private List<RecentChatItem> recentChatItemList;
    private LayoutInflater mInflater;
    private static Random r1 = new Random();
    private BannerAdAutoScrollView headlines_view_vp;

    public RecentChatAdapter(Context context, List<RecentChatItem> recentChatList){
        recentChatItemList = recentChatList;
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return recentChatItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return recentChatItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        RecentChatItem recentChatItem = recentChatItemList.get(position);
        if(convertView==null){

            ConstantCls.DebugY("初始化第"+position+"ssssssssssssssssssss"+recentChatItemList.size());
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_recent_chat_list,parent,false);
            holder.userIconIv = (ImageView)convertView.findViewById(R.id.recent_chat_item_icon_iv);
            holder.userNameTv = (TextView)convertView.findViewById(R.id.recent_chat_item_name_tv);
            holder.recentMsgTv = (TextView)convertView.findViewById(R.id.recent_chat_item_msg_tv);
            holder.recentChatTimeTv = (TextView)convertView.findViewById(R.id.recent_chat_item_time_tv);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        //随机选择头像
        int i1 = r1.nextInt(7);
        Log.i(ConstantCls.LOG_DEBUG_TAG,"随机数是："+i1);
        holder.userIconIv.setBackgroundResource(userIcons[i1]);
        holder.userNameTv.setText(recentChatItem.getUserName());
        holder.recentMsgTv.setText(recentChatItem.getRecentMsg());
        holder.recentChatTimeTv.setText(recentChatItem.getRecentChatTime()+i1+" PM");

        return convertView;
    }
    private class ViewHolder {
        ImageView userIconIv;
        TextView userNameTv;
        TextView recentMsgTv;
        TextView recentChatTimeTv;
    }
}
