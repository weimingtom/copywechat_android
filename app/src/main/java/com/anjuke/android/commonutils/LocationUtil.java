package com.anjuke.android.commonutils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.anjuke.android.commonutils.entity.GeoGps;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 位置相关工具类
 *
 */
public class LocationUtil {
    /** call api连接超时时间 */
    private static final int CONN_TIME_OUT = 5;
    /** call api读取超时时间 */
    private static final int SO_TIME_OUT = 5;

    /** 手机上可以使用的定位provider，与是否打开gps开关等无关 */
    private static List<String> sCanUsedLocationProviders;

    /** 手机上目前打开的定位provider */
    private static List<String> sOpenLocationProviders;

    /** 手机上目前未打开的定位provider */
    private static List<String> sCloseLocationProviders = new ArrayList<String>();

    private static final String SINA_APP_KEY = "2061016394";//年会 小喇叭的key 先拿来用着 调用sina api使用

    /**
     * 得到未开启的定位服务。<br>
     * 如：LocationManager.GPS_PROVIDER, LocationManager.NETWORK_PROVIDER
     *
     * @return
     */
    public static List<String> getClosedLocationProviders(Context context){
        refreshProvider(context);
        return sCloseLocationProviders;
    }

    /**
     * 得到可使用的定位服务。<br>
     * 如：LocationManager.GPS_PROVIDER, LocationManager.NETWORK_PROVIDER
     *
     * @return
     */
    public static List<String> getCanUsedLocationProviders(Context context){
        refreshProvider(context);
        return sCanUsedLocationProviders;
    }

    /**
     * 刷新Providers
     */
    private static void refreshProvider(Context context){

        if(sCloseLocationProviders != null){
            sCloseLocationProviders.clear();
        }
        if(sCanUsedLocationProviders != null){
            sCanUsedLocationProviders.clear();
        }
        if(sOpenLocationProviders != null){
            sOpenLocationProviders.clear();
        }

        LocationManager sLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        sCanUsedLocationProviders = sLocationManager.getProviders(false);
        sOpenLocationProviders = sLocationManager.getProviders(true);

        if(sCanUsedLocationProviders != null && sCanUsedLocationProviders.size() > 0
                && sOpenLocationProviders != null && sOpenLocationProviders.size() > 0){

            sCloseLocationProviders.addAll(sCanUsedLocationProviders);
            sCloseLocationProviders.removeAll(sOpenLocationProviders);
        }
    }


    /**
     * 线程中获取坐标地址设置到textview上，依赖网络速度及网络情况,会缓存数据到hashmap
     * 注意：在使用的时候要确保DevUtil已经被初始化了
     * @param lat 纬度
     * @param lng 经度
     * @param tv 待设置的textview
     */
    public static void setAddress2TextViewInThread(final Double lat, final Double lng, final TextView tv){
        setAddress2TextViewInThread(lat, lng, tv, null, null);
    }

    /**
     * 线程中获取坐标地址设置到textview上，依赖网络速度及网络情况,会缓存数据到hashmap
     * 注意：在使用的时候要确保DevUtil已经被初始化了
     * @param lat 纬度
     * @param lng 经度
     * @param tv 待设置的textview
     * @param defaultText 获取时textview显示的文字 为null则不更改显示
     * @param errorText 获取文字失败textview显示的文字（一般google限制或被墙） 为null时则不更改显示
     */
    public static void setAddress2TextViewInThread(final Double lat, final Double lng, final TextView tv, final String defaultText, final String errorText){
        setAddress2TextViewInThread(lat, lng, tv, null, null, null);
    }

