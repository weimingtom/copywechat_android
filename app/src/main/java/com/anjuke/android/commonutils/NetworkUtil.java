package com.anjuke.android.commonutils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

//以前工程代码，先直接稍作修改拿来使用
public class NetworkUtil {

    /**
     * 得到ip地址
     * @return
     */
    public static String getLocalIpAddress() {
        String ret = "";
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        ret = inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e(LOG_TAG, String.valueOf(ex));
        }
        return ret;
    }

    public static String getNetIsWifiOr3G(Context context){
        String ret = "";

        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);//获取系统的连接服务
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();//获取网络的连接情况
        if(activeNetInfo == null){
            return "none";
        }
        if(activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI){
            //判断WIFI网
            ret = "WIFI";
        }else if(activeNetInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
            //判断3G网
            ret = "2G3G";
        }
        return ret;
    }
    
    private final static String LOG_TAG = NetworkUtil.class.getName();

    // ////////////////////////////////////////////////////////////////////
    // ////////////--------以下为强哥的-------------//////////////////////////
    // ////////////////////////////////////////////////////////////////////
    /**
     * 判断设备网络是否可用
     * 
     * @param context
     * @return 网络可用返回 true, 不可用返回 false
     */
    public static Boolean isNetworkAvailable(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager == null) {
            return false;
        }
        NetworkInfo info[] = manager.getAllNetworkInfo();
        if (info == null) {
            return false;
        }
        for (int i = 0; i < info.length; i++) {
            if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否有Sim卡的存在
     * 
     * @param context
     * @return 有Sim卡，返回true，没Sim卡，返回false
     */
    public static Boolean isSimPresent(Context context) {
        TelephonyManager manager;
        manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        switch (manager.getSimState()) {
            case TelephonyManager.SIM_STATE_READY:
                return true;
            case TelephonyManager.SIM_STATE_ABSENT:
                return false;
            default:
                return false;
        }

    }

    /**
     * get network type
     * <p>
     * 0：No Connection <br>
     * 1：WIFI <br>
     * 2：2G/3G/Other
     * 
     * @param inContext
     * @return 数字0、1或者2
     */
    public static int getNetworkType(Context inContext) {
        // Context context = inContext.getApplicationContext();
        ConnectivityManager connectivity = (ConnectivityManager) inContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivity.getActiveNetworkInfo();

        if (info == null) {
            return 0;
        }
        if (!info.isConnected()) {
            return 0;
        }
        if (info.getTypeName().toUpperCase().equals("WIFI")) {
            return 1;
        } else {
            return 2;
        }
    }

    /**
     * 获取网络类型
     * <p>
     * NETWORK_TYPE_CDMA 网络类型为CDMA <br/>
     * NETWORK_TYPE_EDGE 网络类型为EDGE <br>
     * NETWORK_TYPE_EVDO_0 网络类型为EVDO0 <br>
     * NETWORK_TYPE_EVDO_A 网络类型为EVDOA <br>
     * NETWORK_TYPE_GPRS 网络类型为GPRS <br>
     * NETWORK_TYPE_HSDPA 网络类型为HSDPA <br>
     * NETWORK_TYPE_HSPA 网络类型为HSPA <br>
     * NETWORK_TYPE_HSUPA 网络类型为HSUPA <br>
     * NETWORK_TYPE_UMTS 网络类型为UMTS
     * <p>
     * 在中国，联通的3G为UMTS或HSDPA，移动和联通的2G为GPRS或EGDE，电信的2G为CDMA，电信的3G为EVDO
     */
    /**
     * @param context
     * @return 无网络："NOCONNECTION"<br>
     *         联通3G||电信3G：3G<br>
     *         三大运营商2G：2G <br>
     *         位置类型（比如移动3G）:2Gor3G<br>
     */
    public static String getNetWorkTypeStr(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivity.getActiveNetworkInfo();
        // ----------无网络
        if (info == null || !info.isConnected()) {
            return "NOCONNECTION";
        }
        // ---------手机网络
        if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
            if (info.getSubtype() == TelephonyManager.NETWORK_TYPE_HSDPA
                    || info.getSubtype() == TelephonyManager.NETWORK_TYPE_HSPAP
                    || info.getSubtype() == TelephonyManager.NETWORK_TYPE_UMTS
                    || info.getSubtype() == TelephonyManager.NETWORK_TYPE_EVDO_0
                    || info.getSubtype() == TelephonyManager.NETWORK_TYPE_EVDO_A) {
                return "3G";
            } else if (info.getSubtype() == TelephonyManager.NETWORK_TYPE_GPRS
                    || info.getSubtype() == TelephonyManager.NETWORK_TYPE_CDMA
                    || info.getSubtype() == TelephonyManager.NETWORK_TYPE_EDGE) {
                return "2G";
            } else if (info.getSubtype() == TelephonyManager.NETWORK_TYPE_UNKNOWN) {
                return "2Gor3G";
            }

        }
        // ----------wifi网络
        if (info.getType() == ConnectivityManager.TYPE_WIFI) {
            return "WIFI";
        }
        return "UNKOWN";
    }

    /*
     * //中国电信为CTC //NETWORK_TYPE_EVDO_A是中国电信3G<br> //NETWORK_TYPE_CDMA电信2G是CDMA
     * NETWORK_TYPE_1xRTT也是是电信
     */
    /**
     * 得到网络的名字
     * 
     * @param appContext
     * @return 如果是网络连接是WIFI，则返回WIFI的名字，如果是手机网络，则返回手机网络的制式名字
     */
    public static String getNetworkName(Context appContext) {

        ConnectivityManager connectivity = (ConnectivityManager) appContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivity.getActiveNetworkInfo();
        if (info == null || !info.isConnected()) {
            return "NOCONNECTION";
        }
        if (info.getTypeName().toUpperCase().equals("WIFI")) {
            return info.getExtraInfo();
        }

        return info.getSubtypeName();

        // TelephonyManager tm = (TelephonyManager) appContext
        // .getSystemService(Context.TELEPHONY_SERVICE);
        // int subType = tm.getNetworkType();
        // switch (subType) {
        // case TelephonyManager.NETWORK_TYPE_GPRS:
        // return "GPRS";
        // case TelephonyManager.NETWORK_TYPE_EDGE:
        // return "EDGE";
        // case TelephonyManager.NETWORK_TYPE_UMTS:
        // return "UMTS";
        // case TelephonyManager.NETWORK_TYPE_HSDPA:
        // return "HSDPA";
        // case TelephonyManager.NETWORK_TYPE_HSUPA:
        // return "HSUPA";
        // case TelephonyManager.NETWORK_TYPE_HSPA:
        // return "HSPA";
        // case TelephonyManager.NETWORK_TYPE_CDMA:
        // return "CDMA";
        // case TelephonyManager.NETWORK_TYPE_EVDO_0:
        // return "CDMAEvDo0";
        // case TelephonyManager.NETWORK_TYPE_EVDO_A:
        // return "CDMAEvDoA";
        // case TelephonyManager.NETWORK_TYPE_1xRTT:
        // return "CDMA1xRTT";
        // default:
        // return "XG"; // 其他任何网络制式
        // }
    }

    /**
     * 通过MNC方式获得移动运营商名字
     * 
     * @param appContext
     * @return 中国移动/中国联通/中国电信/未知运营商
     */
    public static String getWirelessCarriers(Context appContext) {
        TelephonyManager telManager = (TelephonyManager) appContext
                .getSystemService(Context.TELEPHONY_SERVICE);
        /**
         * 获取SIM卡的IMSI码 SIM卡唯一标识：IMSI 国际移动用户识别码（IMSI：International Mobile
         * Subscriber Identification Number）是区别移动用户的标志，
         * 储存在SIM卡中，可用于区别移动用户的有效信息。IMSI由MCC、MNC、MSIN组成，其中MCC为移动国家号码，由3位数字组成，
         * 唯一地识别移动客户所属的国家，我国为460；MNC为网络id，由2位数字组成，
         * 用于识别移动客户所归属的移动网络，中国移动为00，中国联通为01,中国电信为03；MSIN为移动客户识别码，采用等长11位数字构成。
         * 唯一地识别国内GSM移动通信网中移动客户。所以要区分是移动还是联通，只需取得SIM卡中的MNC字段即可
         */
        String imsi = telManager.getSubscriberId();
        // 　　　　　　　　　　"getNetworkOperatorName=" +
        // telManager.getNetworkOperatorName()+"\n" +//直接获取移动运营商名称
        if (imsi != null) {
            if (imsi.startsWith("46000") || imsi.startsWith("46002") || imsi.startsWith("46007")) {// 因为移动网络编号46000下的IMSI已经用完，所以虚拟了一个46002编号，134/159号段使用了此编号
                return "中国移动";
                // 中国移动
            } else if (imsi.startsWith("46001")) {
                return "中国联通";
                // 中国联通
            } else if (imsi.startsWith("46003")) {
                return "中国电信";
                // 中国电信
            }
        }
        return "未知运营商";

    }
}
