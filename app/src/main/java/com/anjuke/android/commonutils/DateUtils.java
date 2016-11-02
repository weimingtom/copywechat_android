/**
 *
 * Copyright 2012 Anjuke. All rights reserved.
 * TimeUtils.java
 *
 */

package com.anjuke.android.commonutils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author wangchun (chunwang@anjuke.com)
 * @date 2012-5-17
 */
public class DateUtils {

    public static String getCurrentDisplayTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd   HH:mm");

        Date curDate = new Date(System.currentTimeMillis());

        return formatter.format(curDate);
    }

    public static String getCurrentDisplayTime(String pattern) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        Date curDate = new Date(System.currentTimeMillis());
        return formatter.format(curDate);
    }

    public static String getCurrentTime() {
        String tempTime = getCurrentDisplayTime();

        return tempTime.substring(tempTime.lastIndexOf(" ") + 1, tempTime.length());

    }

    public static String getUnixTimeString(long dateLong) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date targetDate = new Date(dateLong);
        return formatter.format(targetDate);
    }

    public static String getHHmmTimeString(long dateLong) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");

        Date targetDate = new Date(dateLong);

        return formatter.format(targetDate);
    }

    public static String getyyyyMMddHHmmTimeString(long dateL) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd   HH:mm");

        Date targetDate = new Date(dateL);

        return formatter.format(targetDate);
    }

    public static String getyyyyMMddTimeString(long dateL) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");
        Date targetDate = new Date(dateL);
        return formatter.format(targetDate);
    }

    public static String getyyyyMMddTimeString(Date targetDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");
        return formatter.format(targetDate);
    }

    public static String getyyyyMMddHHmmssTimeString(long dateL) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date targetDate = new Date(dateL);

        return formatter.format(targetDate);
    }

    /**
     * 将时间戳转化为long 注意： 如果想要精确的转换，先要确定你的原数据是毫秒级还是秒， 如果是毫秒格式时应加上SSS（如：
     * HH:mm:ss:SSS） 否则会导致你的数据转换后1000范围内的误差(可以忽略1000来解决这个问题，如除以1000)
     * 
     * @param unixTime
     * @param format
     * @return
     */
    public static long getLongTime(String unixTime, String format) {
        long ret = -1;
        SimpleDateFormat sdformat = new SimpleDateFormat(format);
        try {
            Date targetDate = sdformat.parse(unixTime);
            ret = targetDate.getTime();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ret;
    }

    /**
     * 获取日期
     * 
     * @param year
     * @param month
     * @param day
     * @param hourOfDay
     * @param minute
     * @param second
     * @return
     */
    public static Date getDateTime(int year, int month, int day, int hourOfDay, int minute, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hourOfDay, minute, second);
        return calendar.getTime();
    }

    public static Date getDateTime(int year, int month, int day) {
        return getDateTime(year, month, day, 0, 0, 0);
    }

    /**
     * 根据传人的时间戳,返回绝对时间差字符串
     * 
     * @param timestamp 单位毫秒
     * @param currentTime 单位毫秒
     * @return 格式化的字符串
     */
    public static String getDiffTime(Long timestamp, Long currentTime) {
        String ret = "";
        Long sub = Math.abs(timestamp - currentTime) / 1000L;
        if (sub < 60) {
            ret = sub + "秒前";
            return ret;
        }
        if (sub < 3600) {
            ret = sub / 60 + "分钟前";
            return ret;
        }
        if (sub < 86400) {
            ret = sub / 3600 + "小时前";
            return ret;
        }
        ret = sub / 86400 + "天前";

        return ret;
    }

    public static Calendar string2Calendar(String arg, String format) {
        SimpleDateFormat sdf = null;
        String trimString = arg.trim();

        if (format != null) {
            sdf = new SimpleDateFormat(format);
        } else {
            if (trimString.length() > 14)
                sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            else
                sdf = new SimpleDateFormat("yyyy-MM-dd");
        }

        Date d = null;
        try {
            d = sdf.parse(trimString);
        } catch (ParseException e) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        return cal;
    }

    public static Calendar string2Calendar(String arg) {
        return string2Calendar(arg, null);
    }

    /**
     * @param dateValue 单位毫秒
     * @return
     */
    public static String formatTime(Long dateValue) {
        return formatTime(dateValue, "yyyy-MM-dd HH:mm:ss.SSS");
    }

    /**
     * @param dateValue 单位毫秒
     * @param format
     * @return
     */
    public static String formatTime(Long dateValue, String format) {
        Calendar canlendar = Calendar.getInstance();
        canlendar.setTimeInMillis(dateValue);
        return formatTime(canlendar.getTime(), format);
    }

    public static String formatTime(Date dateValue, String format) {
        DateFormat dateformat = new SimpleDateFormat(format);
        String date = dateformat.format(dateValue);
        return date;
    }

    /**
     * @param dateValue eg.2012-09-30 12:30:30 用于团购列表
     */

    public static String getEndTime(String dateValue) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        try {
            Date start = formatter.parse(dateValue.trim());

            c.setTime(start);
            long endmills = c.getTimeInMillis();
            long startmills = System.currentTimeMillis();
            long t = (endmills - startmills) / 1000;
            if (t <= 0) {
                return "已结束";
            }

            long d = t / 86400;
            long h = (t % 86400) / 3600;
            long min = ((t % 86400) % 3600) / 60;
            return d + "天" + h + "小时" + min + "分";

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /*
     * 如果发布时间为当年，显示为MM-dd,如果发布时间不为当年显示为yyyy-MM
     */

    public static String getSpecFormatTime(long time) {

        // 如果发布时间为当年，显示为MM-dd,如果发布时间不为当年显示为yyyy-MM

        Calendar ca = Calendar.getInstance();
        int curYear = ca.get(Calendar.YEAR);

        ca.setTimeInMillis(time * 1000);
        int itemPubYear = ca.get(Calendar.YEAR);

        if (curYear != itemPubYear) {
            return android.text.format.DateFormat.format("yyyy-MM",
                    new Date(time * 1000)).toString();

        } else {
            return android.text.format.DateFormat.format("MM-dd",
                    new Date(time * 1000)).toString();
        }

    }

    public static boolean isYesterday(Date a) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DATE, c.get(Calendar.DATE) - 1);
        Date today = c.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        return format.format(today).equals(format.format(a));
    }

    public static boolean isToday(Date a) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DATE, c.get(Calendar.DATE));
        Date today = c.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        return format.format(today).equals(format.format(a));
    }

    public static String convertSpecTime(long time) {
        Calendar ca = Calendar.getInstance();
        int curYear = ca.get(Calendar.YEAR);
        ca.setTimeInMillis(time);
        int itemPubYear = ca.get(Calendar.YEAR);
        if (curYear != itemPubYear) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
            return sdf.format(new Date(time));

        } else {
            long diff = time - System.currentTimeMillis();
            float flag = (float) (diff / 1000 * 60 * 60 * 24);
            if (flag > 1) {
                SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日");
                return sdf.format(new Date(time));
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                return sdf.format(new Date(time));
            }
        }

    }

    // yyyy-MM-dd HH:mm:ss

    // return MM-dd HH:mm
    public static String getDateAndTime(String formatTime) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long msTime = 0;
        try {
            Calendar c = Calendar.getInstance();

            Date start = formatter.parse(formatTime.trim());
            c.setTime(start);
            msTime = c.getTimeInMillis();
        } catch (ParseException e) {
            return "";
        }

        return formatTime(msTime, "MM-dd HH:mm");
    }
    
    /**
	 * 时间范围				展示说明				范例
	 * 1小时内				N分钟前（当前发是1分钟前） 	1分钟前 <br>
	 * 1小时~24小时 			N小时前 				2小时前<br>
	 * 24小时~前一天的23:59分前	 昨天					 昨天<br>
	 * 前一天的23:59分前~7天内	N天前					6天前<br>
	 * 大于7天~14天			YYYY-MM-DD 			2014-06-14<br>
	 * 
	 */
    public static String getFormatDiffTime(Long timestamp, Long currentTime){
    	String ret = "";
        Long sub = Math.abs(timestamp - currentTime) / 1000L;	//相差秒数
        //获取第三天0点的时间戳
		Calendar calendar = new GregorianCalendar();
		calendar.setTimeInMillis(timestamp);
		calendar.add(Calendar.DATE,2);	
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.SECOND, 0);
		long tommorow = calendar.getTimeInMillis();	
		
        if (sub < 3600) {
            ret = sub / 60 + "分钟前";
        }else if ( sub < 24 * 60 * 60 ) {
			ret = sub / 3600 + "小时前";
 		}else if ( currentTime < tommorow ) {
			ret = "昨天";
		}else if (sub < 7 * 24 * 60 * 60 ) {
			int day = (int) (sub / (24 * 60 * 60)) ;
			ret = day + "天前";
		}else{
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			ret = dateFormat.format(new Date(timestamp));
		}
		return ret;
    }
    
    
    

}
