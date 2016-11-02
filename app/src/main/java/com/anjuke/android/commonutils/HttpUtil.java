package com.anjuke.android.commonutils;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

public class HttpUtil {

    private static HttpClient _httpClient;

    public static String getMethod(String url, int connTimeOut, int soTimeOut) throws ClientProtocolException, IOException {
        HttpEntity entity = null;
        HttpGet method = new HttpGet(url);
        DevUtil.timePoint("jackzhou", "getMethod start url:" + url);
        HttpResponse response = getHttpClient(connTimeOut, soTimeOut).execute(method);
        DevUtil.timePoint("jackzhou", "getMethod end");

        entity = response.getEntity();
        String ret = EntityUtils.toString(entity);
        DevUtil.timePoint("jackzhou", "getMethod response:" + String.valueOf(ret));

        return ret;
    }

    public static String getMethod(String url, HashMap<String, String> params, int connTimeOut, int soTimeOut) throws ClientProtocolException, IOException {

        String paramStr = "";
        for(String key: params.keySet()){

            String value = params.get(key);
            paramStr = paramStr + "&" + key + "=" + urlEncode(value);
        }

        if(url.contains("?")){
            url = url + paramStr;
        }else{
            url = url + "?" + paramStr;
        }

        return getMethod(url, connTimeOut, soTimeOut);
    }

    public static String getMethod(String url, HashMap<String, String> params) throws ClientProtocolException, IOException {
        return getMethod(url, params, true, null);
    }
    
    public static String getMethod(String url, HashMap<String, String> params, HashMap<String, String> header) throws ClientProtocolException, IOException {
        return getMethod(url, params, true, header);
    }

    public static String getMethod(String url, HashMap<String, String> params, boolean isUseGzip) throws ClientProtocolException, IOException {
        return getMethod(url, params, isUseGzip, null);
    }
    
    public static String getMethod(String url, HashMap<String, String> params, boolean isUseGzip, HashMap<String, String> header) throws ClientProtocolException, IOException {

        String paramStr = "";
        for(String key: params.keySet()){

            String value = params.get(key);
            paramStr = paramStr + "&" + key + "=" + urlEncode(value);
        }

        if(url.contains("?")){
            url = url + paramStr;
        }else{
            url = url + "?" + paramStr;
        }

        return getMethod(url, isUseGzip, header);
    }

    public static String getMethod(String url) throws ClientProtocolException, IOException {
        return getMethod(url, true);
    }

    public static String getMethod(String url, boolean isUseGzip) throws ClientProtocolException, IOException {
        return getMethod(url, isUseGzip, null);
    }
    
    public static String getMethod(String url, boolean isUseGzip, HashMap<String, String> header) throws ClientProtocolException, IOException {
        String ret = null;

        HttpEntity entity = null;
        HttpGet method = new HttpGet(url);

        //gzip压缩
        if(isUseGzip){
            method.setHeader("Accept-Encoding", "gzip");
        }
        
        //自定义header
        if(header != null){
            for(String key: header.keySet()){
                method.setHeader(key, header.get(key));
            }
        }
        
        DevUtil.timePoint("jackzhou", "getMethod start url:" + url);
        HttpResponse response = getHttpClient().execute(method);
        DevUtil.timePoint("jackzhou", "getMethod end");

        entity = response.getEntity();

        //是否gzip压缩 处理不同
        Header contentEncoding = entity.getContentEncoding();
        if (contentEncoding != null && contentEncoding.getValue().contains("gzip")) {

            GZIPInputStream gzip = new GZIPInputStream(entity.getContent());
            StringBuilder outString = new StringBuilder();
            byte[] b = new byte[4096];
            int n = gzip.read(b);
            while (n != -1) {
                outString.append(new String(b, 0, n));
                n = gzip.read(b);
            }
            ret = outString.toString();
            DevUtil.v("jackzhou", "getMethod gzip response:" + String.valueOf(ret));
            
            //关闭流
            try{
                gzip.close();
                gzip = null;
            }catch (Exception e) {
                // do nothing
            }
     
        } else {

            ret = EntityUtils.toString(entity);
            DevUtil.timePoint("jackzhou", "getMethod response:" + String.valueOf(ret));
        }

        return ret;
    }

    public static String postMethod(String url, String params) throws ClientProtocolException, IOException {
        return postMethod(url, params, null);
    }
    
