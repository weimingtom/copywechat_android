/**
 *
 * Copyright 2013 Anjuke. All rights reserved.
 * StorageUtil.java
 *
 */
package com.anjuke.android.commonutils;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @author willchun (chunwang@anjuke.com)
 */
public class StorageUtil extends ContextWrapper {
    private final String TAG = "StorageUtil";
    public static final long UNAVAILABLE = -1L;
    public static final long PREPARING = -2L;
    public static final long UNKNOWN_SIZE = -3L;
    public static final long AVAILABLE = 1L;

    /**
     * 构造函数
     * 
     * @param base 使用该工具的activity的context环境
     */
    public StorageUtil(Context base) {
    super(base);
}

    /**
     * 创建新文件
     * 
     * @param path 文件的路径
     * @return 新建了文件返回true，文件已经存在返回false
     * @throws IOException IO异常，检查下路径和权限试下
     */
    public boolean createFile(String path) throws IOException {
        File f = new File(path);
        if (!f.getParentFile().exists()) {
            f.getParentFile().mkdirs();
        }
        if (!f.exists()) {
            f.createNewFile();
            return true;
        } else {
            return false;
        }
    }

    /**
     * 检查一个文件是否已经存在于内部存储中
     * 
     * @param fileName 要被检查的文件的文件名
     * @return 文件存在：true,不存在： false
     */
public boolean isInternalFileExists(String fileName){
    String[] files = fileList();
    for(String file: files) {
        if(file.equalsIgnoreCase(fileName)){
            return true;
        }
    }

    return false;
}

    /**
     * 给指定文件名的文件添加数据
     * 
     * @param fileName 要添加数据的文件名
     * @param data 要添加到指定文件中的byte数组
     * @return 操作成功返回true, 其他情况返回false；
     */
public boolean appendToFile(String fileName, byte[] data){
    FileOutputStream fos;
    try{
        fos = openFileOutput(fileName, Context.MODE_APPEND);
        fos.write(data);
        fos.close();
    } catch (Exception e){
        return false;
    }
    return true;
}

    /**
     * 返回给定文件名的内部文件的FileInputStream
     * 
     * @param fileName 文件的文件名
     * @return 文件的FileInputStream
     */
public FileInputStream getInnerFileInputStream(String fileName){
    try{
        if(isInternalFileExists(fileName))
            return openFileInput(fileName);
        return null;
    } catch (FileNotFoundException e) {
        return null;
    }
}

    /**
     * 返回给定文件名的内部文件的FileOutputStream
     * 
     * @param 文件的文件名
     * @return 文件的FileOutputStream
     */
public FileOutputStream getInnerFileOutputStream(String fileName){
    try{
        if(isInternalFileExists(fileName))
            return openFileOutput(fileName, Context.MODE_PRIVATE);
        return null;
    } catch (FileNotFoundException e) {
        return null;
    }
}

    /**
     * 保存一个object对象到内部存储中的一个文件中
     * 
     * @param obj 将被保存的对象
     * @param fileName 对象将被保存到的文件的文件名
     * @return true 保存成功--》true，保存失败--》false。
     */
public boolean saveObjectToInternalStorage(Object obj, String fileName) {
    ObjectOutputStream output = null;
    try {
        output = new ObjectOutputStream( new BufferedOutputStream( openFileOutput(fileName, Context.MODE_PRIVATE) ) );
        output.writeObject(obj);
        output.flush();
        return true;
    } catch (Exception e) {
        e.printStackTrace(System.out);
    } finally {
        if(output!=null)
            try { output.close(); } catch (IOException e) { }
    }
    return false;
}

    /**
     * 从给定文件名的文件中加载之前保存的对象
     * 
     * @param fileName 将被从中加载的文件的文件名
     * @return 从文件中加载到的对象
     */
public Object loadObjectFromInternalStorage(String fileName) {
    Object obj = null;
    ObjectInputStream input = null;
    try {
        input = new ObjectInputStream( new BufferedInputStream( openFileInput(fileName) ) );
        obj = input.readObject();
    } catch (Exception e) {
        e.printStackTrace(System.out);
    } finally {
        if(input!=null)
            try { input.close(); } catch (IOException e) { }
    }
    return obj;
}

