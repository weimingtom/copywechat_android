package com.anjuke.android.commonutils;


import java.text.DecimalFormat;

public class MathUtil {

/**
 * 格式化double数字
 * @param num			待格式化的数字
 * @param format		格式化格式		类似0.00的字符串
 * @return
 */
    public static Double formatNum(Double num, String format){
        Double ret;
        DecimalFormat dformat = new DecimalFormat(format);
        ret = Double.valueOf( dformat.format(num) );
        return ret;
    }

    /**
     * 格式化保留0位小数
     * @param num
     * @return
     */
    public static Double formatNum_0(Double num){
        return formatNum(num,"0");
    }

    /**
     * 格式化保留2位小数
     * @param num
     * @return
     */
    public static Double formatNum_2(Double num){
        return formatNum(num,"0.00");
    }

    /**
     * 转化坐标格式
     * http://en.wikipedia.org/wiki/Geographic_coordinate_conversion
     * @param coord
     *          Input a double latitude or longitude in the decimal format <br>
     *          e.g 87.728056
     * @return
     */
    public static String gpsDecimalToDMS(double coord) {

            String output, degrees, minutes, seconds;

            // gets the modulus the coordinate devided by one (MOD1).
            // in other words gets all the numbers after the decimal point.
            // e.g mod = 87.728056 % 1 == 0.728056
            //
            // next get the integer part of the coord. On other words the whole number part.
            // e.g intPart = 87

            double mod = coord % 1;
            int intPart = (int)coord;

            //set degrees to the value of intPart
            //e.g degrees = "87"

            degrees = String.valueOf(intPart);

            // next times the MOD1 of degrees by 60 so we can find the integer part for minutes.
            // get the MOD1 of the new coord to find the numbers after the decimal point.
            // e.g coord = 0.728056 * 60 == 43.68336
            //      mod = 43.68336 % 1 == 0.68336
            //
            // next get the value of the integer part of the coord.
            // e.g intPart = 43

            coord = mod * 60;
            mod = coord % 1;
            intPart = (int)coord;

            // set minutes to the value of intPart.
            // e.g minutes = "43"
            minutes = String.valueOf(intPart);

            //do the same again for minutes
            //e.g coord = 0.68336 * 60 == 40.0016
            //e.g intPart = 40
            coord = mod * 60;
            intPart = (int)coord;

            // set seconds to the value of intPart.
            // e.g seconds = "40"
            seconds = String.valueOf(intPart);

            // I used this format for android but you can change it
            // to return in whatever format you like
            // e.g output = "87/1,43/1,40/1"
            output = degrees + "/1," + minutes + "/1," + seconds + "/1";

            //Standard output of D°M′S″
            //output = degrees + "°" + minutes + "'" + seconds + "\"";

            return output;
    }

    /**
     * 字符串转换为数组
     * 
     * @param separator 分隔符
     * @param str 字符串
     * @return
     */
    public static String[] strChangeArrays(String separator, String str) {
        return str.split(separator);
    }

    /**
     * 字符串转换为数组
     * 
     * @param separator 分隔符
     * @param str 字符串
     * @param limit 限制数组个数
     * @return
     */
    public static String[] strChangeArrays(String separator, String str, int limit) {
        return str.split(separator, limit);
    }

    /**
     * 数组转化为字符串
     * 
     * @param separator 分隔符
     * @param arrays 数组
     * @return
     */
    public static String arraysChangeStr(String separator, String[] arrays) {
        StringBuilder strBuilder = new StringBuilder();

        for (int i = 0; i < arrays.length; i++) {
            strBuilder.append(arrays[i] + separator);
        }
        return strBuilder.toString();
    }
}
