/**
 * hyhe
 * 下午3:01:54
 */

package com.anjuke.android.commonutils;

import android.util.Log;


/**
 * @author hyhe
 */
public class BaiduMapUtil {

    /**
     * @param w 请求图片宽度
     * @param h 请求图片高度
     * @param mLat 中心点纬度
     * @param mLng 中心点经度
     * @return 返回拼接好的静态地图地址
     */
    public static String getStaticImageUrl(String w, String h, String mLat, String mLng) {
        String str = "";
        str = "http://api.map.baidu.com/staticimage?center="
                + mLng
                + ","
                + mLat
                + "&width="
                + w
                + "&height="
                + h
                + "&zoom=16&markers="
                + mLng
                + ","
                + mLat
				+ "&markerStyles=-1,http://pic1.ajkimg.com/display/b3c415ba96064dec92308208c07acd12.png,-1";
        return str;

    }

    /**
     * @param mLat 中心点纬度
     * @param mLng 中心点经度
     * @return 返回拼接好的静态地图地址
     */
    public static String getStaticImageUrl(String mLat, String mLng) {
        String str = "";
        int w = Math.min(UIUtils.getWidth(), 1024);
        int h = w * 2 / 5;
        str = "http://api.map.baidu.com/staticimage?center="
                + mLng
                + ","
                + mLat
                + "&width="
                + w
                + "&height="
                + h
                + "&zoom=16&markers="
                + mLng
                + ","
                + mLat
				+ "&markerStyles=-1,http://pic1.ajkimg.com/display/b3c415ba96064dec92308208c07acd12.png,-1";
        if (DevUtil.isDebug()) {
            Log.d("baidumapstaticurl", str);
        }
        return str;

    }

    /**
     * 验证坐标有效性 不为空且在中国范围内
     * 
     * @param lat
     * @param lng
     * @return true：坐标有效 fasle：坐标无效
     */
    public static boolean isValidGeoValue(String lat, String lng) {

        double douLat = 0.0;
        double douLng = 0.0;
        try {
            douLat = Double.valueOf(lat);
            douLng = Double.valueOf(lng);
        } catch (Exception e) {
            Log.e("error", "isValidGeoValue:double convert error");
            return false;
        }
        /**
         * 如果坐标异常，超出中国范围则置空 中国地理位置四至点： 最东端 东经135度2分30秒 黑龙江和乌苏里江交汇处 最西端 东经73度40分
         * 帕米尔高原乌兹别里山口（乌恰县） 最南端 北纬3度52分 南沙群岛曾母暗沙 最北端 北纬53度33分 漠河以北黑龙江主航道（漠河县）
         */
        if (douLng > 136 || douLng < 73 || douLat > 54 || douLat < 3) {
            return false;
        }

        return true;
    }

    public static boolean isValidGeoValue(double douLat, double douLng) {
        /**
         * 如果坐标异常，超出中国范围则置空 中国地理位置四至点： 最东端 东经135度2分30秒 黑龙江和乌苏里江交汇处 最西端 东经73度40分
         * 帕米尔高原乌兹别里山口（乌恰县） 最南端 北纬3度52分 南沙群岛曾母暗沙 最北端 北纬53度33分 漠河以北黑龙江主航道（漠河县）
         */
        if (douLng > 136 || douLng < 73 || douLat > 54 || douLat < 3) {
            return false;
        }

        return true;
    }

}
