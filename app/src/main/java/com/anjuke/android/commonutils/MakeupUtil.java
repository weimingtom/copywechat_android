
package com.anjuke.android.commonutils;

import android.annotation.TargetApi;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.text.TextPaint;
import android.widget.TextView;

/**
 * 系统补丁修正工具类
 * 
 * @author Norika
 * @since 8.0 2014.09.12
 * @version 8.0 2014.09.16
 */
public class MakeupUtil {

    public static void boldTextView(TextView tv) {
        TextPaint tp = tv.getPaint();

        if (!DevUtil.hasHoneycomb() || !tp.isFakeBoldText())
            tp.setFakeBoldText(true);
    }

    @TargetApi(VERSION_CODES.HONEYCOMB_MR1)
    public static String getBundleString(Bundle b, String key, String defaultValue) {
        if (DevUtil.hasHoneycombMR1())
            return b.getString(key, defaultValue);

        String value = b.getString(key);
        return value == null ? defaultValue : value;
    }

}