    /**
     * 保存一个preference到存储中
     * 
     * @param 配置文件的名字
     * @param valueName 配置文件的key
     * @param value 配置文件的value
     * @return 保存成功返回true，否则返回false。
     */
public boolean savePreference(String preferencesName, String valueName, String value){
      SharedPreferences.Editor editor = getSharedPreferences(preferencesName, Context.MODE_PRIVATE).edit();
      editor.putString(valueName, value);
      return editor.commit();
}

    /**
     * 从存储中加载一个preference
     * 
     * @param 配置文件的名字
     * @param valueName 配置文件的key
     * @return 返回一个包含了之前存储的对应key的preference
     */
public String getPreference(String preferencesName, String valueName){
      return getSharedPreferences(preferencesName, Context.MODE_PRIVATE).getString(valueName, "");
}

    /**
     * 保存一个对象到外部存储的一个文件中
     * 
     * @param obj 将被保存的对象
     * @param directory 在sd卡中的目录
     * @param fileName 在sd卡的目录中的文件名
     * @param overwrite 如果为被设置为true，那么当文件已经存在时候，将被覆盖写入（overwritten）
     * @return 写入成功返回true，否则返回false
     */
public boolean saveObjectToExternalStorage(Object obj, String directory, String fileName, boolean overwrite) {
    if(!directory.startsWith(File.separator))
        directory = File.separator + directory;

    File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + directory);
    if(!dir.exists()) dir.mkdirs();
    
    File file = new File(dir, fileName);
    if(file.exists() && !overwrite)
        return false;
    ObjectOutputStream output = null;
    try {
        output = new ObjectOutputStream( new BufferedOutputStream( new FileOutputStream(file) ) );
        output.writeObject(obj);
        output.flush();
        return true;
    } catch (Exception e) {
        e.printStackTrace(System.out);
    } finally {
        if(output!=null)
            try { output.close(); } catch (IOException e) { }
    }
    return false;
}

    /**
     * 从外部存储的文件中加载一个对象
     * 
     * @param fileName 之前对象保存到的文件（的文件名）
     * @return 文件中的对象
     */
public Object loadObjectFromExternalStorage(String fileName) {
    if(!fileName.startsWith(File.separator))
        fileName = File.separator + fileName;
    
    File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + fileName);
    Object obj = null;
    ObjectInputStream input = null;
    try {
        input = new ObjectInputStream( new BufferedInputStream( new FileInputStream(file) ) );
        obj = input.readObject();
    } catch (Exception e) {
        e.printStackTrace(System.out);
    } finally {
        if(input!=null)
            try { input.close(); } catch (IOException e) { }
    }
    return obj;
}

    /**
     * 保存给出的string到外部存储中的一个文件中
     * 
     * @param obj 将被保存的string对象
     * @param directory 保存到的目录
     * @param fileName 文件的名字
     * @param overwrite 如果为被设置为true，那么当文件已经存在时候，将被覆盖写入（overwritten）
     * @return 写入成功返回true, 否则返回false
     */
    public boolean saveStringToExternalStorage(String obj, String directory, String fileName,
                                               boolean overwrite) {
        if (!directory.startsWith(File.separator))
            directory = File.separator + directory;

        File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + directory);
        if (!dir.exists())
            dir.mkdirs();

        File file = new File(dir, fileName);
        if (file.exists() && !overwrite)
            return false;
        BufferedOutputStream output = null;
        try {
            output = new BufferedOutputStream(new FileOutputStream(file));
            output.write(obj.getBytes());
            output.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace(System.out);
        } finally {
            if (output != null)
                try {
                    output.close();
                } catch (IOException e) {
                }
        }
        return false;
    }

    /**
     * 返回一个外部存储中文件的FileOutputStream
     * 
     * @param fileName 文件的文件名
     * @return 文件的一个FileOutputStream
     */
