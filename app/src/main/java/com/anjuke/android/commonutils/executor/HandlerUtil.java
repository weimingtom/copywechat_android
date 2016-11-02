package com.anjuke.android.commonutils.executor;

import android.os.Handler;
import android.os.Looper;

public class HandlerUtil {
    private static Handler handler = new Handler(Looper.getMainLooper());

    public static void post(Runnable runnable) {
        handler.post(runnable);
    }
}
