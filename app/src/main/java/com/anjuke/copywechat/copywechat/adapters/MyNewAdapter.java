package com.anjuke.copywechat.copywechat.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.anjuke.copywechat.copywechat.ChatActivity;
import com.anjuke.copywechat.copywechat.R;
import com.anjuke.copywechat.copywechat.WebActivityForUrlMessage;
import com.anjuke.copywechat.copywechat.model.MessageForChat;
import com.anjuke.copywechat.copywechat.util.ConstantCls;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * desc:
 * author: sishuiye
 * email: sishuiye@anjuke.com
 * date: 2016/4/6
 */
public class MyNewAdapter extends BaseAdapter{


    private ArrayList<MessageForChat> messageList1;

    private ImageLoader imageLoader1;
    private Context context1;
    private int currentMajorId1=1;
    private Bitmap bitmap;

    public MyNewAdapter(Context context,ArrayList<MessageForChat> messageList, ImageLoader imageLoader1, int currentMajorId1) {
        super();
        context1 = context;
        this.messageList1 = messageList;
        this.imageLoader1 = imageLoader1;
        this.currentMajorId1 = currentMajorId1;

        bitmap = BitmapFactory.decodeResource(context1.getResources(),R.drawable.test);
    }

    @Override
    public int getCount() {
        return messageList1.size();
    }

    private int RIGHTITEM = 1;
    private int  LEFTITEM = 2;
    @Override
    public int getItemViewType(int position) {
        if (messageList1.get(position).getUserId()==currentMajorId1){
            return  RIGHTITEM;
        }else {
            return LEFTITEM;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public Object getItem(int i) {
        return messageList1.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        //Log.i(ConstantCls.LOG_DEBUG_TAG,"the i is :"+i);
        //ViewHolder viewHolder=null;
        ViewHolder viewHolder;
        if(view==null){
            if (getItemViewType(i)==RIGHTITEM){
                view = LayoutInflater.from(context1).inflate( R.layout.activity_chat_chat_item_right, viewGroup, false);
                viewHolder = new ViewHolder();
                viewHolder.iconIv = (ImageView)view.findViewById(R.id.activity_chat_chat_item_right_avatar_iv);
                viewHolder.contentIv = (ImageView)view.findViewById(R.id.activity_chat_chat_item_right_content_iv);
                viewHolder.contentTv = (TextView)view.findViewById(R.id.activity_chat_chat_item_right_content_tv);
                viewHolder.contentTv.setText(messageList1.get(i).getMessageContent());
                view.setTag(viewHolder);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(context1,"你点击了右边的Layout",Toast.LENGTH_LONG).show();
                    }
                });
            }else{
                Log.i(ConstantCls.LOG_DEBUG_TAG,"the i is : left"+i+" currentMajorId1 is :"+currentMajorId1);
                view = LayoutInflater.from(context1).inflate( R.layout.activity_chat_chat_item_left, viewGroup, false);
                //view = View.inflate(context1, R.layout.activity_chat_chat_item_left, null);
                viewHolder = new ViewHolder();
                viewHolder.iconIv = (ImageView)view.findViewById(R.id.activity_chat_chat_item_left_avatar_iv);
                viewHolder.contentIv = (ImageView)view.findViewById(R.id.activity_chat_chat_item_left_content_iv);
                viewHolder.contentTv = (TextView)view.findViewById(R.id.activity_chat_chat_item_left_content_tv);
                viewHolder.contentTv.setText(messageList1.get(i).getMessageContent());
                view.setTag(viewHolder);
            }
            viewHolder.contentIv.setOnClickListener(new lvButtonListener(i));
            Log.i(ConstantCls.LOG_DEBUG_TAG, "messageList1.get(i).getMessagePicPath() is "+messageList1.get(i).getMessagePicPath());
            if(messageList1.get(i).getMessagePicPath().equals("")){
                viewHolder.contentIv.setVisibility(View.GONE);
            }
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }
        MessageForChat messageForChat = messageList1.get(i);
        if (getItemViewType(i) == RIGHTITEM) {
            imageLoader1.displayImage("http://www.linuxidc.com/upload/2011_05/110502075420154.gif", viewHolder.contentIv);
            //viewHolder.contentIv.setImageBitmap(BitmapFactory.decodeResource(context1.getResources(),R.drawable.a1));
        } else {
           viewHolder.contentIv.setImageBitmap(bitmap);
        }

        setItemData(viewHolder, messageForChat);
        return view;
    }

    class lvButtonListener implements View.OnClickListener {
        private int position;

        lvButtonListener(int pos) {
            position = pos;
        }
        @Override
        public void onClick(View view) {
            switch (messageList1.get(position).getMessageType()){
                case ConstantCls.MESSAGE_TYPE_URL:

                    Intent intent = new Intent(context1, WebActivityForUrlMessage.class);
                    intent.putExtra(ConstantCls.URL_ADDRESS_FOR_WEBACTIVITY,"http://www.baidu.com");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context1.startActivity(intent);
                    break;
                default:
                    break;
            }
            Toast.makeText(context1,"你点击了图片"+position,Toast.LENGTH_LONG).show();
        }
    }

    private void setItemData(ViewHolder viewHolder, MessageForChat item) {
        if (null != item) {
            if (item.getUserId()==currentMajorId1){
                imageLoader1.displayImage("http://www.linuxidc.com/upload/2011_05/110502075420154.gif",viewHolder.contentIv);
                viewHolder.contentTv.setText(item.getMessageContent());
/*
                viewHolder.contentIv.setImageBitmap(BitmapFactory.decodeResource(context1.getResources(),R.drawable.a1));*/
            }else{
                viewHolder.contentTv.setText(item.getMessageContent());
            }

            if(item.getMessagePicPath().equals("")){
                viewHolder.contentIv.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    static class ViewHolder {
        ImageView iconIv;
        ImageView contentIv;
        TextView contentTv;
    }
}
