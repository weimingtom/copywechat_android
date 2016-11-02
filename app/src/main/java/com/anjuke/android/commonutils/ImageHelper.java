
package com.anjuke.android.commonutils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.os.NetworkOnMainThreadException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

/**
 * 图片工具类
 */
public class ImageHelper {
    private Context mContext;
    private static ImageHelper _instance = null;

    /** 缓存ExifInterface对象，提高速度 */
    private HashMap<String, ExifInterface> mExifMap = new HashMap<String, ExifInterface>();
    private ExifInterface mExif = null;

    public synchronized static ImageHelper getInstance(Context context) {

        if (_instance == null) {
			_instance = new ImageHelper(context.getApplicationContext());
        }
        return _instance;
    }

    private ImageHelper(Context context) {
        mContext = context;
        DevUtil.initialize(context);
    }

    /**
     * 重要：本方法中使用了sdk level5的特性。使用本方法，需要保证你的app中android:minSdkVersion<br>
     * 初始化 <br>
     * 
     * @param filePath 图片地址
     * @return
     */
    @TargetApi(5)
    public ImageHelper initExif(String filePath) {

        if (DevUtil.hasAndroid2_0()) {

            mExif = mExifMap.get(filePath);

            try {
                if (mExif == null) {
                    mExif = new ExifInterface(filePath);
                    mExifMap.put(filePath, mExif);
                }
            } catch (Exception e) {
                throw new RuntimeException(String.valueOf(e));
            }

        }

        return _instance;
    }

    /**
     * 重要：本方法中使用了sdk level5的特性。使用本方法，需要保证你的app中android:minSdkVersion<br>
     * 获取exif信息
     * 
     * @param tag
     * @return
     */

    @TargetApi(5)
    public String getExifValue(String tag) {

        if (DevUtil.hasAndroid2_0()) {
            return mExif.getAttribute(tag);
        } else {
            return "";
        }

    }

    /**
     * 重要：本方法中使用了sdk level5的特性。使用本方法，需要保证你的app中android:minSdkVersion<br>
     * 设置exif信息.commitExif()后保存
     * 
     * @param tag
     * @param value
     * @return
     */
    @TargetApi(5)
    public ImageHelper setExifValue(String tag, String value) {
        if (DevUtil.hasAndroid2_0()) {
            mExif.setAttribute(tag, value);
        }
        return _instance;
    }

    /**
     * 重要：本方法中使用了sdk level5的特性。使用本方法，需要保证你的app中android:minSdkVersion<br>
     * 提交保存exif信息 需要权限uses-permission
     * android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
     */

    @TargetApi(5)
    public void commitExif() {
        try {
            if (DevUtil.hasAndroid2_0()) {
                mExif.saveAttributes();
            }
        } catch (IOException e) {
            throw new RuntimeException(String.valueOf(e));
        }
    }

    /**
     * 保存bitmap到sd卡
     * 
     * @deprecated 不建议public使用
     * @param bitmap 待保存的bitmap
     * @param string 待保存的目录路径
     * @param fileName 待保存的文件名
     * @return 图片路径或者null
     */
    public String saveImage2Local(Bitmap bitmap, String string, String fileName) {
        return saveImage2Local(bitmap, string, fileName, 100);
    }

