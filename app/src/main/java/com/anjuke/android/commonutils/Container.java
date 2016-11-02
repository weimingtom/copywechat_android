package com.anjuke.android.commonutils;

import android.app.Application;
import android.content.Context;

public class Container {
    private static Context context;

    public static void setContext(Application app) {
        Container.context = app.getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
