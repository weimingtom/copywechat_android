package com.anjuke.copywechat.copywechat;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.anjuke.copywechat.copywechat.chattest.MyClientInitializer;
import com.anjuke.copywechat.copywechat.util.ConstantCls;

import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class ChatActivityTestFri extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static Context context;
    public static int MSG_REC=0xabc;
    public static int PORT = ConstantCls.PUSH_SERVER_FOR_CHAT_PORT;
    public static final String HOST = ConstantCls.PUSH_SERVER_FOR_CHAT_IP;
    private NioEventLoopGroup group;
    private Button sendButton;
    private Channel mChannel;
    private ChannelFuture cf;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==0){
                Toast.makeText(context, msg.obj.toString(), Toast.LENGTH_LONG).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_activity_test_fri);
        context = this;
        connected();
        sendButton = (Button) findViewById(R.id.ChatActivityTestFriBtn);
        Log.i(ConstantCls.LOG_DEBUG_TAG, "sendButton:"+sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    // 连接到Socket服务端
    private void connected() {
        new Thread() {
            @Override
            public void run() {
                group = new NioEventLoopGroup();
                try {
                    // Client服务启动器 3.x的ClientBootstrap
                    // 改为Bootstrap，且构造函数变化很大，这里用无参构造。
                    Bootstrap bootstrap = new Bootstrap();
                    // 指定EventLoopGroup
                    bootstrap.group(group);
                    // 指定channel类型
                    bootstrap.channel(NioSocketChannel.class);
                    // 指定Handler
                    bootstrap.handler(new MyClientInitializer(context,mHandler));
                    bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
                    bootstrap.option(ChannelOption.TCP_NODELAY, true);
                    bootstrap.option(ChannelOption.SO_TIMEOUT, 5000);
                    // 连接到本地的7878端口的服务端
                    cf = bootstrap.connect(new InetSocketAddress(
                            HOST, PORT));
                    mChannel = cf.sync().channel();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    //发送数据
    private void sendMessage() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.i(TAG, "mChannel.write sth & " + mChannel.isOpen());
                    Log.i(ConstantCls.LOG_DEBUG_TAG, "mChannel.write sth & " + mChannel.isOpen()+cf.isSuccess()+cf.isVoid());
                    mChannel.writeAndFlush("hello,this message is from client.\r\n");
                    mChannel.read();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (group != null) {
            group.shutdownGracefully();
        }
    }

}