    public static String postMethod(String url, String params, HashMap<String, String> header) throws ClientProtocolException, IOException {
        HttpPost method = new HttpPost(url);

        //自定义header
        if(header != null){
            for(String key: header.keySet()){
                method.setHeader(key, header.get(key));
            }
        }
        
        if(params != null && params.trim().length() > 0){
            StringEntity se = new StringEntity(params, HTTP.UTF_8);
            method.setEntity(se);
        }

        DevUtil.v("jackzhou", "postMethod params:" + params);
        DevUtil.timePoint("jackzhou", "postMethod start url:" + url);
        HttpResponse response = getHttpClient().execute(method);
        DevUtil.timePoint("jackzhou", "postMethod end");

        String ret = EntityUtils.toString(response.getEntity());
        DevUtil.timePoint("jackzhou", "postMethod response:" + String.valueOf(ret));

        return ret;
    }

    /**
     * 为http请求添加cookie
     *
     * @param method
     *            http请求方法
     * @param url
     *            http请求的url
     * @param apiName
     *            需要添加cookie的api名
     * @param cookieKey
     * @param cookieValue
     */
//    private static void addCookiePostMethod(HttpPost method, String url, String apiName, String cookieKey, String cookieValue) {
//        if (url.contains(apiName)) {
//            // 加入cookie
//            Cookie cookie = new BasicClientCookie(cookieKey, cookieValue);
//            CookieSpecBase cookieSpecBase = new BrowserCompatSpec();
//            List<Cookie> cookies = new ArrayList<Cookie>();
//            cookies.add(cookie);
//            cookieSpecBase.formatCookies(cookies);
//            method.setHeader(cookieSpecBase.formatCookies(cookies).get(0));
//        }
//    }

    /**
     * 为http请求添加cookie
     *
     * @param method
     *            http请求方法
     * @param url
     *            http请求的url
     * @param apiName
     *            需要添加cookie的api名
     * @param cookieKey
     * @param cookieValue
     */
//    private static void addCookieGetMethod(HttpGet method, String url, String apiName, String cookieKey, String cookieValue) {
//        if (url.contains(apiName)) {
//            // 加入cookie
//            Cookie cookie = new BasicClientCookie(cookieKey, cookieValue);
//            CookieSpecBase cookieSpecBase = new BrowserCompatSpec();
//            List<Cookie> cookies = new ArrayList<Cookie>();
//            cookies.add(cookie);
//            cookieSpecBase.formatCookies(cookies);
//            method.setHeader(cookieSpecBase.formatCookies(cookies).get(0));
//        }
//    }

    public static String postMethod(String url, HashMap<String, String> params) throws ClientProtocolException, IOException {
        return postMethod(url, params, new HashMap<String, String>());
    }
    
    public static String postMethod(String url, HashMap<String, String> params, HashMap<String, String> header) throws ClientProtocolException, IOException {

        HttpPost method = new HttpPost(url);

        //自定义header
        if(header != null && !header.isEmpty()){
            for(String key: header.keySet()){
                method.setHeader(key, header.get(key));
            }
        }
        
        if (params.size() > 0) {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                nameValuePairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            method.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
        }
        DevUtil.v("jackzhou", "postMethod params:" + printHashMap(params));
        DevUtil.timePoint("jackzhou", "postMethod start url:" + url);
        HttpResponse response = getHttpClient().execute(method);
        DevUtil.timePoint("jackzhou", "postMethod end");

        String ret =  EntityUtils.toString(response.getEntity());
        DevUtil.timePoint("jackzhou", "postMethod response:" + String.valueOf(ret));

        return ret;
    }

    public static String postMethod(String url, HashMap<String, String> params, String filePath, int disconnectTime) throws FileNotFoundException {
        return postMethod(url, params, "file", new File(filePath), disconnectTime);
    }
    public static String postMethod(String url, HashMap<String, String> params, String filePath) throws FileNotFoundException {
        return postMethod(url, params, "file", new File(filePath));
    }


