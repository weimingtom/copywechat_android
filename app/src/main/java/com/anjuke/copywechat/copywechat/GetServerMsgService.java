package com.anjuke.copywechat.copywechat;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Button;

import com.anjuke.copywechat.copywechat.adapters.MyNewAdapter;
import com.anjuke.copywechat.copywechat.chattest.MyClientInitializer;
import com.anjuke.copywechat.copywechat.model.MessageForChat;
import com.anjuke.copywechat.copywechat.util.ConstantCls;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.net.InetSocketAddress;
import java.util.ArrayList;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class GetServerMsgService extends Service {


    private EventLoopGroup group;
    private MyNewAdapter myAdapter;
    private ArrayList<MessageForChat> messageList;

    private ImageLoader imageLoader;
    private Channel mChannel;
    private ChannelFuture cf;
    private int currentMajorId=1;


    private static final String TAG = "MainActivity";
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
                Log.i(ConstantCls.LOG_DEBUG_TAG,"service 服务器发回数据："+msg.obj.toString());

                String msg2 = msg.obj.toString();
                if(msg2.contains("PUSH")) {
                    Log.i(ConstantCls.LOG_DEBUG_TAG,"PUSH 服务器发回数据："+msg.obj.toString());
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    intent.addFlags(Notification.FLAG_ONGOING_EVENT);

                    // The PendingIntent to launch our activity if the user selects this notification
                    PendingIntent contentIntent = PendingIntent.getActivity(getBaseContext(), 0, intent, 0);
                    // construct the Notification object.

                    NotificationCompat.Builder ncb = new NotificationCompat.Builder(getBaseContext());
                    ncb.setTicker("有服务器消息过来！");
                    ncb.setAutoCancel(true);
                    ncb.setContentIntent(contentIntent);
                    ncb.setDefaults(Notification.DEFAULT_ALL);
                    ncb.setContentTitle("推送消息");
                    ncb.setContentText("推送内容是："+msg2);
                    ncb.setSmallIcon(R.drawable.broker_home_adv_close);

                    // look up the notification manager service
                    NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                    nm.notify(R.string.app_name, ncb.build());
                } else if(msg2.contains("MSG")) {
                    Log.i(ConstantCls.LOG_DEBUG_TAG,"MSG 服务器发回数据："+msg.obj.toString());
                    String str[] = msg2.split(";");
                    Intent intent = new Intent(getApplicationContext(),ChatActivity.class);
                    intent.addFlags(Notification.FLAG_ONGOING_EVENT);
                    intent.putExtra(ConstantCls.USERNAME_FOR_CHATACTIVITY,str[2]);

                    // The PendingIntent to launch our activity if the user selects this notification
                    PendingIntent contentIntent = PendingIntent.getActivity(getBaseContext(), 0, intent, 0);
                    // construct the Notification object.

                    NotificationCompat.Builder ncb = new NotificationCompat.Builder(getBaseContext());
                    ncb.setTicker("有服务器消息过来！");
                    ncb.setAutoCancel(true);
                    ncb.setContentIntent(contentIntent);
                    ncb.setDefaults(Notification.DEFAULT_ALL);
                    ncb.setContentTitle("推送消息");
                    ncb.setContentText("推送内容是："+msg2);
                    ncb.setSmallIcon(R.drawable.broker_home_adv_close);

                    // look up the notification manager service
                    NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                    nm.notify(R.string.app_name, ncb.build());

                }
            }
        }
    };


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    public GetServerMsgService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        connected2();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
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

                    mChannel2.writeAndFlush("SERVICE;user1;1\n");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