    /**
     * 线程中获取坐标地址设置到textview上，依赖网络速度及网络情况,会缓存数据到hashmap
     * 注意：在使用的时候要确保DevUtil已经被初始化了
     * @param lat 纬度
     * @param lng 经度
     * @param tv 待设置的textview
     * @param defaultText 获取时textview显示的文字 为null则不更改显示
     * @param errorText 获取文字失败textview显示的文字（一般google限制或被墙） 为null时则不更改显示
     * @param suffix 设置文字后缀，例如：定位到陆家嘴166号 suffix=附近 则设置到textview文字为“陆家嘴166号附近”
     */
    public static void setAddress2TextViewInThread(final Double lat, final Double lng, final TextView tv, final String defaultText, final String errorText, final String suffix){

        new AsyncTask<Void, Void, String>(){

            protected void onPreExecute() {

                if(defaultText != null){
                    tv.setText(defaultText);
                }
            }

            @Override
            protected String doInBackground(Void... params) {
                return getAddress(lat, lng);
            }

            protected void onPostExecute(String result) {

                try{
                    if(result != null && result.trim().length() > 0){
                        if(suffix != null && suffix.trim().length() > 0){
                            tv.setText(result + suffix);
                        }else{
                            tv.setText(result);
                        }
                    }else{
                        if(errorText != null){
                            tv.setText(errorText);
                        }
                    }
                }catch (Exception e) {//新打开activity的启动该线程快速关掉activity，可能会导致tv被回收设置错误的情况

                }
            }
        }.execute();
    }

    private static HashMap<String, String> addressCacheMap = new HashMap<String, String>();
    /**
     * 通过坐标获取地址，依赖网络速度及网络情况，会缓存数据到hashmap,相同lat,lng会直接返回
     * @param lat
     * @param lng
     * @return
     */
    public static String getAddress(Double lat, Double lng){

        //缓存取数
        String key = String.valueOf(lat) + String.valueOf(lng);
        String cache = addressCacheMap.get(key);
        if(cache != null){
            return cache;
        }

        //网络获取
        String ret = "";
        ret = getSimpleAddressByGoogle(lat, lng);

        if(ret != null && ret.trim().length() > 0){
            addressCacheMap.put(key, ret);
            return ret;
        }

        //google服务出问题  其他方式获取
        ret = getAddressBySina(lat, lng);
        addressCacheMap.put(key, ret);
        return ret;
    }

    public static String getAddressBySina(Double lat, Double lng){

        if(lat == null || lng == null){
            return "";
        }

        if(lat == 0 || lng == 0){
            return "";
        }

        String ret = "";
        String lnglat = String.valueOf(lng) + "," + String.valueOf(lat);

        //使用新浪api
        //说明：http://open.weibo.com/wiki/2/location/geo/geo_to_address
        //限制：普通授权：10000次/小时
        //测试：http://api.weibo.com/2/location/geo/geo_to_address.json?source=2061016394&coordinate=121.50235428905,31.235913901451
        StringBuffer urlBuffer = new StringBuffer();
        urlBuffer.append("http://api.weibo.com/2/location/geo/geo_to_address.json?source=");
        urlBuffer.append(SINA_APP_KEY);
        urlBuffer.append("&coordinate=");
        urlBuffer.append(lnglat);

        try {
            String json = HttpUtil.getMethod(urlBuffer.toString(), CONN_TIME_OUT, SO_TIME_OUT);
            JSONObject jsonObject = new JSONObject(json);
            JSONObject geo = jsonObject.getJSONArray("geos").getJSONObject(0);

            String address = geo.getString("address");
            String provinceName = geo.getString("province_name");
            String cityName = geo.getString("city_name");

            if(provinceName != null){//去掉省
                address = address.replaceFirst(provinceName, "");
            }
            if(cityName != null){
                address = address.replaceFirst(cityName, "");
            }

            ret = address;

        } catch (Exception e) {
            ret = "";
        }

        return ret;
    }

    /**
     * 得到简单的地址。路名
     * @param lat
     * @param lng
     * @return
     */
    private static String getSimpleAddressByGoogle(Double lat, Double lng){

        if (lat == null || lng == null) {
            return "";
        }

        if (lat == 0 || lng == 0) {
            return "";
        }

        String ret = "";

        GoogleAddress googleAddress = getAddressByGoogle(lat, lng);

        if(googleAddress != null && googleAddress.getRoute() != null){
            ret = googleAddress.getRoute();
        }

        return ret;
    }

