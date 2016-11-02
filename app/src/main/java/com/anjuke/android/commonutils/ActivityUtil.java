
package com.anjuke.android.commonutils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import java.util.List;

/**
 * Activity工具类。Activity间传值等方法。
 */
public class ActivityUtil {

    static final String KEY__FROM_WITCH_ACTIVITY = "fromActivity";

    /**
     * 启动其他activity
     * 
     * @param packageContext
     * @param cls
     */
    public static void libStartActivity(Activity currentActivity, Class<?> cls) {
        libStartActivity(currentActivity, cls, null, null);
    }

    /**
     * 启动其他activity
     * 
     * @param currentActivity
     * @param cls
     * @param flag 与系统中Intent的flag相同
     */
    public static void libStartActivity(Activity currentActivity, Class<?> cls, Integer flag) {
        libStartActivity(currentActivity, cls, new String[] {}, new String[] {}, flag);
    }

    /**
     * 启动其他activity 并传数据
     * 
     * @param currentActivity from activity
     * @param cls to activity
     * @param key 传递数据的每个变量名
     * @param value 传递数据每个变量的值
     */
    public static void libStartActivity(Activity currentActivity, Class<?> cls, String[] key, String[] value) {
        libStartActivity(currentActivity, cls, key, value, null);
    }

    public static void libStartActivity(Activity currentActivity, Class<?> cls, String[] key, String[] value,
                                        Integer flag) {

        if (key != null && value != null && key.length != value.length) {
            throw new RuntimeException("libStartAvtivity()'params key.length is not equal value.length");
        }

        Intent intent = new Intent(currentActivity, cls);

        if (flag != null) {
            intent.setFlags(flag);
        }

        intent.putExtra(KEY__FROM_WITCH_ACTIVITY, currentActivity.getClass().getName());

        if (key != null && value != null && key.length >= 0) {
            for (int i = 0; i < key.length; i++) {
                intent.putExtra(key[i], value[i]);
            }
        }

        currentActivity.startActivity(intent);
    }

    /**
     * 获取activity来源（从哪个acivity跳过来的）<br>
     * <br>
     * 需要：<br>
     * 所有activity的跳转都使用libStartActivity方法<br>
     * 或者activity跳转时自行添加intent.putExtra(“fromActivity”,
     * currentActivity.getClass().getName());
     * 
     * @param currentActivity
     * @return
     */
    public static String libGetFromWhitchActivity(Activity currentActivity) {

        Intent intent = currentActivity.getIntent();

        if (intent.hasExtra(KEY__FROM_WITCH_ACTIVITY)) {
            return intent.getStringExtra(KEY__FROM_WITCH_ACTIVITY);
        } else {
            return "";
        }
    }

    /**
     * 得到传递的变量
     * 
     * @param key
     * @return
     */
    public static String libGetExtra(Activity currentActivity, String key) {

        String ret = null;

        Intent intent = currentActivity.getIntent();
        if (intent.hasExtra(key)) {
            ret = intent.getStringExtra(key);
        }

        return ret;
    }

    /**
     * 得到传递的变量
     * 
     * @param key
     * @return
     */
    public static Integer libGetExtraOptInt(Activity currentActivity, String key) {

        Integer ret = null;
        try {
            ret = Integer.parseInt(libGetExtra(currentActivity, key));
        } catch (Exception e) {
            ret = null;
        }
        return ret;
    }

    /**
     * 得到传递的变量
     * 
     * @param key
     * @return
     */
    public static Double libGetExtraOptDouble(Activity currentActivity, String key) {

        Double ret = null;
        try {
            ret = Double.parseDouble(libGetExtra(currentActivity, key));
        } catch (Exception e) {
            ret = null;
        }
        return ret;
    }

    /**
     * 其他调用startActivity的可走此方法
     * 
     * @param it
     */
    public static void libStartActivity(Activity currentActivity, Intent it) {
        currentActivity.startActivity(it);
    }

    /**
     * 获取栈最顶层的包和activity 名称
     * 
     * @param context
     * @return
     */
    public static String getCurrentActivityName(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Activity.ACTIVITY_SERVICE);
        List<RunningTaskInfo> taskInfo = am.getRunningTasks(1);
        ComponentName componentInfo = taskInfo.get(0).topActivity;
        return componentInfo.getClassName();
    }

    /**
     * 获取当前最底层的activity
     *
     * @return
     */
    public static String getCurrentRootActivityName(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Activity.ACTIVITY_SERVICE);
        List<RunningTaskInfo> taskInfo = am.getRunningTasks(1);
        ComponentName componentInfo = taskInfo.get(0).baseActivity;
        return componentInfo.getClassName();
    }

    /**
     * whether application is in background
     * <ul>
     * <li>need <uses-permission android:name="android.permission.GET_TASKS" />
     * in Manifest.xml</li>
     * </ul>
     * 
     * @param context
     * @return
     */
    public static boolean isApplicationInBackground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> taskList = am.getRunningTasks(1);
        if (taskList != null && !taskList.isEmpty()) {
            ComponentName topActivity = taskList.get(0).topActivity;
            if (topActivity != null && !topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * whether this process is named with processName
     * 
     * @param processName
     * @return
     */
    public static boolean isNamedProcess(Context context, String processName) {
        if (TextUtils.isEmpty(processName)) {
            return true;
        }

        int pid = android.os.Process.myPid();
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> processInfoList = manager.getRunningAppProcesses();
        if (processInfoList == null || processInfoList.size() == 0) {
            return true;
        }

        for (RunningAppProcessInfo processInfo : processInfoList) {
            if (processInfo.pid == pid && processName.equals(processInfo.processName)) {
                return true;
            }
        }
        return false;
    }
}
