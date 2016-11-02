package com.anjuke.copywechat.copywechat.chattest;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * desc:
 * author: sishuiye
 * email: sishuiye@anjuke.com
 * date: 2016/4/8
 */
public class MyClientHandler extends SimpleChannelInboundHandler<String> {
    private Context ctx;
    private Handler handler;

    public MyClientHandler(Context context, Handler handler) {
        this.ctx = context;
        this.handler = handler;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg)
            throws Exception {
        Log.d("MyHelloClientHandler", "channelRead0->msg="+msg);
        Message m = new Message();
        m.what = 0;
        m.obj = msg;
        handler.sendMessage(m);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client active");
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client close ");
        super.channelInactive(ctx);
    }

}
