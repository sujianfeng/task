package com.len.task.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;

@Slf4j
public class HttpClientHelper {

    private static HttpClientHelper instance = new HttpClientHelper();

    static {
        instance.init();
        instance.idleConnectionMonitorThread = instance.new IdleConnectionMonitorThread();
        instance.idleConnectionMonitorThread.start();
    }

    private volatile boolean shutdown;
    private PoolingHttpClientConnectionManager cm = null;
    private CloseableHttpClient httpClient = null;
    private IdleConnectionMonitorThread idleConnectionMonitorThread = null;
    private RequestConfig defaultRequestConfig = RequestConfig.custom()
            .setConnectionRequestTimeout(20000) // 获取连接时间
            .setConnectTimeout(30000) // 连接时间
            .setSocketTimeout(50000) // 传输时间
            .build();// 设置请求和传输超时时间
    private ConnectionConfig defaultConnectionConfig = ConnectionConfig
            .custom().setCharset(Consts.UTF_8).build();

    public static HttpClientHelper getInstance() {
        return instance;
    }

    public static String doGet(String url, List<NameValuePair> headers,
                               List<NameValuePair> params, String charSet)
            throws IOException {
        return instance.get(url, headers, params, charSet);
    }

    public static String doGet(String url, List<NameValuePair> headers,
                               List<NameValuePair> params) throws ClientProtocolException,
            IOException {
        return instance.get(url, headers, params, Consts.UTF_8.name());
    }

    public static String doGet(String url, List<NameValuePair> headers)
            throws IOException {
        return instance.get(url, headers, null, Consts.UTF_8.name());
    }

    public static String doGet(String url) throws
            IOException {
        return instance.get(url, null, null, Consts.UTF_8.name());
    }

    public static String doPost(String url, List<NameValuePair> params,
                                String charSet) throws IOException {
        return instance.post(url, params, charSet);
    }

    public static String doPost(String url, List<NameValuePair> params)
            throws IOException {
        return instance.post(url, params, Consts.UTF_8.name());
    }

    public static String doPost(String url, List<NameValuePair> headers,
                                String charSet, String bodyString) throws
            IOException {
        return instance.post(url, headers, charSet, bodyString);
    }

    private void init() {
        cm = new PoolingHttpClientConnectionManager();

        cm.setMaxTotal(200);
        cm.setDefaultMaxPerRoute(100);
        httpClient = HttpClients.custom().setConnectionManager(cm)
                .setDefaultConnectionConfig(defaultConnectionConfig).build();
    }

    public String get(String url, List<NameValuePair> headers,
                      List<NameValuePair> params, String charSet)
            throws IOException {
        CloseableHttpResponse response = null;
        try {
            if (charSet == null) {
                charSet = Consts.UTF_8.name();
            }
            if (params != null && params.size() > 0) {
                String paramStr = URLEncodedUtils.format(params, charSet);
                url += url.indexOf("?") < 0 ? "?" : "&";
                url += paramStr;
            }
            HttpGet httpGet = new HttpGet(url);
            if (headers != null) {
                for (NameValuePair nvp : headers) {
                    httpGet.addHeader(nvp.getName(), nvp.getValue());
                }
            }
            httpGet.setConfig(defaultRequestConfig);
            response = httpClient.execute(httpGet);
            log.info("请求url：" + url);
            log.info("请求状态：" + response.getStatusLine().getStatusCode());
            if (response.getStatusLine().getStatusCode() == 200) { // 只处理状态为200
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    String result = EntityUtils.toString(entity, charSet);
                    log.info("返回数据：" + result);
                    return result;
                }
            }
            return null;
        } finally {
            HttpClientUtils.closeQuietly(response);
        }
    }

    public String post(String url, List<NameValuePair> params, String charSet)
            throws IOException {
        CloseableHttpResponse response = null;
        try {
            if (charSet == null) {
                charSet = Consts.UTF_8.name();
            }
            HttpPost httpPost = new HttpPost(url);
            if (params != null) {
                UrlEncodedFormEntity paramEntity = new UrlEncodedFormEntity(
                        params, charSet);
                httpPost.setEntity(paramEntity);
            }
            httpPost.setConfig(defaultRequestConfig);
            response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) { // 只处理状态为200
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    String result = EntityUtils.toString(entity, charSet);
                    log.info("返回数据：" + result);
                    return result;
                }
            }
            return null;
        } finally {
            HttpClientUtils.closeQuietly(response);
        }
    }

    public String post(String url, List<NameValuePair> headers, String charSet,
                       String bodyString) throws IOException {
        CloseableHttpResponse response = null;
        try {
            if (charSet == null) {
                charSet = Consts.UTF_8.name();
            }

            HttpPost httpPost = new HttpPost(url);

            if (headers != null) {
                for (NameValuePair nvp : headers) {
                    httpPost.addHeader(nvp.getName(), nvp.getValue());
                }
            }
            StringEntity stringEntity = new StringEntity(bodyString, charSet);
            httpPost.setEntity(stringEntity);
            response = httpClient.execute(httpPost);
            log.info("请求url：" + url);
            log.info("请求bodyString：" + bodyString);
            log.info("请求状态：" + response.getStatusLine().getStatusCode());
            if (response.getStatusLine().getStatusCode() == 200) { // 只处理状态为200
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    String result = EntityUtils.toString(entity, charSet);
                    log.info("返回数据：" + result);
                    return result;
                }
            }
            log.info("httpcode:" + response.getStatusLine().getStatusCode());

            return null;
        } finally {
            HttpClientUtils.closeQuietly(response);
        }
    }

    class IdleConnectionMonitorThread extends Thread {
        @Override
        public void run() {
            log.info("HttpClient 回收过期连接 线程开始...");
            try {
                while (!shutdown) {
                    synchronized (this) {
                        wait(5000);
                        cm.closeExpiredConnections();
                    }
                }
            } catch (InterruptedException ex) {
            }
        }

        public void shutdown() {
            shutdown = true;
            synchronized (this) {
                notifyAll();
            }
        }
    }

}
