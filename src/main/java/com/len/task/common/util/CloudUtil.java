package com.len.task.common.util;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.len.task.common.constant.ServerConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;

import javax.net.ssl.SSLContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
public class CloudUtil {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");

    /**
     * 获取http请求中的数据流
     *
     * @param request
     * @return 数据流
     */
    public static String getRequestString(HttpServletRequest request) {
        String dataString = null;
        try {
            StringBuffer requestString = new StringBuffer();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(request.getInputStream(), "UTF-8"));
            String tmpString = "";
            while ((tmpString = reader.readLine()) != null) {
                requestString.append(tmpString);
            }
            reader.close();
            dataString = requestString.toString().trim();
        } catch (IOException e) {
            log.error("exception:" + e.getMessage());
            e.printStackTrace();
        }
        return dataString;
    }

    /**
     * 判断请求是否超时
     *
     * @param timestamp 请求时间戳
     * @return false:未超时 true:超时
     */
    public static boolean isOverTime(String timestamp) {
        try {
            long request_timestamp = Long.parseLong(timestamp);
            log.info("request_time:"
                    + new Date(request_timestamp).toString());
            long current_timestamp = System.currentTimeMillis();
            log.info("current_time:"
                    + new Date(current_timestamp).toString());
            long diff_timestamp = 5 * 60 * 1000;

            long timediff = (current_timestamp - request_timestamp) > 0 ? (current_timestamp - request_timestamp)
                    : (request_timestamp - current_timestamp);

            log.info("timediff:" + timediff);
            if (timediff > diff_timestamp) {
                return true;
            }
            return false;
        } catch (Exception e) {
            log.error("exception:" + e.getMessage());
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 按照字典升序拼接连接字符串
     *
     * @param params 需要拼接的字符串map
     * @return 拼接后的字符串
     */
    public static String createLinkString(Map<String, String> params) {

        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);

        String prestr = "";
        for (int i = 0; i < keys.size(); i++) {

            String key = keys.get(i);
            String value = params.get(key);

            if (i == keys.size() - 1) {
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }
        return prestr;
    }

    /**
     * 除去数组中的空值和签名参数
     *
     * @param sArray 签名参数组
     * @return 去掉空值与签名参数后的新签名参数组
     */
    public static Map<String, String> paraFilter(Map<String, String> sArray) {

        Map<String, String> result = new HashMap<String, String>();

        if (sArray == null || sArray.size() <= 0) {
            return result;
        }

        for (String key : sArray.keySet()) {
            String value = sArray.get(key);
            if (value == null || value.equals("")
                    || key.equalsIgnoreCase("sign")
                    || key.equalsIgnoreCase("sign_type")) {
                continue;
            }
            result.put(key, value);
        }

        return result;
    }

    /**
     * 校验同方参数加密
     *
     * @param map
     * @param key
     * @return
     */
    public static String signParam(Map<String, String> map, String key) {
        log.info("签名key:" + key);
        String linkString = createLinkString(map);
        String source = linkString + "&" + "key=" + key;
        log.info("加密前字符串:" + source);
        String signString = MD5Tool.md5Encryption(source);
        log.info("加密后字符串:" + signString);
        return signString;
    }

    /**
     * 创建订单号
     *
     * @return 基于当前日期的订单号
     */
    public static String getOrderId() {
        StringBuffer result = new StringBuffer();
        result.append(sdf.format(new Date()));
        Random r = new Random();
        for (int i = 0; i < 10; i++) {
            result.append(r.nextInt(10));
        }
        return result.toString();
    }

    /**
     * 获取几个月后的日期
     *
     * @param date     初始日期
     * @param duration 月份间隔
     * @return 几个月后的日期
     */
    public static Date getMonthLater(Date date, int duration) {
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(date);
        // 日期加duration个月
        rightNow.add(Calendar.MONTH, duration);
        Date laterDate = rightNow.getTime();
        return laterDate;
    }

    /**
     * 获取几个月后的日期
     *
     * @param date     初始日期
     * @param duration 月份间隔
     * @return 几个月后的日期
     */
    public static Date getEndDate(Date date, int duration, int unit) {
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(date);
        // 日期加duration个月
        if (unit == 1) {
            rightNow.add(Calendar.DATE, duration);
        }
        if (unit == 2) {
            rightNow.add(Calendar.HOUR, duration);
        }
        Date laterDate = rightNow.getTime();
        return laterDate;
    }

    /**
     * 获取客户端IP
     *
     * @param request
     * @return 客户端IP
     */
    public static String getClientIp(HttpServletRequest request) {
        String internerIp = request.getRemoteAddr();
        if (StringUtils.isBlank(internerIp)) {
            internerIp = request.getRemoteHost();
        }
        return internerIp;
    }

    /**
     * 输出json到客户端
     *
     * @param response
     * @param outputjson
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    public static void writeJsonResult(HttpServletResponse response,
                                       JSONObject outputjson) throws IOException {
        PrintWriter out = new PrintWriter(new OutputStreamWriter(
                response.getOutputStream(), "utf-8"));
        out.print(outputjson);
        out.flush();
        out.close();
    }

    /**
     * 输出字符串到客户端
     *
     * @param response
     * @param result
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    public static void writeStringResult(HttpServletResponse response,
                                         String result) throws IOException {
        log.info("写入到客户端的数据：" + result);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(
                response.getOutputStream(), "utf-8"));
        out.print(result);
        out.flush();
        out.close();
    }


    /**
     * 比较版本号的大小,前者大则返回一个正数,后者大返回一个负数,相等则返回0
     *
     * @param version1
     * @param version2
     * @return
     */
    public static int compareVersion(String version1, String version2) {
        if (version1 == null || version2 == null) {
            return 1;
        }
        // 注意此处为正则匹配，不能用"."；
        String[] versionArray1 = version1.split("\\.");
        String[] versionArray2 = version2.split("\\.");
        int idx = 0;
        // 取最小长度值
        int minLength = Math.min(versionArray1.length, versionArray2.length);
        int diff = 0;
        // 先比较长度
        // 再比较字符
        while (idx < minLength && (diff = versionArray1[idx].length() - versionArray2[idx].length()) == 0
                && (diff = versionArray1[idx].compareTo(versionArray2[idx])) == 0) {
            ++idx;
        }
        // 如果已经分出大小，则直接返回，如果未分出大小，则再比较位数，有子版本的为大；
        diff = (diff != 0) ? diff : versionArray1.length - versionArray2.length;
        return diff;
    }

    /**
     * 检查参数是否完整
     *
     * @param params
     * @return
     */
    public static boolean checkParams(String... params) {

        for (String param : params) {
            if (StringUtils.isBlank(param)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 返回保存信息
     *
     * @param bool
     * @param msg
     */
    public static JSONObject buildAjaxOperationJson(boolean bool, String msg) {
        JSONObject resultJson = new JSONObject();
        if (bool) {
            resultJson.put("isCorrect", 1);
            resultJson.put("msg", msg);
        } else {
            resultJson.put("isCorrect", 2);
            resultJson.put("msg", msg);
        }
        return resultJson;
    }

    /**
     * 获取运营商名称
     *
     * @param oid
     * @return
     */
    public static String getOName(int oid) {
        String oname = "移动";
        switch (oid) {
            case 1:
                oname = "移动";
                break;
            case 2:
                oname = "联通";
                break;
            case 3:
                oname = "电信";
                break;
            case 4:
                oname = "支付宝";
                break;
            case 5:
                oname = "微信";
                break;
            case 6:
                oname = "宽带";
                break;
            default:
        }
        return oname;
    }

    /**
     * 记录查询条件
     *
     * @param condition
     */
    public static void logCondition(String[] condition) {
        StringBuffer conditionBuff = new StringBuffer();
        for (String con : condition) {
            conditionBuff.append(con + ",");
        }
        log.info("查询条件:" + conditionBuff);
    }


    /**
     * 创建免校验ssl连接
     *
     * @return
     */
    public static CloseableHttpClient createSSLClientDefault() {
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(
                    null, new TrustStrategy() {
                        // 信任所有
                        @Override
                        public boolean isTrusted(X509Certificate[] chain,
                                                 String authType) throws CertificateException {
                            return true;
                        }

                    }).build();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                    sslContext);
            return HttpClients.custom().setSSLSocketFactory(sslsf).build();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        return HttpClients.createDefault();
    }

    public static CloseableHttpClient createHttpsClient() {
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(
                    null, new TrustStrategy() {
                        // 信任所有
                        @Override
                        public boolean isTrusted(X509Certificate[] chain,
                                                 String authType) throws CertificateException {
                            return true;
                        }
                    }).build();
            final SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext,
                    NoopHostnameVerifier.INSTANCE);

            final Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", new PlainConnectionSocketFactory())
                    .register("https", sslsf)
                    .build();
            final PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry);
            cm.setMaxTotal(100);
            CloseableHttpClient httpClient = HttpClients.custom()
                    .setSSLSocketFactory(sslsf)
                    .setConnectionManager(cm)
                    .setHostnameVerifier(SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER)
                    .build();
            return httpClient;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        return HttpClients.createDefault();
    }

    /**
     * 输入错误信息
     *
     * @param resultMsg
     * @return
     */
    public static String buildErrorResponse(String resultMsg) {
        try {
            JSONObject json = new JSONObject();
            json.put("returnCode", 0);
            json.put("resultCode", 0);
            json.put("resultMsg", resultMsg);
            return json.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static double round(double v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The   scale   must   be   a   positive   integer   or   zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static boolean uploadFile(HttpServletRequest request, String file) {
        try {
            InputStream in = request.getInputStream();
            OutputStream out = new FileOutputStream(file);
            //创建一个缓冲区
            byte[] buffer = new byte[1024];
            //判断输入流中的数据是否已经读完的标识
            int len = 0;
            //循环将输入流读入到缓冲区当中，(len=in.read(buffer))>0就表示in里面还有数据
            while ((len = in.read(buffer)) > 0) {
                //使用FileOutputStream输出流将缓冲区的数据写入到指定的目录当中
                out.write(buffer, 0, len);
            }
            //关闭输入流
            in.close();
            //关闭输出流
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
            log.error("文件上传失败");
            return false;
        }
        log.error("文件上传成功");
        return true;
    }

    public static void downloadFile(String fileUrl, String filename, HttpServletResponse response) throws IOException {
        /* 读取文件 */
        File file = new File(fileUrl);
        /* 如果文件存在 */
        if (file.exists()) {
            response.reset();
            response.setContentType("application/octet-stream;charset=utf-8");
            response.addHeader("Content-Disposition", "attachment; filename='" + filename + "'");
            int fileLength = (int) file.length();
            response.setContentLength(fileLength);
            if (fileLength != 0) {
                InputStream inStream = new FileInputStream(file);
                byte[] buf = new byte[4096];
                ServletOutputStream servletOS = response.getOutputStream();
                int readLength;
                while (((readLength = inStream.read(buf)) != -1)) {
                    servletOS.write(buf, 0, readLength);
                }
                servletOS.flush();
                if (servletOS != null) {
                    servletOS.close();
                }
                if (inStream != null) {
                    inStream.close();
                }
            }
        }
    }


    public static List<NameValuePair> map2NamePairs(Map<String, String> sParaTemp) {
        List<NameValuePair> valuePairList = new ArrayList<>();
        for (String key : sParaTemp.keySet()) {
            log.info(key + "=" + sParaTemp.get(key));
            valuePairList.add(new BasicNameValuePair(key, sParaTemp.get(key)));
        }
        return valuePairList;
    }

    /**
     * 获取支付宝通知参数
     *
     * @param request
     * @return
     */
    public static Map<String, String> getAliNotifyMap(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<>();
        Enumeration<String> rnames = request.getParameterNames();
        for (Enumeration<String> e = rnames; e.hasMoreElements(); ) {
            String thisName = e.nextElement();
            String thisValue = request.getParameter(thisName);
            log.info("paramMap:" + thisName + "=" + thisValue);
            paramMap.put(thisName, thisValue);
        }
        return paramMap;
    }

    /**
     * @return
     * @Auther:xs
     * @Description:设置请求头
     * @Date: 10:20 2019/4/17
     */

    public static List<NameValuePair> makeHeaderAPPJSON() {
        List<NameValuePair> headers = new ArrayList<>();
        headers.add(new BasicNameValuePair("Accept", "application/json"));
        headers.add(new BasicNameValuePair("Content-Type", "application/json;chartset=UTF-8"));

        return headers;
    }


    /**
     * @Auther:xs
     * @Description:生成4位随机数 范围内数字(1000, 9999)
     * @Date: 17:02 2019/4/17
     */
    public static String fourRandom() {
        int ran = (int) (Math.random() * 8998) + 1000 + 1;
        return String.valueOf(ran);
    }


    public static String stringToBase64(String source) {
        try {
            return new Base64().encodeToString(source.getBytes(ServerConstant.UTF8));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return source;
    }

    public static boolean isJson(String json) {
        if (StringUtils.isBlank(json)) {
            return false;
        }
        try {
            new JsonParser().parse(json);
            return true;
        } catch (JsonParseException e) {
            return false;
        }
    }
}
