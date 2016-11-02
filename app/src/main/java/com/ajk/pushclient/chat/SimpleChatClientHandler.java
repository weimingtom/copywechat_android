package com.ajk.pushclient.chat;

import android.os.Handler;
import android.util.Log;

import com.anjuke.copywechat.copywechat.util.ConstantCls;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class SimpleChatClientHandler extends  SimpleChannelInboundHandler<String> {
    private Handler handler;
    public SimpleChatClientHandler(Handler hander){
        this.handler = hander;
    }
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String s) throws Exception {
        Log.i(ConstantCls.LOG_DEBUG_TAG,s + "ssssssssssssssssssssssssss");
        handler.sendEmptyMessage(0);
    }
}