    public static class GoogleAddress{
        private String street_number = "";
        private String route = "";
        private String sublocality = "";
        private String locality = "";
        private String administrative_area_level_1 = "";
        private String country = "";
        public String getStreet_number() {
            return street_number;
        }
        public void setStreet_number(String street_number) {
            this.street_number = street_number;
        }
        public String getRoute() {
            return route;
        }
        public void setRoute(String route) {
            this.route = route;
        }
        public String getSublocality() {
            return sublocality;
        }
        public void setSublocality(String sublocality) {
            this.sublocality = sublocality;
        }
        public String getLocality() {
            return locality;
        }
        public void setLocality(String locality) {
            this.locality = locality;
        }
        public String getAdministrative_area_level_1() {
            return administrative_area_level_1;
        }
        public void setAdministrative_area_level_1(String administrative_area_level_1) {
            this.administrative_area_level_1 = administrative_area_level_1;
        }
        public String getCountry() {
            return country;
        }
        public void setCountry(String country) {
            this.country = country;
        }

    }
    /**
     * 通过坐标获取地址，依赖google服务
     * @param lat
     * @param lng
     * @return
     */
    public static GoogleAddress getAddressByGoogle(Double lat, Double lng){
        GoogleAddress ret = new GoogleAddress();

        if (lat == null || lng == null) {
            return ret;
        }

        if (lat == 0 || lng == 0) {
            return ret;
        }

        String latlng = String.valueOf(lat) + "," + String.valueOf(lng);
        String url = "http://maps.google.com/maps/api/geocode/json?latlng=" + latlng + "&sensor=true&oe=utf-8&language=zh-CN";

        try {

            String json = HttpUtil.getMethod(url, CONN_TIME_OUT, SO_TIME_OUT);
            JSONObject jsonObject = new JSONObject(json);

            if (jsonObject.optString("status").equals("OK")) {
                JSONArray jsonArray = jsonObject.getJSONArray("results");
                JSONObject jsonResult = jsonArray.getJSONObject(0);

                JSONArray addressComponentsArray = jsonResult.getJSONArray("address_components");

                HashMap<String, String> addressMap = new HashMap<String, String>();//保存

                for(int i=0; i<addressComponentsArray.length(); i++){

                    try{
                        JSONObject temp = addressComponentsArray.getJSONObject(i);
                        String key = temp.getJSONArray("types").getString(0);
                        addressMap.put(key, temp.getString("long_name"));
                    }catch (Exception e) {
                        continue;
                    }
                }

                String street_number =  addressMap.get("street_number");
                String route =  addressMap.get("route");
                String sublocality =  addressMap.get("sublocality");
                String locality =  addressMap.get("locality");
                String administrative_area_level_1 =  addressMap.get("administrative_area_level_1");
                String country =  addressMap.get("country");

                if(street_number != null && street_number.trim().length() > 0){
                    ret.setStreet_number(street_number);
                }
                if(route != null && route.trim().length() > 0){
                    ret.setRoute(route);
                }
                if(sublocality != null && sublocality.trim().length() > 0){
                    ret.setSublocality(sublocality);
                }
                if(locality != null && locality.trim().length() > 0){
                    ret.setLocality(locality);
                }
                if(administrative_area_level_1 != null && administrative_area_level_1.trim().length() > 0){
                    ret.setAdministrative_area_level_1(administrative_area_level_1);
                }
                if(country != null && country.trim().length() > 0){
                    ret.setCountry(country);
                }

            }
        } catch (Exception e) {
            return ret;
        }

        return ret;
    }

    /**
     * 由新浪api获取偏移修正的坐标
     * http://open.weibo.com/wiki/2/location/geo/gps_to_offset
     * @param lat
     * @param lng
     * @return
     */
    public static GeoGps getOffSetGpsBySina(Double lat, Double lng){
        GeoGps ret = new GeoGps(lat, lng);

        if(lat != null && lng != null){
            StringBuffer url = new StringBuffer();
            url.append("http://api.weibo.com/2/location/geo/gps_to_offset.json?source=");
            url.append(SINA_APP_KEY);
            url.append("&coordinate=");
            url.append(lng);
            url.append(",");
            url.append(lat);

            try {
                String json = HttpUtil.getMethod(url.toString(), CONN_TIME_OUT, SO_TIME_OUT);
//                {"geos":[{"longitude":"121.501304","latitude":"31.23566"}]}
                JSONObject geosJsonObject = new JSONObject(json);

                Double offsetLat = geosJsonObject.getJSONArray("geos").getJSONObject(0).getDouble("latitude");
                Double offsetLng = geosJsonObject.getJSONArray("geos").getJSONObject(0).getDouble("longitude");

                ret.setLat(offsetLat);
                ret.setLng(offsetLng);

            } catch (Exception e) {
                Log.e(TAG, String.valueOf(e));
            }
        }

        return ret;
    }

