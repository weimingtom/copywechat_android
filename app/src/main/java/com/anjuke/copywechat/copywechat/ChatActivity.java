package com.anjuke.copywechat.copywechat;

import android.content.Context;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ajk.pushclient.chat.SimpleChatClient;
import com.ajk.pushclient.chat.SimpleChatClientInitializer;
import com.anjuke.copywechat.copywechat.adapters.MyNewAdapter;
import com.anjuke.copywechat.copywechat.chattest.MyClientInitializer;
import com.anjuke.copywechat.copywechat.model.MessageForChat;
import com.anjuke.copywechat.copywechat.util.ConstantCls;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.net.InetSocketAddress;
import java.util.ArrayList;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class ChatActivity extends Activity implements View.OnClickListener{

    private Button btn_sendMessage;
    private ListView lv_contentMessage;
    private EditText editText_sentMessage;
    private TextView title_chat_activity;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Log.i(ConstantCls.LOG_DEBUG_TAG, msg.obj.toString());
        }
    };


    private EventLoopGroup group;
    private MyNewAdapter myAdapter;
    private ArrayList<MessageForChat> messageList;

    private ImageLoader imageLoader;
    private Channel mChannel;
    private ChannelFuture cf;
    private int currentMajorId=1;


    private static Context context;
    public static int MSG_REC=0xabc;
    public static int PORT = ConstantCls.PUSH_SERVER_FOR_CHAT_PORT;
    public static final String HOST = ConstantCls.PUSH_SERVER_FOR_CHAT_IP;
    private NioEventLoopGroup group2;
    private Button sendButton;
    private Channel mChannel2;
    private ChannelFuture cf2;
    private Handler mHandler2 = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==0){
                Log.i(ConstantCls.LOG_DEBUG_TAG,"服务器发回数据："+msg.obj.toString());

                String msg2 = msg.obj.toString();
                if(!msg2.contains("[you]")) {
                    MessageForChat messageForChat = new MessageForChat();
                    messageForChat.setUserId(2);
                    messageForChat.setMessageContent(msg2);
                    messageList.add(messageForChat);
                    myAdapter.notifyDataSetChanged();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        context=this;
        connected2();
        btn_sendMessage = (Button)findViewById(R.id.btn_send_message);
        btn_sendMessage.setOnClickListener(this);

        lv_contentMessage = (ListView)findViewById(R.id.lv_message_content);

        messageList = new ArrayList<>();
        MessageForChat messageForChat1 = new MessageForChat();
        messageForChat1.setUserId(1);
        messageForChat1.setMessagePicPath("ssss");
        MessageForChat messageForChat2 = new MessageForChat();
        messageForChat2.setUserId(2);
        messageForChat2.setMessageType(ConstantCls.MESSAGE_TYPE_URL);
        messageForChat2.setMessagePicPath("ssss");
        messageList.add(messageForChat2);
        messageList.add(messageForChat2);
        messageList.add(messageForChat1);
        messageList.add(messageForChat1);
        messageList.add(messageForChat1);
        messageList.add(messageForChat1);
        messageList.add(messageForChat2);
        messageList.add(messageForChat1);
        messageList.add(messageForChat2);
        messageList.add(messageForChat1);
        messageList.add(messageForChat1);

        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(ChatActivity.this));

        myAdapter = new MyNewAdapter(getBaseContext(), messageList,imageLoader,currentMajorId);
        lv_contentMessage.setAdapter(myAdapter);

        lv_contentMessage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(ChatActivity.this,"你点击了Item",Toast.LENGTH_LONG).show();
            }
        });

        editText_sentMessage = (EditText)findViewById(R.id.edittext_sent_message);

        title_chat_activity = (TextView)findViewById(R.id.title_chat_activity);
        String dialcompany = getIntent().getExtras().get(ConstantCls.USERNAME_FOR_CHATACTIVITY).toString();
        title_chat_activity.setText("和"+dialcompany+"聊天");
    }


    // 连接到Socket服务端
    private void connected2() {
        new Thread() {
            @Override
            public void run() {
                group2 = new NioEventLoopGroup();
                try {
                    // Client服务启动器 3.x的ClientBootstrap
                    // 改为Bootstrap，且构造函数变化很大，这里用无参构造。
                    Bootstrap bootstrap = new Bootstrap();
                    // 指定EventLoopGroup
                    bootstrap.group(group2);
                    // 指定channel类型
                    bootstrap.channel(NioSocketChannel.class);
                    // 指定Handler
                    bootstrap.handler(new MyClientInitializer(context,mHandler2));
                    bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
                    bootstrap.option(ChannelOption.TCP_NODELAY, true);
                    bootstrap.option(ChannelOption.SO_TIMEOUT, 5000);

                    cf2 = bootstrap.connect(new InetSocketAddress(
                            HOST, PORT));
                    mChannel2 = cf2.sync().channel();
                    //发送握手信息

                    mChannel2.writeAndFlush("CHAT;user1;1\n");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }


    //发送数据
    private void sendMessage3() {
        mHandler2.post(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.i(ConstantCls.LOG_DEBUG_TAG, "mChannel.write sth & " + mChannel2.isOpen());
                    Log.i(ConstantCls.LOG_DEBUG_TAG, "mChannel.write sth & " + mChannel2.isOpen()+cf2.isSuccess()+cf2.isVoid());
                    mChannel2.writeAndFlush("sss");
                    mChannel2.read();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
/*
    //发送数据
    private void sendMessage2() {

        mHandler2.post(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.i(TAG, "mChannel.write sth & " + mChannel2.isOpen());
                    Log.i(ConstantCls.LOG_DEBUG_TAG, "mChannel.write sth & " + mChannel2.isOpen()+cf2.isSuccess()+cf2.isVoid());
                    mChannel2.writeAndFlush("hello,this message is from client.\r\n");
                    mChannel2.read();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }*/

    class MyThread implements Runnable{
        String s;
        public MyThread(String s1){
            s = s1;
        }
        @Override
        public void run() {
            try {
                Log.i(ConstantCls.LOG_DEBUG_TAG, "mChannel.write sth & " + mChannel2.isOpen()+cf2.isSuccess()+cf2.isVoid());
                mChannel2.writeAndFlush("MSG;TEXT;user2;"+s+"\r\n");
                //mChannel2.read();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    //发送数据
    private void sendMessage2(String msg) {
        MyThread myThread = new MyThread(msg);
        mHandler2.post(myThread);
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
                    bootstrap.handler(new MyClientInitializer(ChatActivity.this,mHandler));
                    //bootstrap.handler(new SimpleChatClientInitializer(mHandler));
                    bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
                    bootstrap.option(ChannelOption.TCP_NODELAY, true);
                    bootstrap.option(ChannelOption.SO_TIMEOUT, 5000);
                    // 连接到本地的7878端口的服务端
                    cf = bootstrap.connect(new InetSocketAddress(
                            ConstantCls.PUSH_SERVER_FOR_CHAT_IP, ConstantCls.PUSH_SERVER_FOR_CHAT_PORT));
                    mChannel = cf.sync().channel();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_send_message:
                String message = editText_sentMessage.getText().toString();
                sendMessage2(message);
                Log.i(ConstantCls.LOG_DEBUG_TAG, "message内容是："+message);
                MessageForChat messageForChat = new MessageForChat();
                messageForChat.setUserId(currentMajorId);
                messageForChat.setMessageContent(message);
                messageList.add(messageForChat);
                myAdapter.notifyDataSetChanged();
                editText_sentMessage.setText("");
                //sendMessage(message);
                break;
            default:
                break;
        }
    }
    //发送数据
    private void sendMessage(final String msg) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                try {

                    Log.i(ConstantCls.LOG_DEBUG_TAG, "mChannel.write sth & " + mChannel.isOpen()+cf.isSuccess()+cf.isVoid());
                    if(!mChannel.isOpen()){
                        mChannel = cf.sync().channel();
                        Log.i(ConstantCls.LOG_DEBUG_TAG, "mChannel.write sth & " + mChannel.isOpen()+cf.isSuccess()+cf.isVoid());
                    }
                    mChannel.writeAndFlush(msg);
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
