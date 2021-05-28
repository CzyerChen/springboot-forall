package com.demo.util;


import com.demo.bean.KeyValue;
import com.demo.exception.HttpException;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class HttpUtils {
    private static Logger logger = LoggerFactory.getLogger(HttpUtils.class);
    public final static CloseableHttpClient httpsClient = initHttpsClient();
    public final static CloseableHttpClient httpClient = HttpClients.createDefault();
    public static final int MAX_TIMEOUT = 15000;
    public static final String CHARSET_UTF8 = "UTF-8";
    public static final String CHARSET_GB2312 = "GB2312";
    public static final String CHARSET_GBK = "GBK";

    /**
     * 发送HTTP GET请求
     *
     * @param url 请求地址
     * @return 请求后返回的数据
     */
    public static String get(String url) {
        return execute(httpClient, new HttpGet(url));
    }

    public static String get(String url, String encoding) {
        return execute(httpClient, new HttpGet(url), encoding);
    }

    /**
     * 发送HTTPS GET请求
     *
     * @param url 请求地址
     * @return 请求后返回的数据
     */
    public static String gets(String url) {
        return execute(httpsClient, new HttpGet(url));
    }

    /**
     * 发送HTTP GET请求
     *
     * @param url 请求地址
     * @return 请求后返回的数据
     */
    public static String get(String url, Map<String, String> args) {
        String serialize = serialize(args);
        String urlArgs = appendToUrl(url, serialize);
        return execute(httpClient, new HttpGet(urlArgs));
    }

    /**
     * 发送HTTPS GET请求
     *
     * @param url 请求地址
     * @return 请求后返回的数据
     */
    public static String gets(String url, Map<String, String> args) {
        String serialize = serialize(args);
        String urlArgs = appendToUrl(url, serialize);
        return execute(httpsClient, new HttpGet(urlArgs));
    }

    /**
     * 发送HTTP GET请求
     *
     * @param url 请求地址
     * @return 请求后返回的数据
     */
    public static String get(String url, KeyValue... args) {
        String serialize = serialize(args);
        String urlArgs = appendToUrl(url, serialize);
        return execute(httpClient, new HttpGet(urlArgs));
    }

    /**
     * 发送HTTP GET请求
     *
     * @param url 请求地址
     * @return 请求后返回的数据
     */
    public static String get(String url,Integer timeOut, KeyValue... args) {
        String serialize = serialize(args);
        String urlArgs = appendToUrl(url, serialize);
        HttpGet httpGet=new HttpGet(urlArgs);
        timeOut=timeOut==null?MAX_TIMEOUT:timeOut;
        //设置请求和传输超时时间
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeOut).setConnectTimeout(timeOut).build();
        httpGet.setConfig(requestConfig);
        return execute(httpClient, httpGet);
    }


    /**
     * 发送HTTP GET请求，必须响应JSON数据，并且将JSON数据转换成对于的类型
     *
     * @param url   请求地址
     * @param clazz 响应类型的Class
     * @param <T>   响应结果
     * @return 响应结果
     */
    public static <T> T get(String url, Class<T> clazz) {
        return Utils.json(get(url), clazz);
    }

    /**
     * 发送HTTPS GET请求，必须响应JSON数据，并且将JSON数据转换成对于的类型
     *
     * @param url   请求地址
     * @param clazz 响应类型的Class
     * @param <T>   响应结果
     * @return 响应结果
     */
    public static <T> T gets(String url, Class<T> clazz) {
        return Utils.json(gets(url), clazz);
    }

    /**
     * 发送HTTP POST请求
     *
     * @param url 请求地址
     * @return 响应结果
     */
    public static String post(String url) {
        return execute(httpClient, new HttpPost(url));
    }

    /**
     * 发送HTTPS POST请求
     *
     * @param url 请求地址
     * @return 响应结果
     */
    public static String posts(String url) {
        return execute(httpsClient, new HttpPost(url));
    }

    /**
     * 发送HTTP POST请求
     *
     * @param url  请求地址
     * @param args POST请求传递的参数
     * @return
     */
    public static String post(String url, Map<String, String> args) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(adapter(args));
        return execute(httpClient, httpPost);
    }

    /**
     * 发送HTTPS POST请求
     *
     * @param url  请求地址
     * @param args POST请求传递的参数
     * @return
     */
    public static String posts(String url, Map<String, String> args) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(adapter(args));
        return execute(httpsClient, httpPost);
    }

    /**
     * 发送HTTP POST请求
     *
     * @param url  请求地址
     * @param args POST请求传递的参数
     * @return
     */
    public static String post(String url, KeyValue... args) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(adapter(args));
        return execute(httpClient, httpPost);
    }

    /**
     * 发送HTTP POST请求
     *
     * @param url      请求地址
     * @param encoding 字符编码
     * @param args     POST请求传递的参数
     * @return
     */
    public static String post(String url, String encoding, KeyValue... args) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(adapter(encoding, args));
        return execute(httpClient, httpPost, encoding);
    }

    public static String postHeader(String url, String encoding,KeyValue[] headers) {
        HttpPost httpPost = new HttpPost(url);
        for (KeyValue keyValue : headers){
            httpPost.setHeader(keyValue.getKey(),keyValue.getValue().toString());
        }
        return execute(httpClient, httpPost, encoding);
    }

    public static String postHeader(String url, String encoding,KeyValue[] headers, KeyValue... args) {
        HttpPost httpPost = new HttpPost(url);
        for (KeyValue keyValue : headers){
            httpPost.setHeader(keyValue.getKey(),keyValue.getValue().toString());
        }
        httpPost.setEntity(adapter(encoding, args));
        return execute(httpClient, httpPost, encoding);
    }

    /**
     * 发送HTTPS POST请求
     *
     * @param url  请求地址
     * @param args POST请求传递的参数
     * @return
     */
    public static String posts(String url, KeyValue... args) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(adapter(args));
        return execute(httpsClient, httpPost);
    }

    /**
     * 发送HTTP POST请求，响应类型必须是JSON
     *
     * @param url   请求地址
     * @param args  POST请求传递的参数
     * @param clazz 响应类型的Class
     * @param <T>   响应结果
     * @return 响应结果
     */
    public static <T> T post(String url, Class<T> clazz, KeyValue... args) {
        return Utils.json(post(url, args), clazz);
    }

    /**
     * 发送HTTPS POST请求，响应类型必须是JSON
     *
     * @param url   请求地址
     * @param args  POST请求传递的参数
     * @param clazz 响应类型的Class
     * @param <T>   响应结果
     * @return 响应结果
     */
    public static <T> T posts(String url, Class<T> clazz, KeyValue... args) {
        return Utils.json(posts(url, args), clazz);
    }

    /**
     * 发送HTTP POST请求，响应类型必须是JSON
     *
     * @param url   请求地址
     * @param args  POST请求传递的参数
     * @param clazz 响应类型的Class
     * @param <T>   响应结果
     * @return 响应结果
     */
    public static <T> T post(String url, Map<String, String> args, Class<T> clazz) {
        return Utils.json(post(url, args), clazz);
    }

    /**
     * 发送HTTPS POST请求，响应类型必须是JSON
     *
     * @param url   请求地址
     * @param args  POST请求传递的参数
     * @param clazz 响应类型的Class
     * @param <T>   响应结果
     * @return 响应结果
     */
    public static <T> T posts(String url, Map<String, String> args, Class<T> clazz) {
        return Utils.json(posts(url, args), clazz);
    }

    /**
     * 发送HTTP POST请求
     *
     * @param url     请求地址
     * @param content 请求内容，自动编码为UTF-8
     * @return
     */
    public static String post(String url, String content) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(new StringEntity(content, CHARSET_UTF8));
        logger.debug("http content: {}", content);
        return execute(httpClient, httpPost);
    }

    /**
     * 发送HTTPS POST请求
     *
     * @param url     请求地址
     * @param content 请求内容，自动编码为UTF-8
     * @return
     */
    public static String posts(String url, String content) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(new StringEntity(content, CHARSET_UTF8));
        logger.debug("https content: {}", content);
        return execute(httpsClient, httpPost);
    }

    /**
     * 发送HTTPS POST请求
     *
     * @param url     请求地址
     * @param content 请求内容，自动编码为UTF-8
     * @param clazz   响应结果类型
     * @return
     */
    public static <T> T posts(String url, String content, Class<T> clazz) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(new StringEntity(content, CHARSET_UTF8));
        logger.debug("https content: {}", content);
        return Utils.json(execute(httpsClient, httpPost), clazz);
    }

    /**
     * 发送HTTPS POST请求
     *
     * @param url     请求地址
     * @param content 请求内容，自动编码为UTF-8
     * @param clazz   响应结果类型
     * @return
     */
    public static <T> T postsUTF8(String url, String content, Class<T> clazz) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(new StringEntity(encoding(content), CHARSET_UTF8));
        logger.debug("https content: {}", content);
        return Utils.json(execute(httpsClient, httpPost), clazz);
    }

    /**
     * 发送HTTP POST请求
     *
     * @param url        请求地址
     * @param httpEntity 请求内容
     * @return
     */
    public static String post(String url, HttpEntity httpEntity) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(httpEntity);
        return execute(httpClient, httpPost);
    }

    /**
     * 发送HTTPS POST请求
     *
     * @param url        请求地址
     * @param httpEntity 请求内容
     * @return
     */
    public static String posts(String url, HttpEntity httpEntity) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(httpEntity);
        return execute(httpsClient, httpPost);
    }

    /**
     * 发送HTTP POST请求
     *
     * @param url        请求地址
     * @param httpEntity 请求内容
     * @param clazz      响应类型的CLASS
     * @param <T>
     * @return
     */
    public static <T> T post(String url, HttpEntity httpEntity, Class<T> clazz) {
        return Utils.json(post(url, httpEntity), clazz);
    }

    /**
     * 发送HTTPS POST请求
     *
     * @param url        请求地址
     * @param httpEntity 请求内容
     * @param clazz      响应类型的CLASS
     * @param <T>
     * @return
     */
    public static <T> T posts(String url, HttpEntity httpEntity, Class<T> clazz) {
        return Utils.json(posts(url, httpEntity), clazz);
    }

    /**
     * web应用销毁时，关闭httpClient,释放资源
     */
    public static void close() {
        try {
            httpClient.close();
            logger.debug("http utils close...");
        } catch (IOException e) {
            logger.error("http utils close error", e);
        }
        try {
            httpsClient.close();
            logger.debug("https utils close...");
        } catch (IOException e) {
            logger.error("https utils close error", e);
        }
    }

    static String execute(CloseableHttpClient client, HttpUriRequest request, String encoding) {
        InputStream inputStream = null;
        try (CloseableHttpResponse httpResponse = client.execute(request)) {
            HttpEntity entity = httpResponse.getEntity();
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                inputStream = entity.getContent();
                return IOUtils.toString(inputStream, encoding);
            } else {
                throw new HttpException("http request statusCode = " + statusCode);
            }
        } catch (IOException e) {
            throw new HttpException(e);
        } finally {
//            request.abort();
            IOUtils.closeQuietly(inputStream);
        }
    }

    static String execute(CloseableHttpClient client, HttpUriRequest request) {
        return execute(client, request, CHARSET_UTF8);
    }

    public static String encoding(String source) {
        return encoding(source, CHARSET_UTF8);
    }

    public static String encoding(String source, String encoding) {
        try {
            return new String(source.getBytes(encoding));
        } catch (UnsupportedEncodingException e) {
            logger.error("unsupported encoding", e);
            return source;
        }
    }

    private static String serialize(KeyValue... args) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (KeyValue kv : args) {
            if (i++ != 0) {
                sb.append("&");
            }
            sb.append(kv.getKey());
            sb.append("=");
            sb.append(kv.getValue());
        }
        return sb.toString();
    }

    private static String serialize(Map<String, String> args) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (String key : args.keySet()) {
            if (i++ != 0) {
                sb.append("&");
            }
            sb.append(key);
            sb.append("=");
            sb.append(args.get(key));
        }
        return sb.toString();
    }

    private static String appendToUrl(String url, String serialize) {
        int i = url.indexOf('?');
        if (i > 0) {
            return url + "&" + serialize;
        } else {
            return url + "?" + serialize;
        }
    }

    private static UrlEncodedFormEntity adapter(String encoding, KeyValue... args) {
        List<NameValuePair> params = new LinkedList<>();
        for (KeyValue kv : args) {
            params.add(new BasicNameValuePair(kv.getKey(), (String) kv.getValue()));
        }
        try {
            return new UrlEncodedFormEntity(params, encoding);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("编码格式不支持," + encoding, e);
        }
    }

    private static UrlEncodedFormEntity adapter(KeyValue... args) {
        return adapter(CHARSET_UTF8, args);
    }

    private static UrlEncodedFormEntity adapter(Map<String, String> args) {
        List<NameValuePair> params = new LinkedList<>();
        args.forEach((key, val) -> params.add(new BasicNameValuePair(key, args.get(key))));
        try {
            return new UrlEncodedFormEntity(params, CHARSET_UTF8);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("编码格式不支持," + CHARSET_UTF8, e);
        }
    }

    private static CloseableHttpClient initHttpsClient() {
        PoolingHttpClientConnectionManager connMgr = new PoolingHttpClientConnectionManager();
        connMgr.setValidateAfterInactivity(30 * 1000);
        connMgr.setMaxTotal(10);
        connMgr.setDefaultMaxPerRoute(connMgr.getMaxTotal());
        RequestConfig.Builder configBuilder = RequestConfig.custom();
        configBuilder.setConnectTimeout(MAX_TIMEOUT);
        configBuilder.setSocketTimeout(MAX_TIMEOUT);
        configBuilder.setConnectionRequestTimeout(MAX_TIMEOUT);
        RequestConfig requestConfig = configBuilder.build();
        return HttpClients.custom().setSSLSocketFactory(createSSLConnSocketFactory()).setConnectionManager(connMgr)
                .setDefaultRequestConfig(requestConfig).build();
    }

    private static SSLConnectionSocketFactory createSSLConnSocketFactory() {
        SSLConnectionSocketFactory factory = null;
        try {
            TrustManager[] tm = {new MyX509TrustManager()};
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            factory = new SSLConnectionSocketFactory(sslContext, new DefaultHostnameVerifier());
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return factory;
    }

    static class MyX509TrustManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }

}