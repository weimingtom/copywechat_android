
package com.anjuke.android.commonutils;

import java.util.ArrayList;
import java.util.List;

/**
 * 数组和基本类型互转
 * 
 * @author Norika
 * @since 7.0
 * @version 7.0
 */
public class DataTransfer {

    public static String[] longArray2StringArray(long... ls) {
        if (ls == null || ls.length <= 0)
            return null;

        final String[] array = new String[ls.length];
        for (int i = 0; i < ls.length; i++)
            array[i] = Long.toString(ls[i]);

        return array;
    }

    public static List<Long> longArray2LongList(long[] ls) {
        if (ls == null || ls.length <= 0)
            return null;

        List<Long> list = new ArrayList<Long>(ls.length);
        for (long l : ls)
            list.add(l);
        return list;
    }

    public static long[] longList2longArray(List<Long> lList) {
        if (lList == null || lList.size() <= 0)
            return null;

        int size = lList.size();
        long[] ls = new long[size];
        for (int i = 0; i < size; i++)
            ls[i] = lList.get(i);

        return ls;
    }

}
