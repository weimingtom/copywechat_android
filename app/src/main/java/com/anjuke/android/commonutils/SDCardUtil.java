
package com.anjuke.android.commonutils;

import android.os.Environment;
import android.os.StatFs;

/**
 * SD卡相关工具类
 * 
 * @author 张磊
 */
public class SDCardUtil {

    /**
     * 计算SD卡可用空间
     * 
     * @return 单位KB
     */
    public static long getAvailableSize() {

        String path = Environment.getExternalStorageDirectory().getPath();// 获取SD卡得根路径
        StatFs stat = new StatFs(path);
        long availableBlocks = stat.getAvailableBlocks();// 获取空闲BLOCK数量
        long blockSize = stat.getBlockSize();// 获取BLOCK的大小
        long availableSize = availableBlocks * blockSize;
        // availableSize/1024 单位KB
        // availableSize/1024 /1024 单位MB
        return availableSize / 1024;
    }

    /**
     * 计算SD总空间大小
     * 
     * @return 单位KB
     */
    public static long getAllSize() {

        String path = Environment.getExternalStorageDirectory().getPath();
        StatFs stat = new StatFs(path);
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getBlockCount();
        return availableBlocks * blockSize / 1024;
    }

}