    /**
     * 获取修正偏移后的坐标，查询失败返回原始坐标<br>
     * 使用安特网接口<br>
     * 目前：免费用户每天查询上限为5000次。<br>
     * http://www.anttna.com/archives/1010.html<br>
     * <br>
     * 因走网络接口，在线程中使用<br>
     *
     * @param gps 原始坐标
     * @return
     */
    public static GeoGps getOffSetGps(GeoGps gps){

        //缓存未命中
        GeoGps ret = gps;

        if(gps.getLat() != null && gps.getLng() != null){
            StringBuffer url = new StringBuffer();
            url.append("http://www.anttna.com/goffset/goffset1.php?lat=");
            url.append(gps.getLat());
            url.append("&lon=");
            url.append(gps.getLng());

            try {
                String respon = HttpUtil.getMethod(url.toString(), CONN_TIME_OUT, SO_TIME_OUT);
                String[] temp = respon.split(",");

                Double lat = Double.parseDouble(temp[0]);
                Double lng = Double.parseDouble(temp[1]);

                //方法getOffSetGps使用修正时，快速多次访问时会偶尔直接返回(0,0)的坐标，该问题导致客户端这边增加异常处理
                //测试：http://www.anttna.com/goffset/goffset1.php?lat=31.2378832&lon=121.497875
                //算法：
                //    记录上次的偏移量，取到0时通过上次的偏移量计算修正后的坐标
                if(lat != 0 || lng != 0){
                    diffLat = lat - gps.getLat();
                    diffLng = lng - gps.getLng();
                }else{
                    lat = gps.getLat() + diffLat;
                    lng = gps.getLng() + diffLng;
                }

                ret.setLat(lat);
                ret.setLng(lng);

            } catch (Exception e) {
                Log.e(TAG, String.valueOf(e));
            }
        }

        return ret;
    }

    /**
     * 获取修正偏移后的坐标，查询失败返回原始坐标<br>
     * 使用安特网接口<br>
     * 目前：免费用户每天查询上限为5000次。<br>
     * http://www.anttna.com/archives/1010.html<br>
     * <br>
     * 因走网络接口，在线程中使用<br>
     *
     * @param lat 原始坐标纬度
     * @param lng 原始坐标经度
     * @return
     */
    public static GeoGps getOffSetGps(Double lat, Double lng){
        GeoGps ret = new GeoGps(lat, lng);

        if(lat != null && lng != null && lat != 0 && lng != 0){
            GeoGps temp = new GeoGps(lat, lng);
            ret = getOffSetGps(temp);
        }

        return ret;
    }

    private static double diffLat;//diffLat = 修正后的lat - 修正前的lat
    private static double diffLng;


