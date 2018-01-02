package com.trasen.top.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class HttpUtil {
    private static final Logger logger = Logger.getLogger(HttpUtil.class);
    public static final String ERROR = "error";

    public static String connectURL(String commString, String address) {
        String rec_string = "";
        URL url = null;
        HttpURLConnection urlConn = null;
        try {
            url = new URL(address);
            urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setConnectTimeout(1000 * 60 * 5);
            urlConn.setReadTimeout(1000 * 60 * 5);
            urlConn.setRequestMethod("POST");
            urlConn.setDoOutput(true);
            urlConn.setRequestProperty(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
            urlConn.setRequestProperty(HttpHeaders.CONTENT_ENCODING, "UTF-8");
            OutputStream out = urlConn.getOutputStream();
            out.write(commString.getBytes());
            out.flush();
            out.close();
            BufferedReader rd = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), "UTF-8"));
            StringBuffer sb = new StringBuffer();
            int ch;
            while ((ch = rd.read()) > -1) {
                sb.append((char) ch);
            }
            rec_string = sb.toString().trim();
            rd.close();
        } catch (Exception e) {
            logger.error("http请求提交异常信息：", e);
            rec_string = ERROR;
        } finally {
            if (urlConn != null) {
                urlConn.disconnect();
            }
        }
        return rec_string;
    }

    public static String connectURLGET(String address) {
        String rec_string = "";
        URL url = null;
        HttpURLConnection urlConn = null;
        try {
            url = new URL(address);
            urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setConnectTimeout(1000 * 60 * 5);
            urlConn.setReadTimeout(1000 * 60 * 5);
            urlConn.setRequestProperty(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
            urlConn.connect();
            BufferedReader rd = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), "UTF-8"));
            StringBuffer sb = new StringBuffer();
            int ch;
            while ((ch = rd.read()) > -1) {
                sb.append((char) ch);
            }
            rec_string = sb.toString().trim();
            rd.close();
        } catch (Exception e) {
            logger.info("http请求连接异常", e);
        } finally {
            if (urlConn != null) {
                urlConn.disconnect();
            }
        }
        return rec_string;
    }


    public static String connectURLGETByQuery(String address) {
        String rec_string = "";
        URL url = null;
        HttpURLConnection urlConn = null;
        try {
            url = new URL(address);
            urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setConnectTimeout(1000 * 60 * 5);
            urlConn.setReadTimeout(1000 * 60 * 5);
            urlConn.setRequestProperty(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
            urlConn.setRequestProperty(HttpHeaders.HOST,"query.dcs.shuyun.com");
            urlConn.setRequestProperty(HttpHeaders.USER_AGENT,"Java/1.7.0_40");
            urlConn.connect();
            BufferedReader rd = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), "UTF-8"));
            StringBuffer sb = new StringBuffer();
            int ch;
            while ((ch = rd.read()) > -1) {
                sb.append((char) ch);
            }
            rec_string = sb.toString().trim();
            rd.close();
        } catch (Exception e) {
            logger.info("http请求连接异常", e);
        } finally {
            if (urlConn != null) {
                urlConn.disconnect();
            }
        }
        return rec_string;
    }

    public static String connectURLGET(String address, String jsonstr) {
        String result = "";
        URL url = null;
        HttpURLConnection conn = null;
        try {
            url = new URL(address);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(1000 * 60 * 5);
            conn.setReadTimeout(1000 * 60 * 5);
            conn.setDoOutput(true);
            conn.setRequestMethod("GET");
            conn.setRequestProperty(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
            OutputStream out = conn.getOutputStream();
            out.write(jsonstr.getBytes("UTF-8"));
            out.flush();
            out.close();

            String resCode = new Integer(conn.getResponseCode()).toString();
            logger.info("http请求响应码:" + resCode);
            InputStream input = resCode.startsWith("2") ? conn.getInputStream() : conn.getErrorStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
            result = reader.readLine();
            logger.info("http请求返回数据:" + result);
        } catch(Exception e) {
            logger.error("http提交请求异常,", e);
        } finally {
            if(conn != null) {
                conn.disconnect();
            }
        }
        return result;
    }

    public static String connectURL(String commString, String address, String code) {
        String rec_string = "";
        URL url = null;
        HttpURLConnection urlConn = null;
        try {
            url = new URL(address);
            urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setConnectTimeout(1000 * 60 * 5);
            urlConn.setReadTimeout(1000 * 60 * 5);
            urlConn.setRequestMethod("POST");
            urlConn.setDoOutput(true);
            OutputStream out = urlConn.getOutputStream();
            out.write(commString.getBytes());
            out.flush();
            out.close();
            BufferedReader rd = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), code));
            StringBuffer sb = new StringBuffer();
            int ch;
            while ((ch = rd.read()) > -1) {
                sb.append((char) ch);
            }
            rec_string = sb.toString().trim();
            rd.close();
        } catch (Exception e) {
            logger.error("http请求提交异常信息：", e);
            rec_string = ERROR;
        } finally {
            if (urlConn != null) {
                urlConn.disconnect();
            }
        }
        return rec_string;
    }

    public static String connectURLtoCookie(String address, String JSESSIONID) {
        String rec_string = "";
        URL url = null;
        HttpURLConnection urlConn = null;
        try {
            url = new URL(address);
            urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setRequestProperty("Cookie", "JSESSIONID=" + JSESSIONID);
            urlConn.setConnectTimeout(1000 * 60 * 5);
            urlConn.setReadTimeout(1000 * 60 * 5);
            urlConn.connect();
            BufferedReader rd = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), "UTF-8"));
            StringBuffer sb = new StringBuffer();
            int ch;
            while ((ch = rd.read()) > -1) {
                sb.append((char) ch);
            }
            rec_string = sb.toString().trim();
            rd.close();
        } catch (Exception e) {
            logger.info("http请求连接异常", e);
        } finally {
            if (urlConn != null) {
                urlConn.disconnect();
            }
        }
        return rec_string;
    }

    public static Map<String, Object> connectURLMap(String address, String jsonstr) {
        String result = "";
        Integer responseCode = null;
        URL url = null;
        HttpURLConnection conn = null;
        try {
            url = new URL(address);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(1000 * 60 * 5);
            conn.setReadTimeout(1000 * 60 * 5);
            conn.setDoOutput(true);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
            OutputStream out = conn.getOutputStream();
            out.write(jsonstr.getBytes());
            out.flush();
            out.close();

            responseCode = conn.getResponseCode();
            logger.info("http请求响应码:" + responseCode);
            InputStream input = String.valueOf(responseCode).startsWith("2") ? conn.getInputStream() : conn.getErrorStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
            result = reader.readLine();
            logger.info("http请求返回数据:" + result);
        } catch (Exception e) {
            logger.error("http提交请求异常,", e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        Map<String, Object> map = Maps.newHashMap();
        map.put("resCode", responseCode);
        map.put("result", result);
        return map;
    }

    public static void main(String[] args) {
        /*String str = HttpUtil.connectURLtoCookie("http://passport.fenxibao.com/account/kjtest/user/10000347/department/shops/","629e3c6b1b43b9a73e714708a1760b179b2b4f66");
        String str2 = HttpUtil.connectURLGET("http://passport.fenxibao.com/account/kjtest/user/10000347/department/shops/");
        if (str != null) {

            System.out.printf(str2);
        }*/



    }

    /**
     * @param requestUr
     * @param requestHeaderParam
     * @param postParam
     * @return
     */
    public static String connectPostWithParamAndHeader(String requestUr, Map<String, String> requestHeaderParam, Map<String, Object> postParam) {
        OutputStream outputStream = null;
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(requestUr);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            Set<Map.Entry<String, String>> entries = requestHeaderParam.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                urlConnection.setRequestProperty(entry.getKey(), entry.getValue());
            }
            urlConnection.setRequestProperty(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
            urlConnection.setRequestProperty(HttpHeaders.CONTENT_ENCODING, "UTF-8");
            urlConnection.setRequestProperty("connection", "Keep-Alive");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.connect();
            outputStream = urlConnection.getOutputStream();
            outputStream.write(JSON.toJSONString(postParam).getBytes());
            outputStream.flush();
            inputStream = urlConnection.getInputStream();
            byte[] buffer = new byte[1024];
            int index = 0;
            StringBuilder bufferString = new StringBuilder();
            while ((index = inputStream.read(buffer)) != -1) {
                bufferString.append(new String(buffer, 0, index, "UTF-8"));
            }
            return bufferString.toString();
        } catch (Exception e) {
            logger.error("获取 推荐商品信息 [失败] 错误信息 " + e.getMessage());
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                logger.info("关闭输入流 或 输出流 或 连接 出现异常 异常信息 : " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * 上传文件
     * @param urlStr
     * @param textMap
     * @param fileMap
     * @return
     */
    public static String formUpload(String urlStr, Map<String, String> textMap, Map<String, String> fileMap, String JSESSIONID) {
        String res = "";
        HttpURLConnection conn = null;
        String BOUNDARY = "---------------------------123821742118716"; //boundary就是request头和上传文件内容的分隔符
        try {
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            //连接超时时间10秒
            conn.setConnectTimeout(10000);
            //获取服务端返回数据超时时间30秒
            conn.setReadTimeout(30000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
            conn.setRequestProperty("Cookie", "JSESSIONID=" + JSESSIONID);

            OutputStream out = new DataOutputStream(conn.getOutputStream());
            // text
            if (textMap != null) {
                StringBuffer strBuf = new StringBuffer();
                Iterator<Map.Entry<String, String>> iter = textMap.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry<String, String> entry = iter.next();
                    String inputName = (String) entry.getKey();
                    String inputValue = (String) entry.getValue();
                    if (inputValue == null) {
                        continue;
                    }
                    strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
                    strBuf.append("Content-Disposition:form-data;name=\"" + inputName + "\"\r\n\r\n");
                    strBuf.append(inputValue);
                }
                out.write(strBuf.toString().getBytes("utf-8"));
            }

            // file
            if (fileMap != null) {
                Iterator<Map.Entry<String, String>> iter = fileMap.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry<String, String> entry = iter.next();
                    String inputName = (String) entry.getKey();
                    String inputValue = (String) entry.getValue();
                    if (inputValue == null) {
                        continue;
                    }
                    File file = new File(inputValue);
                    String filename = file.getName();
                    StringBuffer strBuf = new StringBuffer();
                    strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
                    strBuf.append("Content-Disposition:form-data;name=\"" + inputName + "\"; filename=\"" + filename + "\"\r\n");
                    strBuf.append("Content-Type:" + "text/plain" + "\r\n\r\n");

                    out.write(strBuf.toString().getBytes());

                    DataInputStream in = new DataInputStream(new FileInputStream(file));
                    int bytes = 0;
                    byte[] bufferOut = new byte[1024];
                    while ((bytes = in.read(bufferOut)) != -1) {
                        out.write(bufferOut, 0, bytes);
                    }
                    in.close();
                }
            }
            byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
            out.write(endData);
            out.flush();
            out.close();
            int resCode = conn.getResponseCode();
            if (resCode == HttpURLConnection.HTTP_OK) {
                // 读取返回数据
                StringBuffer strBuf = new StringBuffer();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    strBuf.append(line).append("\n");
                }
                res = strBuf.toString();
                reader.close();
                reader = null;
            }else {
                res= String.valueOf(resCode);
            }
        } catch (Exception e) {
            logger.error("发送POST请求出错===" + e);
        } finally {
            if (conn != null) {
                conn.disconnect();
                conn = null;
            }
        }
        return res;
    }

    /**
     * GET请求 带queryParam参数
     * @param urlPath http://qiushi6-ccms.fenxibao.com/node/marketingondemand/param/check?campaignName=sdesws
     * @param params
     * @param enc 编码格式
     * @return res
     * @throws IOException
     */
    public static String sendGetRequest(String urlPath, Map<String, String> params, String enc, String JSESSIONID) throws IOException {
        String res="";
        StringBuilder sb = new StringBuilder(urlPath);
        sb.append('?');
        for(Map.Entry<String, String> entry : params.entrySet()) {
            sb.append(entry.getKey()).append('=').append(URLEncoder.encode(entry.getValue(), enc)).append('&');
        }
        sb.deleteCharAt(sb.length()-1);
        HttpURLConnection conn=null;
        try {
            URL url = new URL(sb.toString());
            conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");    //设置方法为GET
            conn.setReadTimeout(5 * 1000);   //设置过期时间为5秒
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Accept-Charset", "UTF-8");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
            conn.setRequestProperty("contentType", "UTF-8");
            conn.setRequestProperty("Cookie", "JSESSIONID=" + JSESSIONID);
            // 读取返回数据
            StringBuffer strBuf = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
            String line = null;
            while ((line = reader.readLine()) != null) {
                strBuf.append(line).append("\n");
            }
            res = strBuf.toString();
            reader.close();
            reader = null;
        } catch (Exception e){
            logger.error("发送GET请求出错===" + e);
        }finally {
            if (conn != null) {
                conn.disconnect();
                conn = null;
            }
        }
        return res;
    }

    public static String connectPostURL(String commString, String address, String JSESSIONID, String tenantId) {
        String rec_string = "";
        URL url = null;
        HttpURLConnection urlConn = null;
        try {
            url = new URL(address);
            urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setConnectTimeout(1000 * 60 * 5);
            urlConn.setReadTimeout(1000 * 60 * 5);
            urlConn.setRequestMethod("POST");
            urlConn.setDoOutput(true);
            urlConn.setRequestProperty(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
            urlConn.setRequestProperty(HttpHeaders.CONTENT_ENCODING, "UTF-8");
            urlConn.setRequestProperty(HttpHeaders.ACCEPT_ENCODING,"gzip, deflate");
            urlConn.setRequestProperty(HttpHeaders.ACCEPT,MediaType.APPLICATION_JSON);
            urlConn.setRequestProperty("Cookie", "JSESSIONID=" + JSESSIONID+";tenantId="+tenantId);
            OutputStream out = urlConn.getOutputStream();
            out.write(commString.getBytes());
            out.flush();
            out.close();
            BufferedReader rd = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), "UTF-8"));
            StringBuffer sb = new StringBuffer();
            int ch;
            while ((ch = rd.read()) > -1) {
                sb.append((char) ch);
            }
            rec_string = sb.toString().trim();
            rd.close();
        } catch (Exception e) {
            logger.error("http请求提交异常信息：", e);
            rec_string = ERROR;
        } finally {
            if (urlConn != null) {
                urlConn.disconnect();
            }
        }
        return rec_string;
    }
    public static String connectGetURL(String address, String JSESSIONID, String tenantId) {
        String rec_string = "";
        URL url = null;
        HttpURLConnection urlConn = null;
        try {
            url = new URL(address);
            urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setConnectTimeout(1000 * 60 * 5);
            urlConn.setReadTimeout(1000 * 60 * 5);
            urlConn.setRequestProperty(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
            urlConn.setRequestProperty(HttpHeaders.CONTENT_ENCODING, "UTF-8");
            urlConn.setRequestProperty(HttpHeaders.ACCEPT_ENCODING,"gzip, deflate");
            urlConn.setRequestProperty(HttpHeaders.ACCEPT,MediaType.APPLICATION_JSON);
            urlConn.setRequestProperty("Cookie", "JSESSIONID=" + JSESSIONID+";tenantId="+tenantId);
            urlConn.connect();
            BufferedReader rd = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), "UTF-8"));
            StringBuffer sb = new StringBuffer();
            int ch;
            while ((ch = rd.read()) > -1) {
                sb.append((char) ch);
            }
            rec_string = sb.toString().trim();
            rd.close();
        } catch (Exception e) {
            logger.info("http请求连接异常", e);
        } finally {
            if (urlConn != null) {
                urlConn.disconnect();
            }
        }
        return rec_string;
    }
    /**
     * 发起https请求并获取结果
     *
     * @param requestUrl 请求地址
     * @param requestMethod 请求方式（GET、POST）
     * @param outputStr 提交的数据
     * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
     */
    public static JSONObject handleRequest(String requestUrl, String requestMethod, String outputStr) {
        JSONObject jsonObject = null;
        HttpsURLConnection conn = null;
        try {
            URL url = new URL(requestUrl);
            conn = (HttpsURLConnection) url.openConnection();
            SSLContext ctx = SSLContext.getInstance("SSL", "SunJSSE");
            TrustManager[] tm = {new MyX509TrustManager()};
            ctx.init(null, tm, new SecureRandom());
            SSLSocketFactory sf = ctx.getSocketFactory();
            conn.setSSLSocketFactory(sf);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod(requestMethod);
            conn.setUseCaches(false);

            if ("GET".equalsIgnoreCase(requestMethod)) {
                conn.connect();
            }

            if (StringUtils.isNotEmpty(outputStr)) {
                OutputStream out = conn.getOutputStream();
                out.write(outputStr.getBytes("utf-8"));
                out.close();
            }

            InputStream in = conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in,"utf-8"));
            StringBuffer buffer = new StringBuffer();
            String line = null;

            while ((line = br.readLine()) != null) {
                buffer.append(line);
            }

            in.close();
            conn.disconnect();

            jsonObject = (JSONObject) JSONObject.parse(buffer.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            logger.error("URL错误！");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }finally {
            if (conn != null) {
                conn.disconnect();
                conn = null;
            }
        }
        return jsonObject;
    }
}
