package com.anjuke.copywechat.copywechat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.anjuke.copywechat.copywechat.util.ConstantCls;

public class BootBroadcastReceiver extends BroadcastReceiver {
    public BootBroadcastReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(ConstantCls.LOG_DEBUG_TAG,"开机启动服务！！！");
        Intent intent1 = new Intent(context,GetServerMsgService.class);
        context.stopService(intent1);
    }
}