    /**
     * 提示是否打开定位服务
     */
    public static void showLocationProvider(final Context context) {

        new AlertDialog.Builder(context).setTitle("提示").setMessage("您的手机尚未打开定位功能，是否需要打开？").setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                context.startActivity(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"));
            }

        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }

        }).show();

    }


    /**
     * 由地球坐标获取百度坐标 非公开接口 谨慎使用 随时可能被咔嚓 接口无法使用后返回未偏移传入的坐标<br>
     * 
     * @param geoGps
     * @return
     */
    public static GeoGps getOffSetOfBaiduGpsByBaidu(GeoGps geoGps) {
        return getOffSetGpsByBaidu(geoGps, 4);
    }
    /**
     * 由地球坐标获取火星坐标 非公开接口 谨慎使用 随时可能被咔嚓 接口无法使用后返回未偏移传入的坐标<br>
     * 
     * @param geoGps
     * @return
     */
    public static GeoGps getOffSetOfMarsGpsByBaidu(GeoGps geoGps) {
        return getOffSetGpsByBaidu(geoGps, 2);
    }
    
    /**
     * 由火星坐标获取百度坐标 非公开接口 谨慎使用 随时可能被咔嚓 接口无法使用后返回未偏移传入的坐标
     * @param geoGps
     * @return
     */
    public static GeoGps getOffSetOfBaiduGpsFromMarsByBaidu(GeoGps geoGps) {
        return getOffSetGpsByBaidu(geoGps, 2, 4);
    }
    
    /**
     * 由地球坐标获取百度坐标 非公开接口 谨慎使用 随时可能被咔嚓 接口无法使用后返回未偏移传入的坐标<br>
     * 限制：每秒访问少于50次<br>
     * <br>
     * @param geoGps
     * @param to        2表示火星坐标，4表示百度坐标
     * @return
     */
    private static GeoGps getOffSetGpsByBaidu(GeoGps geoGps, int to) {
        return getOffSetGpsByBaidu(geoGps, 0, to);
    }
    
    /**
     * 由地球坐标、火星坐标获取百度坐标 非公开接口 谨慎使用 随时可能被咔嚓 接口无法使用后返回未偏移传入的坐标<br>
     * 限制：每秒访问少于50次<br>
     * <br>
     * 接口地址：http://api.map.baidu.com/ag/coord/convert?x=121.583140&y=31.341174&from=0&to=2&mode=1<br>
     * 说明：x和y就是经纬度了，替换成你真实的经纬度即可，from和to表示坐标系，0表示地球坐标，2表示火星坐标，4表示百度坐标，所以这里是从地球坐标转换成火星坐标，mode参数未知。<br>
     * 结果：[{"error":0,"x":"MTIxLjU4NzM2NDA5NTA1","y":"MzEuMzM5MDI3NTA2NTE="}]<br>
     * 说明：error为0表示没有错误，返回的x和y是base64算法后的结果(可以自行Google加解密base64)，解密后就是：121.58736409505和31.33902750651，这个就是火星坐标。<br>
     * @param geoGps
     * @param from      0标示地球坐标，2表是火星坐标
     * @param to        2表示火星坐标，4表示百度坐标
     * @return
     */
    private static GeoGps getOffSetGpsByBaidu(GeoGps geoGps, int from, int to) {
        if(geoGps == null){
            return null;
        }
        
        GeoGps ret = new GeoGps(geoGps.getLat(), geoGps.getLng());
        
        Double lat = geoGps.getLat();
        Double lng = geoGps.getLng();
        
        if (lat != null && lng != null) {
            StringBuffer url = new StringBuffer();
            url.append("http://api.map.baidu.com/ag/coord/convert?x=");
            url.append(lng);
            url.append("&y=");
            url.append(lat);
            url.append("&from=");
            url.append(from);
            url.append("&to=");
            url.append(to);
            url.append("&mode=1");

            try {
                String json = HttpUtil.getMethod(url.toString(), CONN_TIME_OUT, SO_TIME_OUT);
                //[{"error":0,"x":"MTIxLjUwNjgwOTc2NzAz","y":"MzEuMjQxMTAxNzIxOTY2"}]
                JSONArray geosJsonArray = new JSONArray(json);
                JSONObject geosJsonObject = geosJsonArray.getJSONObject(0);
                if(geosJsonObject.getInt("error") == 0){
                    
                    String baiduLatEncode = geosJsonObject.getString("y");
                    String baiduLngEncode = geosJsonObject.getString("x");
                    
                    String baiduLat = new String( com.alibaba.fastjson.util.Base64.decodeFast(baiduLatEncode) );
                    String baiduLng = new String( com.alibaba.fastjson.util.Base64.decodeFast(baiduLngEncode) );
                    
                    ret.setLat(Double.parseDouble(baiduLat));
                    ret.setLng(Double.parseDouble(baiduLng));
                    
                }

            } catch (Exception e) {
                Log.e(TAG, String.valueOf(e));
            }
        }
        
        return ret;
    }
    

    private final static String TAG = LocationUtil.class.getName();
}
