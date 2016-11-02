
package com.anjuke.android.commonutils;

import android.util.Log;

import com.alibaba.fastjson.util.Base64;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MapUtils {

    /**
     * 火星转百度(返回数组)
     * 
     * @param gg_lat
     * @param gg_lon
     * @return
     */
    public static String[] convertGDToBDArray(double gg_lat, double gg_lon) {
        double x = gg_lon, y = gg_lat;
        double x_pi = x / 180.0;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * x_pi);
        double bd_lat = z * Math.sin(theta) + 0.006;
        double bd_lon = z * Math.cos(theta) + 0.0065;
        return new String[] {
                bd_lat + "", bd_lon + ""
        };
    }

    public static double[] convertGoogleToBaidu(double x, double y) {
        String path = "http://api.map.baidu.com/geoconv/v1/?coords=" + x + "," + y + "&ak=b7LAoVySMGeSUtQKSKW1lwqh";
        Log.d("MapUtils", path);
        try {
            // 使用http请求获取转换结果
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5 * 1000);
            InputStream inStream = conn.getInputStream();

            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
            // 得到返回的结果
            String res = outStream.toString();
            System.out.println(res);
            // 处理结果
            if (res.indexOf("(") > 0 && res.indexOf(")") > 0) {
                String str = res.substring(res.indexOf("(") + 1,
                        res.indexOf(")"));
                String err = res.substring(res.indexOf("error") + 7,
                        res.indexOf("error") + 8);
                if ("0".equals(err)) {
                    JSONObject js = new JSONObject(str);
                    // 编码转换
                    String x1 = new String(Base64.decodeFast(js.getString("x")));
                    String y1 = new String(Base64.decodeFast(js.getString("y")));
                    double dx = Double.parseDouble(x1);
                    double dy = Double.parseDouble(y1);
                    return new double[] {
                            dx, dy
                    };
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

}