    /**
     * 保存bitmap到sd卡
     * 
     * @deprecated 不建议public使用
     * @param bitmap 待保存的bitmap
     * @param path 待保存的目录路径
     * @param fileName 待保存的文件名
     * @param compress 图片压缩率
     * @return 图片路径或者null
     */
    public String saveImage2Local(Bitmap bitmap, String path, String fileName, int compress) {

        File imagePath = null;
        try {
            if (bitmap != null && !bitmap.isRecycled()) {

                File imgDir = new File(path);
                if (!imgDir.exists()) {// 如果存储的不存在，先创建
                    imgDir.mkdirs();
                }

                imagePath = new File(path, fileName);// 给新照的照片文件命名

                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(imagePath));

                /* 采用压缩转档方法 */
                bitmap.compress(Bitmap.CompressFormat.JPEG, compress, bos);

                /* 调用flush()方法，更新BufferStream */
                bos.flush();

                /* 结束OutputStream */
                bos.close();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (imagePath != null && bitmap != null) {
            return imagePath.toString();
        } else {
            return null;
        }

    }

    /**
     * @deprecated 不建议public使用
     *             对应操作均可用loadImage、isDiskCached、diskCachedFilePath方法搞定
     * @param filePathOrUrl
     * @param maxWidth
     * @param maxHeight
     * @return
     */
    public String getFilePathOfInSampleBitmapUseFilePathOrUrl(String filePathOrUrl, int maxWidth, int maxHeight) {

        return getFilePathOfInSampleBitmapUseFilePathOrUrl(filePathOrUrl, maxWidth, maxHeight, null);
    }

    /**
     * 得到网络或本地已经略缩图片的缓存在本地的图片path
     *
     * @deprecated 不建议public使用
     *             对应操作均可用loadImage、isDiskCached、diskCachedFilePath方法搞定
     * @param filePathOrUrl
     * @param maxWidth
     * @param maxHeight
     * @param cachePath 指定缓存图片的路径 路径结尾不要带"/"
     * @return
     */
    public String getFilePathOfInSampleBitmapUseFilePathOrUrl(String filePathOrUrl, int maxWidth, int maxHeight,
                                                              String cachePath) {

        if (cachePath == null) {
            cachePath = mContext.getCacheDir().getAbsolutePath();
        }

        String ret = null;

        String filePath = "";
        if (filePathOrUrl.startsWith("http")) {
            filePath = url2Md5FilePath(filePathOrUrl);
        } else {
            filePath = filePathOrUrl;
        }

        final String bitmapKey = getInSampleBitmapFileNameKey(filePath, maxWidth, maxHeight);
        final String bitmapFileNameMd5 = url2Md5FileName(bitmapKey);

        if (isFileNameInCache(bitmapFileNameMd5, cachePath)) {// 已经有缓存
            ret = cachePath + "/" + bitmapFileNameMd5;
        }

        return ret;
    }

    /**
     * 重要：本方法中使用了sdk
     * level8的特性。使用本方法，需要保证你的app中android:minSdkVersion大于等于8，否则在低版本中会异常
     * 获得按比例的略缩图，图片为指定大小（先缩放再裁剪）
     *
     * @param filePathOrUrl
     * @param cutWidth
     * @param cutHeight
     * @return
     */
    @SuppressWarnings("unused")
    private Bitmap getInSampleAndCutBitmap(String filePathOrUrl, int cutWidth, int cutHeight) {
        return getInSampleAndCutBitmap(filePathOrUrl, cutWidth, cutHeight, null);
    }

    /**
     * 重要：本方法中使用了sdk
     * level8的特性。使用本方法，需要保证你的app中android:minSdkVersion大于等于8，否则在低版本中会异常
     * 获得按比例的略缩图，图片为指定大小（先缩放再裁剪）
     *
     * @param filePathOrUrl
     * @param cutWidth
     * @param cutHeight
     * @param cachePath
     * @return
     */
    @TargetApi(8)
    private Bitmap getInSampleAndCutBitmap(String filePathOrUrl, int cutWidth, int cutHeight, String cachePath) {

        if (!DevUtil.hasFroyo()) {// 小于2.2的系统无法使用
            return BitmapFactory.decodeByteArray(new byte[] {}, 0, 0);
        }

        if (cachePath == null) {
            cachePath = mContext.getCacheDir().getAbsolutePath();
        }

        Bitmap ret = null;

        // 缓存命中
        // 缓存 在特殊情况会有问题,建议每次拍照生成不同图片: 拍照在本地循环使用同一张照片保存。
        // 会产生问题，后续拍照生成的略缩图因为filePath全是返回的缓存的第一张图
        if (isDiskCached(filePathOrUrl, cutWidth, cutHeight, cachePath, true)) {
            String filePath = diskCachedFilePath(filePathOrUrl, cutWidth, cutHeight, cachePath, true);
            DevUtil.v("jackzhou", String.format("DiskCache hit(InSampleAndCut) path:%s", filePath));
            return BitmapFactory.decodeFile(filePath);
        }

        if (filePathOrUrl.startsWith("http")) {// 线上图片

            if (cutWidth == 0 && cutHeight == 0) {// 都不限制？
                ret = loadImage(filePathOrUrl);
            } else {

                Bitmap bitmap = loadImage(filePathOrUrl);
                ret = ThumbnailUtils.extractThumbnail(bitmap, cutWidth, cutHeight);

                if (bitmap != null && !bitmap.isRecycled()) {
                    bitmap.recycle();
                    bitmap = null;
                }
            }

        } else {// 本地图片

            if (cutWidth == 0 && cutHeight == 0) {// 都不限制？
                ret = BitmapFactory.decodeFile(filePathOrUrl);
            } else {
                Bitmap bitmap = BitmapFactory.decodeFile(filePathOrUrl);
                ret = ThumbnailUtils.extractThumbnail(bitmap, cutWidth, cutHeight);

                if (bitmap != null && !bitmap.isRecycled()) {
                    bitmap.recycle();
                    bitmap = null;
                }

            }
        }

        // 缓存ret图片 不放入线程。在线程中，一些功能需要马上使用得到对应缓存图片做操作会有问题
        String bitmapFileNameMd5 = getBitmapFileNameMd5(filePathOrUrl, cutWidth, cutHeight, cachePath, true);
        saveImage2Local(ret, cachePath, bitmapFileNameMd5);

        return ret;
    }

    /**
     * 略缩图本地缓存时的文件名算法
     *
     * @deprecated 不建议public使用
     *             对应操作均可用loadImage、isDiskCached、diskCachedFilePath方法搞定
     * @param filePath
     * @param maxWidth
     * @param maxHeight
     * @return
     */
    public static String getInSampleBitmapFileNameKey(String filePath, int maxWidth, int maxHeight) {
        return "" + maxWidth + maxHeight + filePath;// filePatch需要放在最后为了适用url2Md5FileName方法
    }

    /**
     * @deprecated 不建议public使用
     *             对应操作均可用loadImage、isDiskCached、diskCachedFilePath方法搞定
     * @param filePath
     * @param maxWidth
     * @param maxHeight
     * @return
     */
    public Bitmap getInSampleBitmap(String filePath, int maxWidth, int maxHeight) {

        return getInSampleBitmap(filePath, maxWidth, maxHeight, null);
    }

    // http://stackoverflow.com/questions/2641726/decoding-bitmaps-in-android-with-the-right-size
    /**
     * 取得图片的略缩图<br>
     * <br>
     * 缓存算法：<br>
     * 以字符串maxWidth + maxHeight + filePatch拼接字符串Md5为缓存文件名<br>
     * 缓存在mContext.getCacheDir()目录中,下次取直接可取得缓存图片。<br>
     *
     * @deprecated 不建议public使用
     *             对应操作均可用loadImage、isDiskCached、diskCachedFilePath方法搞定
     * @param filePath
     * @param maxWidth 0表示不限
     * @param maxHeight 0表示不限
     * @param cachePath 指定缓存图片的路径 路径结尾不要带"/"
     * @return
     */
    public Bitmap getInSampleBitmap(String filePath, int maxWidth, int maxHeight, String cachePath) {

        if (cachePath == null) {
            cachePath = mContext.getCacheDir().getAbsolutePath();
        }

        Bitmap ret = null;

        final String bitmapKey = getInSampleBitmapFileNameKey(filePath, maxWidth, maxHeight);
        final String bitmapFileNameMd5 = url2Md5FileName(bitmapKey);
        // 缓存 在特殊情况会有问题,建议每次拍照生成不同图片: 拍照在本地循环使用同一张照片保存。
        // 会产生问题，后续拍照生成的略缩图因为filePath全是返回的缓存的第一张图
        if (isFileNameInCache(bitmapFileNameMd5, cachePath)) {// 已经有缓存
            DevUtil.v("jackzhou", String.format("DiskCache hit(InSample) path:%s/%s", cachePath, bitmapFileNameMd5));
            return BitmapFactory.decodeFile(cachePath + "/" + bitmapFileNameMd5);
        }

        if (maxWidth == 0 && maxHeight == 0) {// 都不限制？
            ret = BitmapFactory.decodeFile(filePath);
        } else {
            if (maxWidth == 0) {
                maxWidth = -1;
            }
            if (maxHeight == 0) {
                maxHeight = -1;
            }

            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(filePath, opts);
            if (opts.outWidth == -1) {// error
                return null;
            }
            int width = opts.outWidth;// 图片宽
            int height = opts.outHeight;// 图片高
            if (maxWidth >= width && maxHeight >= height) {// 略缩图比原图还大？！！
                ret = BitmapFactory.decodeFile(filePath);
            } else {
                // 计算到maxWidth的压缩比
                float inSampleSizeWidthFloat = (float) width / (float) maxWidth;
                int inSampleSizeWidth = Math.round(inSampleSizeWidthFloat);
                // 计算到maxHeight的压缩比
                float inSampleSizeHeightFloat = (float) height / (float) maxHeight;
                int inSampleSizeHeight = Math.round(inSampleSizeHeightFloat);

                int inSampleSize = Math.max(inSampleSizeWidth, inSampleSizeHeight);

                opts.inJustDecodeBounds = false;
                opts.inSampleSize = inSampleSize;
                ret = BitmapFactory.decodeFile(filePath, opts);
            }
        }

        // //缓存ret图片
        // new AsyncTask<Bitmap, Void, Void>(){
        //
        // @Override
        // protected Void doInBackground(Bitmap... params) {
        // Bitmap bitmap = params[0];
        // saveImage2Local(bitmap, mContext.getCacheDir().getAbsolutePath(),
        // bitmapFileNameMd5);
        // return null;
        // }
        //
        // }.execute(ret);

        // 缓存ret图片 不放入线程。在线程中，一些功能需要马上使用得到对应缓存图片做操作会有问题
        saveImage2Local(ret, cachePath, bitmapFileNameMd5);

        return ret;
    }

    /**
     * @deprecated 不建议public使用
     *             对应操作均可用loadImage、isDiskCached、diskCachedFilePath方法搞定
     * @param url
     * @param maxWidth
     * @param maxHeight
     * @return
     */
    public Bitmap getRemotePicInSampleBitmap(String url, int maxWidth, int maxHeight) {

        return getRemotePicInSampleBitmap(url, maxWidth, maxHeight, null);
    }

    /**
     * 取得图片的略缩图
     *
     * @deprecated 不建议public使用
     *             对应操作均可用loadImage、isDiskCached、diskCachedFilePath方法搞定
     * @param url 图片地址
     * @param maxWidth
     * @param maxHeight
     * @param cachePath 指定缓存图片的路径 路径结尾不要带"/"
     * @return
     */
    public Bitmap getRemotePicInSampleBitmap(String url, int maxWidth, int maxHeight, String cachePath) {

        // 判断是否有缓存
        if (isDiskCached(url, maxWidth, maxHeight, cachePath)) {
            String filePath = diskCachedFilePath(url, maxWidth, maxHeight, cachePath);
            DevUtil.v("jackzhou", String.format("DiskCache hit(InSample) path:%s", filePath));
            return BitmapFactory.decodeFile(filePath);
        }

        // 未缓存则先去网络上下载图片
        Bitmap temp = loadImage(url);
        if (temp != null && !temp.isRecycled()) {
            temp.recycle();
        }

        // 缩放下载后的图片
        return getInSampleBitmap(url2Md5FilePath(url), maxWidth, maxHeight, cachePath);
    }

    /**
     * @deprecated 不建议public使用
     *             对应操作均可用loadImage、isDiskCached、diskCachedFilePath方法搞定
     * @param filePathOrUrl
     * @param maxWidth
     * @param maxHeight
     * @return
     */
    public Bitmap getInSampleBitmapUseFilePathOrUrl(String filePathOrUrl, int maxWidth, int maxHeight) {

        return getInSampleBitmapUseFilePathOrUrl(filePathOrUrl, maxWidth, maxHeight, null);
    }

    /**
     * 获取网络或本地的图片的略缩图
     *
     * @deprecated 不建议public使用
     *             对应操作均可用loadImage、isDiskCached、diskCachedFilePath方法搞定
     * @param filePathOrUrl
     * @param maxWidth
     * @param maxHeight
     * @param cachePath 指定缓存图片的路径 路径结尾不要带"/"
     * @return
     */
    public Bitmap getInSampleBitmapUseFilePathOrUrl(String filePathOrUrl, int maxWidth, int maxHeight, String cachePath) {
        Bitmap ret = null;

        if (filePathOrUrl.startsWith("http")) {
            ret = getRemotePicInSampleBitmap(filePathOrUrl, maxWidth, maxHeight, cachePath);
        } else {
            ret = getInSampleBitmap(filePathOrUrl, maxWidth, maxHeight, cachePath);
        }

        return ret;
    }

    /**
     * 载入网站上图片保存在app的cache目录中
     *
     * @param filePathOrUrl 图片filePathOrUrl
     * @return
     */
    public Bitmap loadImage(String filePathOrUrl) {

        if (filePathOrUrl.startsWith("http")) {
            return loadBitmapUseSDCard(filePathOrUrl);
        } else {
            DevUtil.v("jackzhou", String.format("DiskCache hit(Nomal) path:%s", filePathOrUrl));
            return BitmapFactory.decodeFile(filePathOrUrl);
        }
    }

    /**
     * 载入手机本地路径的图片
     *
     * @deprecated 不建议public使用
     *             对应操作均可用loadImage、isDiskCached、diskCachedFilePath方法搞定
     * @param path 图片路径
     * @return
     */
    public Bitmap loadImageUsePath(String path) {
        return getInSampleBitmap(path, 0, 0);
    }

    /**
     * 判断文件是否存在
     *
     * @deprecated 不建议public使用
     * @param path
     * @return
     */
    public boolean isFileExist(String path) {

        if (path == null || path.trim().length() == 0) {
            return false;
        }

        File file = new File(path);
        if (file.exists()) {
            return true;
        } else {
            return false;
        }
    }

    // 不对外提供。无法控制图片回收会造成out of memory的crash
    // /**
    // * 线程中载入网站上图片设置到imageview中
    // * @param imageView
    // * @param url
    // */
    // public void loadImageUseSDCardInThread(final ImageView imageView, String
    // url){
    // if(url == null || url.length() == 0){
    // return;
    // }
    //
    // new AsyncTask<Object, Void, Bitmap>(){
    // ImageView imageView;
    // String url;
    // @Override
    // protected Bitmap doInBackground(Object... params) {
    // url = (String) params[0];
    // imageView = (ImageView) params[1];
    // return loadBitmapUseSDCard(url);
    // }
    //
    // protected void onPostExecute(Bitmap result) {
    // imageView.setImageBitmap(result);
    // }
    //
    // }.execute(url, imageView);
    // }

    /**
     * 判断以前下载的图片是否在app的cache中
     *
     * @deprecated 不建议public使用
     *             对应操作均可用loadImage、isDiskCached、diskCachedFilePath方法搞定
     * @param url
     * @return
     */
    public boolean isBitmapInSDCard(String url) {
        String fileName = url2Md5FileName(url);
        if (fileName == null) {
            return false;
        }

        File file = new File(mContext.getCacheDir(), fileName);// 保存文件,
        if (file.exists()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断图片是否在app的cache目录（ mContext.getCacheDir() ）中
     *
     * @deprecated 不建议public使用
     *             对应操作均可用loadImage、isDiskCached、diskCachedFilePath方法搞定
     * @param fileName
     * @param cachePath
     * @return
     */
    public boolean isFileNameInCache(String fileName) {
        return isFileNameInCache(fileName, null);
    }

    /**
     * 判断图片是否在指定的cache目录中
     *
     * @deprecated 不建议public使用
     *             对应操作均可用loadImage、isDiskCached、diskCachedFilePath方法搞定
     * @param
     * @return
     */
    public boolean isFileNameInCache(String fileName, String cachePath) {

        if (cachePath == null) {
            cachePath = mContext.getCacheDir().getAbsolutePath();
        }

        if (fileName == null) {
            return false;
        }

        File file = new File(cachePath, fileName);
        if (file.exists() && file.length() > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 取得以前下载图片在本地保存的文件路径
     *
     * @deprecated 不建议public使用
     *             对应操作均可用loadImage、isDiskCached、diskCachedFilePath方法搞定
     * @param url
     * @return
     */
    public String getBitmapPath(String url) {
        String ret = null;

        if (!isBitmapInSDCard(url)) {
            return null;
        }

        String fileName = url2Md5FileName(url);
        if (fileName == null) {
            return null;
        }

        ret = mContext.getCacheDir() + "/" + fileName;
        return ret;
    }

    /**
     * bitmap转换为byte[]
     *
     * @param bitmap
     * @return
     */
    public byte[] bitmap2Byte(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * 字节转换为bitmap
     *
     * @param data
     * @return
     */
    public Bitmap byte2Bitmap(byte[] data) {
        return BitmapFactory.decodeByteArray(data, 0, data.length);
    }

    /**
     * 把字节数组保存为一个文件
     *
     * @param b
     * @param outputFile
     * @return
     */
    public File byte2File(byte[] b, String outputFile) {
        BufferedOutputStream stream = null;
        File file = null;
        try {
            file = new File(outputFile);
            FileOutputStream fstream = new FileOutputStream(file);
            stream = new BufferedOutputStream(fstream);
            stream.write(b);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return file;
    }

    /**
     * 转化url为对应md5的文件名
     *
     * @deprecated 不建议public使用
     *             对应操作均可用loadImage、isDiskCached、diskCachedFilePath方法搞定
     * @param url
     * @return
     */
    public static String url2Md5FileName(String url) {
        String ret = null;

        String tempfileNameExt = null;
        if (url == null || url.length() == 0) {
            return null;
        }
        if (url.lastIndexOf("/") != -1) {
            String tempFileName = url.substring(url.lastIndexOf("/") + 1);
            if (tempFileName.lastIndexOf(".") != -1) {
                tempfileNameExt = tempFileName.substring(tempFileName.lastIndexOf(".") + 1);
                ret = MD5Util.Md5(url) + "." + tempfileNameExt;
            } else {
                ret = MD5Util.Md5(url) + ".jpg";
            }
        }

        return ret;
    }

    /**
     * 获得改url使用loadimage方法生成图片的本地路径地址 例：/data/data/com.abc/airakjv.jpg
     *
     * @deprecated 不建议public使用
     * @param url
     * @return
     */
    public String url2Md5FilePath(String url) {
        String ret = null;

        String fileName = url2Md5FileName(url);
        if (fileName == null) {
            return null;
        }

        ret = mContext.getCacheDir() + "/" + fileName;
        return ret;
    }

    /**
     * 通过url从网上拿图并保存为手机上图片文件。下次直接本地读取。 本地文件名为url的md5编码
     *
     * @param url
     * @return
     */
    @SuppressLint("NewApi")
    private Bitmap loadBitmapUseSDCard(String url) {
        Bitmap ret = null;
        String fileName = url2Md5FileName(url);

        if (fileName == null) {
            return null;
        }

        String imagePath = mContext.getCacheDir() + "/" + fileName;
        File file = new File(mContext.getCacheDir(), fileName);// 保存文件,
        if (!file.exists()) {
            FileOutputStream fileOutputStreamTemp = null;
            try {
                byte[] data = loadImageFromNetwork(url);
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

                String imageType = getImageType(data);
                fileOutputStreamTemp = new FileOutputStream(file);
                if (!imageType.equalsIgnoreCase("jpg")) {
                    // 判断是否png 避免透明背景被弄成黑色
                    bitmap.compress(CompressFormat.PNG, 100, fileOutputStreamTemp);
                } else {
                    bitmap.compress(CompressFormat.JPEG, 60, fileOutputStreamTemp);
                }
                imagePath = file.getAbsolutePath();
                if (!bitmap.isRecycled()) {// 图片存成文件后，直接回收掉. 防止out of memory
                    bitmap.recycle();
                }
            } catch (Exception e) {
                e.printStackTrace();

                if (DevUtil.hasHoneycomb()) {
                    if (NetworkOnMainThreadException.class.isInstance(e)) {
                        throw new RuntimeException("图片下载等耗时操作不允许在主进程中使用...");
                    }
                }

            } finally {
                try {
                    if (fileOutputStreamTemp != null) {
                        fileOutputStreamTemp.close();
                        fileOutputStreamTemp = null;
                    }
                } catch (Exception e1) {
                    // ignore
                }
            }
        } else {
            DevUtil.v("jackzhou", String.format("DiskCache hit(Nomal) path:%s", imagePath));
        }

        ret = BitmapFactory.decodeFile(imagePath);
        return ret;
    }

    /**
     * 从网站上下载得到图片对应byte[]
     *
     * @deprecated 不建议外部使用 加载图片可以直接使用loadImage
     * @param url
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public byte[] loadImageFromNetwork(String url) throws ClientProtocolException, IOException {
        HttpGet method = new HttpGet(url);
        HttpResponse response = HttpUtil.getHttpClient().execute(method);
        HttpEntity entity = response.getEntity();
        BufferedInputStream in = new BufferedInputStream(entity.getContent());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[8192];
        while (true) {
            int len = in.read(buf);
            if (len < 0) {
                break;
            }
            out.write(buf, 0, len);
        }
        out.close();
        in.close();
        return out.toByteArray();
    }

    /**
     * 判断图片类型
     *
     * @param b
     * @return
     */
    public String getImageType(byte[] b) {
        if (b.length < 10) {
            return "Unknown";
        }
        if (b[0] == (byte) 'G' && b[1] == (byte) 'I' && b[2] == (byte) 'F') {
            return "gif";
        } else if (b[1] == (byte) 'P' && b[2] == (byte) 'N' && b[3] == (byte) 'G') {
            return "png";
        } else if (b[6] == (byte) 'J' && b[7] == (byte) 'F' && b[8] == (byte) 'I' && b[9] == (byte) 'F') {
            return "jpg";
        } else {
            return "Unknown";
        }
    }

    public Bitmap Rotate(Bitmap bitmap, float degrees) {

        // set rotate
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        // Setting post rotate to 90
        Matrix mtx = new Matrix();
        mtx.postRotate(degrees);

        // Rotating Bitmap
        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
    }

    /**
     * 获取目录下文件个数
     *
     * @deprecated
     * @param path
     * @return
     */
    public int getFilesCount(String path) {

        int fileCount = 0;

        File d = new File(path);
        File list[] = d.listFiles();
        for (int i = 0; i < list.length; i++) {
            if (list[i].isFile()) {
                fileCount++;
            }
        }

        return fileCount;
    }

    /**
     * 判断图片是否已经磁盘缓存
     *
     * @param filePathOrUrl
     *            图片的url或者加载的最原始的本地路径(http://a.com/a.jpg或者/data/data/xxx
     *            .com/cache/a.jpg)
     * @return
     */
    public boolean isDiskCached(String filePathOrUrl) {
        return isDiskCached(filePathOrUrl, 0, 0);
    }

    /**
     * 判断图片是否已经磁盘缓存
     *
     * @param filePathOrUrl
     *            图片的url或者加载的最原始的本地路径(http://a.com/a.jpg或者/data/data/xxx
     *            .com/cache/a.jpg)
     * @param width 图片在调用缩放裁剪方法时传入的width
     * @param height 图片在调用缩放裁剪方法时传入的height
     * @return
     */
    public boolean isDiskCached(String filePathOrUrl, int width, int height) {
        return isDiskCached(filePathOrUrl, width, height, null);
    }

    /**
     * 判断图片是否已经磁盘缓存
     *
     * @param filePathOrUrl
     *            图片的url或者加载的最原始的本地路径(http://a.com/a.jpg或者/data/data/xxx
     *            .com/cache/a.jpg)
     * @param width 图片在调用缩放裁剪方法时传入的width
     * @param height 图片在调用缩放裁剪方法时传入的height
     * @param cachePath 图片在调用缩放裁剪时传入的自定义磁盘缓存cache路径（/sdcard/000）路径结尾不要带"/"
     * @return
     */
    public boolean isDiskCached(String filePathOrUrl, int width, int height, String cachePath) {
        return isDiskCached(filePathOrUrl, width, height, cachePath, false);
    }

    /**
     * 判断图片是否已经磁盘缓存
     *
     * @param filePathOrUrl
     *            图片的url或者加载的最原始的本地路径(http://a.com/a.jpg或者/data/data/xxx
     *            .com/cache/a.jpg)
     * @param width 图片在调用缩放裁剪方法时传入的width
     * @param height 图片在调用缩放裁剪方法时传入的height
     * @param cachePath 图片在调用缩放裁剪时传入的自定义磁盘缓存cache路径（/sdcard/000）路径结尾不要带"/"
     * @param isNeedCut 图片是否裁剪
     * @return
     */
    public boolean isDiskCached(String filePathOrUrl, int width, int height, String cachePath, boolean isNeedCut) {

        boolean ret = false;

        String bitmapFileNameMd5 = getBitmapFileNameMd5(filePathOrUrl, width, height, cachePath, isNeedCut);
        ret = isFileNameInCache(bitmapFileNameMd5, cachePath);

        return ret;
    }

    /**
     * 获取已经磁盘缓存图片的路径
     *
     * @param filePathOrUrl
     * @return
     */
    public String diskCachedFilePath(String filePathOrUrl) {
        return diskCachedFilePath(filePathOrUrl, 0, 0);
    }

    /**
     * 获取已经磁盘缓存图片的路径
     *
     * @param filePathOrUrl
     * @param width
     * @param height
     * @return
     */
    public String diskCachedFilePath(String filePathOrUrl, int width, int height) {
        return diskCachedFilePath(filePathOrUrl, width, height, null);
    }

    /**
     * 获取已经磁盘缓存图片的路径
     *
     * @param filePathOrUrl
     * @param width
     * @param height
     * @param cachePath
     * @return
     */
    public String diskCachedFilePath(String filePathOrUrl, int width, int height, String cachePath) {
        return diskCachedFilePath(filePathOrUrl, width, height, cachePath, false);
    }

    /**
     * 获取已经磁盘缓存图片的路径
     *
     * @param filePathOrUrl
     * @param width
     * @param height
     * @param cachePath
     * @param isNeedCut
     * @return
     */
    public String diskCachedFilePath(String filePathOrUrl, int width, int height, String cachePath, boolean isNeedCut) {

        String bitmapFileNameMd5 = getBitmapFileNameMd5(filePathOrUrl, width, height, cachePath, isNeedCut);

        if (cachePath == null) {
            cachePath = mContext.getCacheDir().getAbsolutePath();
        }

        if (bitmapFileNameMd5 == null) {
            return "";
        }

        File file = new File(cachePath, bitmapFileNameMd5);
        if (file.exists() && file.length() > 0) {
            return file.getAbsolutePath();
        } else {
            return "";
        }

    }

    /**
     * 本图片加载类的图片文件名算法。
     *
     * @param filePathOrUrl
     * @param width
     * @param height
     * @param cachePath
     * @return
     */
    private String getBitmapFileNameMd5(String filePathOrUrl, int width, int height, String cachePath, boolean isNeedCut) {

        if (filePathOrUrl == null) {
            return filePathOrUrl;
        }

        String bitmapFileNameMd5 = null;

        if (width == 0 && height == 0 && cachePath == null) {// 图片非剪切缩放情况：只直接使用filePathOrUrl的md5作为key
            bitmapFileNameMd5 = url2Md5FileName(filePathOrUrl);
        } else {// 图片需要缩放剪切情况下
            String filePath = "";
            if (filePathOrUrl.startsWith("http")) {
                filePath = url2Md5FilePath(filePathOrUrl);
            } else {
                filePath = filePathOrUrl;
            }

            String bitmapKey;
            if (isNeedCut) {// 缩放及剪切的情况
                bitmapKey = String.format("%s%s%s%s", width, height, isNeedCut, filePath);// filePatch需要放在最后为了适用url2Md5FileName方法
            } else {// 仅缩放
                bitmapKey = getInSampleBitmapFileNameKey(filePath, width, height);
            }

            bitmapFileNameMd5 = url2Md5FileName(bitmapKey);
        }

        return bitmapFileNameMd5;
    }

    /**
     * 加载图片 按比例缩放保证图片宽和高不大于width和height
     *
     * @param filePathOrUrl 网络图片url或者本地路径
     * @param width 缩放时宽
     * @param height 缩放时高
     */
    public Bitmap loadImage(String filePathOrUrl, int width, int height) {
        return loadImage(filePathOrUrl, width, height, null);
    }

    /**
     * 加载图片 按比例缩放保证图片宽和高不大于width和height
     *
     * @param filePathOrUrl 网络图片url或者本地路径
     * @param width 宽
     * @param height 高
     * @param cachePath 指定图片缓存目录 null则默认使用手机cache目录mContext.getCacheDir()
     */
    public Bitmap loadImage(String filePathOrUrl, int width, int height, String cachePath) {
        return loadImage(filePathOrUrl, width, height, cachePath, false);
    }

    /**
     * 加载图片<br>
     * <br>
     * 重要：本方法中isNeedCut=true使用了sdk
     * level8的特性。使用本方法，需要保证你的app中android:minSdkVersion大于等于8，否则在低版本中会异常
     *
     * @param filePathOrUrl 网络图片url或者本地路径
     * @param width 宽
     * @param height 高
     * @param isNeedCut true:缩放后将大于指定width和height的裁剪掉
     *            false:仅按比例缩放保证图片宽和高不大于width和height
     * @return
     */
    @TargetApi(8)
    public Bitmap loadImage(String filePathOrUrl, int width, int height, boolean isNeedCut) {
        return loadImage(filePathOrUrl, width, height, null, isNeedCut);
    }

    final static int Nomal = 0;
    final static int InSample = 1;
    final static int InSampleAndCut = 2;

    /**
     * 加载图片<br>
     * <br>
     * 重要：本方法中isNeedCut=true使用了sdk
     * level8的特性。使用本方法，需要保证你的app中android:minSdkVersion大于等于8，否则在低版本中会异常
     *
     * @param filePathOrUrl 网络图片url或者本地路径
     * @param width 宽
     * @param height 高
     * @param cachePath 指定图片缓存目录 null则默认使用手机cache目录mContext.getCacheDir()
     * @param isNeedCut true:缩放后将大于指定width和height的裁剪掉
     *            false:仅按比例缩放保证图片宽和高不大于width和height
     * @return
     */
    @TargetApi(8)
    public Bitmap loadImage(String filePathOrUrl, int width, int height, String cachePath, boolean isNeedCut) {

        if (filePathOrUrl == null) {
            return null;
        }

        Bitmap ret = null;

        int imageLoadType = Nomal;
        if (width != 0 && height != 0) {
            if (isNeedCut) {
                imageLoadType = InSampleAndCut;
            } else {
                imageLoadType = InSample;
            }
        }

        switch (imageLoadType) {
            case Nomal:
                DevUtil.v("jackzhou", String.format("loadImage:Nomal task:%s", this));
                ret = loadImage(filePathOrUrl);
                break;

            case InSample:
                DevUtil.v("jackzhou", String.format("loadImage:InSample task:%s", this));
                ret = getInSampleBitmapUseFilePathOrUrl(filePathOrUrl, width, height, cachePath);

                break;

            case InSampleAndCut:
                DevUtil.v("jackzhou", String.format("loadImage:InSampleAndCut task:%s", this));
                ret = getInSampleAndCutBitmap(filePathOrUrl, width, height, cachePath);

                break;
        }

        return ret;
    }

    /**
     * 高斯模糊
     */
    public static Bitmap BoxBlurFilter(Bitmap bmp, int iterations, int hRadius, int vRadius) {
        if(bmp == null){
            return bmp;
        }
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        int[] inPixels = new int[width * height];
        int[] outPixels = new int[width * height];
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bmp.getPixels(inPixels, 0, width, 0, 0, width, height);
        for (int i = 0; i < iterations; i++) {
            blur(inPixels, outPixels, width, height, hRadius);
            blur(outPixels, inPixels, height, width, vRadius);
        }
        blurFractional(inPixels, outPixels, width, height, hRadius);
        blurFractional(outPixels, inPixels, height, width, vRadius);
        bitmap.setPixels(inPixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    public static void blur(int[] in, int[] out, int width, int height,
            float radius) {
        int widthMinus1 = width - 1;
        int r = (int) radius;
        int tableSize = 2 * r + 1;
        int divide[] = new int[256 * tableSize];

        for (int i = 0; i < 256 * tableSize; i++)
            divide[i] = i / tableSize;

        int inIndex = 0;

        for (int y = 0; y < height; y++) {
            int outIndex = y;
            int ta = 0, tr = 0, tg = 0, tb = 0;

            for (int i = -r; i <= r; i++) {
                int rgb = in[inIndex + clamp(i, 0, width - 1)];
                ta += (rgb >> 24) & 0xff;
                tr += (rgb >> 16) & 0xff;
                tg += (rgb >> 8) & 0xff;
                tb += rgb & 0xff;
            }

            for (int x = 0; x < width; x++) {
                out[outIndex] = (divide[ta] << 24) | (divide[tr] << 16)
                        | (divide[tg] << 8) | divide[tb];

                int i1 = x + r + 1;
                if (i1 > widthMinus1)
                    i1 = widthMinus1;
                int i2 = x - r;
                if (i2 < 0)
                    i2 = 0;
                int rgb1 = in[inIndex + i1];
                int rgb2 = in[inIndex + i2];

                ta += ((rgb1 >> 24) & 0xff) - ((rgb2 >> 24) & 0xff);
                tr += ((rgb1 & 0xff0000) - (rgb2 & 0xff0000)) >> 16;
                tg += ((rgb1 & 0xff00) - (rgb2 & 0xff00)) >> 8;
                tb += (rgb1 & 0xff) - (rgb2 & 0xff);
                outIndex += height;
            }
            inIndex += width;
        }
    }

    public static void blurFractional(int[] in, int[] out, int width,
            int height, float radius) {
        radius -= (int) radius;
        float f = 1.0f / (1 + 2 * radius);
        int inIndex = 0;

        for (int y = 0; y < height; y++) {
            int outIndex = y;

            out[outIndex] = in[0];
            outIndex += height;
            for (int x = 1; x < width - 1; x++) {
                int i = inIndex + x;
                int rgb1 = in[i - 1];
                int rgb2 = in[i];
                int rgb3 = in[i + 1];

                int a1 = (rgb1 >> 24) & 0xff;
                int r1 = (rgb1 >> 16) & 0xff;
                int g1 = (rgb1 >> 8) & 0xff;
                int b1 = rgb1 & 0xff;
                int a2 = (rgb2 >> 24) & 0xff;
                int r2 = (rgb2 >> 16) & 0xff;
                int g2 = (rgb2 >> 8) & 0xff;
                int b2 = rgb2 & 0xff;
                int a3 = (rgb3 >> 24) & 0xff;
                int r3 = (rgb3 >> 16) & 0xff;
                int g3 = (rgb3 >> 8) & 0xff;
                int b3 = rgb3 & 0xff;
                a1 = a2 + (int) ((a1 + a3) * radius);
                r1 = r2 + (int) ((r1 + r3) * radius);
                g1 = g2 + (int) ((g1 + g3) * radius);
                b1 = b2 + (int) ((b1 + b3) * radius);
                a1 *= f;
                r1 *= f;
                g1 *= f;
                b1 *= f;
                out[outIndex] = (a1 << 24) | (r1 << 16) | (g1 << 8) | b1;
                outIndex += height;
            }
            out[outIndex] = in[width - 1];
            inIndex += width;
        }
    }

    public static int clamp(int x, int a, int b) {
        return (x < a) ? a : (x > b) ? b : x;
    }

	/**
	 * 压缩图片文件
	 *
	 * @param imageFile
	 * @param width
	 * @param height
	 * @return
	 */
	public File getReSizeFile(File imageFile, int width, int height) {
		try {
		    File cacheFile = new File(Container.getContext().getCacheDir(),
	                System.currentTimeMillis() + "");
	        Bitmap bitmap = loadImage(imageFile.getAbsolutePath(), width, height);
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
	        byte[] bb = baos.toByteArray();
	        byte2File(bb, cacheFile.getAbsolutePath());
	        DevUtil.v("zqt", bitmap.getWidth() + "-" + bitmap.getHeight()
	                + " size:" + bb.length+" "+imageFile.getAbsolutePath());
	        bitmap.recycle();
	        bitmap=null;
	        return cacheFile;
        } catch (Exception e) {
            e.printStackTrace();
        }
	    return null;
	}
}