    public static String postMethod(String actionUrl, HashMap<String, String> params, String paramsFilename, File file) throws FileNotFoundException {
        return postMethod(actionUrl, params, paramsFilename, file, 20000);
    }
    public static String postMethod(String actionUrl, HashMap<String, String> params, String paramsFilename, File file, int disconnectTime) throws FileNotFoundException {
        FileInputStream fis = new FileInputStream(file);

        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        String res = null;

        try {
            URL url = new URL(actionUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
            con.setRequestMethod("POST");
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Charset", "UTF-8");
            con.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            con.setConnectTimeout(10*1000);
            
            //在wifi连接到cmcc网络(网络可以访问，并会将你重定向到他们自己的登陆页)下测试时，.setReadTimeout及下面手写超时终止均无效
            //待更改，更改文件上传方式。不使用HttpURLConnection
            con.setReadTimeout(20*1000);
            new Thread(new InterruptThread(con, disconnectTime)).start();//.setReadTimeout()无用。手写设置超时终止

            
            
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                sb.append(twoHyphens + boundary + end);
                sb.append("Content-Disposition:form-data;name=" + entry.getKey() + end + end + entry.getValue() + end);
            }
            sb.append(twoHyphens + boundary + end);
            sb.append("Content-Disposition:form-data;"
                    + "name=\""+paramsFilename+"\";filename=\"" + file.getName() + "\"" + end);
            sb.append(end);

            DataOutputStream ds = new DataOutputStream(con.getOutputStream());
            ds.write(sb.toString().getBytes());

            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int length = -1;
            while ((length = fis.read(buffer)) != -1) {
                ds.write(buffer, 0, length);
            }
            ds.writeBytes(end);
            ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
            fis.close();
            ds.flush();

            InputStream is = con.getInputStream();
            int ch;
            StringBuffer b = new StringBuffer();
            while ((ch = is.read()) != -1) {
                b.append((char) ch);
            }

            res = b.toString().trim();
            ds.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
    public static class InterruptThread implements Runnable {
        HttpURLConnection con;
        int time;
        
        public InterruptThread(HttpURLConnection con, int time) {
            this.con = con;
            this.time = time;
        }

        public void run() {
            try {
                Thread.sleep(time);
//                Log.v("", "11111111-forcing disconnect start");
                con.disconnect();
//                Log.v("", "11111111-forcing disconnect end");
            } catch (Exception e) {

            }
           
        }
    }

    /**
     * 得到httpclient对象，每次都是新对象
     * @param connTimeOut 连接超时时间  单位秒
     * @param soTimeOut 读取超时时间 单位秒
     * @return
     */
    private static HttpClient getHttpClient(int connTimeOut, int soTimeOut) {
            HttpParams params = new BasicHttpParams();

            // Turn off stale checking. Our connections break all the time
            // anyway,
            // and it's not worth it to pay the penalty of checking every time.
            HttpConnectionParams.setStaleCheckingEnabled(params, false);

            // Default connection and socket timeout of 20 seconds. Tweak to
            // taste.
            HttpConnectionParams.setConnectionTimeout(params, connTimeOut * 1000);
            HttpConnectionParams.setSoTimeout(params, soTimeOut * 1000);
            HttpConnectionParams.setSocketBufferSize(params, 8192);

            // Don't handle redirects -- return them to the caller. Our code
            // often wants to re-POST after a redirect, which we must do
            // ourselves.

            // DOES WE NEED REDIRECTING?
            HttpClientParams.setRedirecting(params, true);

            // Set the specified user agent and register standard protocols.
            HttpProtocolParams.setUserAgent(params, HttpUtil.class.getName());
            SchemeRegistry schemeRegistry = new SchemeRegistry();
            schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
            schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));

            ClientConnectionManager manager = new ThreadSafeClientConnManager(params, schemeRegistry);

            _httpClient = new DefaultHttpClient(manager, params);

        return _httpClient;
    }

    /**
     * 得到httpclient对象，每次返回第一次建的对象
     * @return
     */
    public synchronized static HttpClient getHttpClient() {
        if (_httpClient == null) {
            _httpClient = getHttpClient(20, 20);
        }

        return _httpClient;
    }

    /**
     * url encode
     * @param str
     * @return
     */
    public static String urlEncode(String str) {
        if (str != null)
            return URLEncoder.encode(str);
        else
            return "";
    }

    private static String printHashMap(HashMap<String, String> map){

        String ret = "[";

        for(String temp: map.keySet()){
                ret += temp + "=" + map.get(temp) + "  ";
            }

        ret += "]";
        return ret;
    }
}
