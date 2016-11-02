
package com.anjuke.android.commonutils;

public class ITextUtil {

    private ITextUtil() {

    }

    /**
     * 判断字符串是否为空
     * 
     * @param value
     * @return 不为空：true，为空：false
     */
    public static final boolean isValidText(CharSequence cs) {
        return cs != null && cs.toString().trim().length() > 0;
    }

    public static long parseLong(String s, long l_default) {
        try {
            return Long.parseLong(s);
        } catch (NumberFormatException e) {

        }

        return l_default;
    }

}