public FileInputStream getExternalFileInputStream(String fileName){
    if(!fileName.startsWith(File.separator))
        fileName = File.separator + fileName;
    
    File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + fileName);
    try {
        return new FileInputStream(file) ;
    } catch (Exception e) {
        e.printStackTrace(System.out);
    }
    return null;
}

    /**
     * 返回一个外部存储中文件的FileOutputStream
     * @param fileName 文件的文件名
     * @return 文件的一个FileOutputStream
     */
public FileOutputStream getExternalFileOutputStream(String fileName){
    if(!fileName.startsWith(File.separator))
        fileName = File.separator + fileName;
    
    File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + fileName);
    try {
        return new FileOutputStream(file) ;
    } catch (Exception e) {
        e.printStackTrace(System.out);
    }
    return null;
}

    /**
     * 检测是不是有外部存储
     * @param requireWriteAccess 是不是需要写入
     * @return 有外部存储就返回true，如果需要写入（requireWriteAccess
     *         传入true），将同时检测是不是拥有写入权限。如果media没有被挂载，或者只是 以只读的方式挂载，那么返回false。
     */
public static boolean hasExternalStorage(boolean requireWriteAccess) {
    String state = Environment.getExternalStorageState();

    if (Environment.MEDIA_MOUNTED.equals(state)) {
        return true;
    } else if (!requireWriteAccess && Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
        return true;
    }
    return false;
}

    /**
     * 检查外部存储文件是否存在
     * 
     * @param fileName 比如 /anjuke/propfile.anj对应的就是SD卡根目录看到的anjuke目录下的文件
     * @return 存在返回true，否则返回false
     */
    public boolean isExternalFileExists(String fileName) {
        File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + fileName);
        if (dir.exists()) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * 检测外部存储的状态，sd卡的状态
     * 
     * @return 共有三种状态：UNAVAILABLE、PREPARING、AVAILABLE<br>
     *         均为StorageUtil的静态final字段
     */
    public long checkExternalStorageState() {
        String state = Environment.getExternalStorageState();
        Log.d(TAG, "External storage state=" + state);
        if (Environment.MEDIA_CHECKING.equals(state)) {
            return PREPARING;
        }
        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            return UNAVAILABLE;
        }

        File dir = Environment.getExternalStorageDirectory();
        dir.mkdirs();
        if (!dir.isDirectory() || !dir.canWrite()) {
            return UNAVAILABLE;
        }
        return AVAILABLE;
        
    }

    /**
     * 获得可用的外部存储的空间大小
     * 
     * @return 如果外部存储状态为正常，返回外部剩余空间大小,单位b，需要你自己转换为kb，mb，gb<br>
     *         如果sd卡挂载异常，返回checkExternalStorageState中的sd卡状态值
     *         （负值）UNAVAILABLE或者PREPARING
     */
    public long getAvailableExternalSpace() {
        Long state = checkExternalStorageState();
        if (state != AVAILABLE) {
            return state;
        }
        try {
            StatFs stat = new StatFs(Environment.getExternalStorageDirectory().toString());
            Log.v(TAG, Environment.getExternalStorageDirectory().getPath());
            Log.v(TAG, Environment.getExternalStorageDirectory().getAbsolutePath());
            Log.v(TAG, Environment.getExternalStorageDirectory().getCanonicalPath());
            Log.v(TAG, Environment.getExternalStorageDirectory().getName());
            Log.v(TAG, Environment.getExternalStorageDirectory().toString());
            return stat.getAvailableBlocks() * (long) stat.getBlockSize();
        } catch (Exception e) {
            Log.i(TAG, "Fail to access external storage", e);
        }
        return UNKNOWN_SIZE;
    }

    /**
     * @return 系统可用内部空间，单位b，需要你自己转换kb，mb，gb
     */
    public long getAvailableInternalSpace() {
        File root = Environment.getRootDirectory();
        StatFs stat = new StatFs(root.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return blockSize * availableBlocks;
    }

}
