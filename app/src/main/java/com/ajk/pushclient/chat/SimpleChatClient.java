package com.ajk.pushclient.chat;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.anjuke.copywechat.copywechat.util.ConstantCls;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class SimpleChatClient {
    private Handler handler;

    private final String host;
    private final int port;
    private Channel channel=null;

    public SimpleChatClient(String host, int port, Handler handler){
        this.host = host;
        this.port = port;
        this.handler = handler;
    }

    public Handler run() throws Exception{
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap  = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new SimpleChatClientInitializer(handler));
            channel = bootstrap.connect(host, port).sync().channel();
            /*BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            while(true){
                channel.writeAndFlush(in.readLine() + "\r\n");
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }

        return new Handler(){
            @Override
            public void handleMessage(Message msg) {
                Log.i(ConstantCls.LOG_DEBUG_TAG, "发送了数据："+msg);
                channel.writeAndFlush(msg);
            }
        };
    }
}
