package com.learning.wxpay;

import static org.apache.http.HttpHeaders.ACCEPT;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.apache.http.entity.ContentType.APPLICATION_JSON;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.function.Consumer;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.HttpHost;
import org.apache.http.util.EntityUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class HttpClientBuilderTest {

    private static final String merchantId = "1900009191"; // 商户号
    private static final String merchantSerialNumber = "1DDE55AD98ED71D6EDD4A4A16996DE7B47773A8C"; // 商户证书序列号
    private static final String requestBody = "{\n"
            + "    \"stock_id\": \"9433645\",\n"
            + "    \"stock_creator_mchid\": \"1900006511\",\n"
            + "    \"out_request_no\": \"20190522_001中文11\",\n"
            + "    \"appid\": \"wxab8acb865bb1637e\"\n"
            + "}";
    // 你的商户私钥
    private static final String privateKey = "-----BEGIN PRIVATE KEY-----\n"
            + "-----END PRIVATE KEY-----";
    // 你的微信支付平台证书
    private static final String certificate = "-----BEGIN CERTIFICATE-----\n"
            + "-----END CERTIFICATE-----";
    private CloseableHttpClient httpClient;

    private static final HttpHost proxy = null;

    @Before
    public void setup() {
        PrivateKey merchantPrivateKey = PemUtil.loadPrivateKey(privateKey);
        X509Certificate wechatPayCert = PemUtil.loadCertificate(
                new ByteArrayInputStream(certificate.getBytes(StandardCharsets.UTF_8)));

        ArrayList<X509Certificate> wechatPayCertificates = new ArrayList<>();
        wechatPayCertificates.add(wechatPayCert);

        httpClient = WechatPayHttpClientBuilder.create()
                .withMerchant(merchantId, merchantSerialNumber, merchantPrivateKey)
                .withWechatPay(wechatPayCertificates)
                .withProxy(proxy)
                .build();
    }

    public void createOrder() throws Exception{
        //请求URL
        HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/v3/pay/transactions/jsapi");

        // 请求body参数
        String reqdata = "{"
                + "\"amount\": {"
                + "\"total\": 100,"
                + "\"currency\": \"CNY\""
                + "},"
                + "\"mchid\": \"1900006891\","
                + "\"description\": \"Image形象店-深圳腾大-QQ公仔\","
                + "\"notify_url\": \"https://www.weixin.qq.com/wxpay/pay.php\","
                + "\"payer\": {"
                + "\"openid\": \"o4GgauE1lgaPsLabrYvqhVg7O8yA\"" + "},"
                + "\"out_trade_no\": \"1217752501201407033233388881\","
                + "\"goods_tag\": \"WXG\","
                + "\"appid\": \"wxdace645e0bc2c424\"" + "}";
        StringEntity entity = new StringEntity(reqdata,"utf-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");

        //完成签名并执行请求
        CloseableHttpResponse response = httpClient.execute(httpPost);
        try {
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                System.out.println("success,return body = " + EntityUtils.toString(response.getEntity()));
            } else if (statusCode == 204) {
                System.out.println("success");
            } else {
                System.out.println("failed,resp code = " + statusCode+ ",return body = " + EntityUtils.toString(response.getEntity()));
                throw new IOException("request failed");
            }
        } finally {
            response.close();
            httpClient.close();
        }
    }

    public void pageJsPay(){
//        function onBridgeReady() {
//            WeixinJSBridge.invoke('getBrandWCPayRequest', {
//                    "appId": "wx2421b1c4370ecxxx",   //公众号ID，由商户传入
//                    "timeStamp": "1395712654",   //时间戳，自1970年以来的秒数
//                    "nonceStr": "e61463f8efa94090b1f366cccfbbb444",      //随机串
//                    "package": "prepay_id=wx21201855730335ac86f8c43d1889123400",
//                    "signType": "RSA",     //微信签名方式：
//                    "paySign": "oR9d8PuhnIc+YZ8cBHFCwfgpaK9gd7vaRvkYD7rthRAZ\/X+QBhcCYL21N7cHCTUxbQ+EAt6Uy+lwSN22f5YZvI45MLko8Pfso0jm46v5hqcVwrk6uddkGuT+Cdvu4WBqDzaDjnNa5UK3GfE1Wfl2gHxIIY5lLdUgWFts17D4WuolLLkiFZV+JSHMvH7eaLdT9N5GBovBwu5yYKUR7skR8Fu+LozcSqQixnlEZUfyE55feLOQTUYzLmR9pNtPbPsu6WVhbNHMS3Ss2+AehHvz+n64GDmXxbX++IOBvm2olHu3PsOUGRwhudhVf7UcGcunXt8cqNjKNqZLhLw4jq\/xDg==" //微信签名
//      },
//            function(res) {
//                if (res.err_msg == "get_brand_wcpay_request:ok") {
//                    // 使用以上方式判断前端返回,微信团队郑重提示：
//                    //res.err_msg将在用户支付成功后返回ok，但并不保证它绝对可靠。
//                }
//            });
//        }
//        if (typeof WeixinJSBridge == "undefined") {
//            if (document.addEventListener) {
//                document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);
//            } else if (document.attachEvent) {
//                document.attachEvent('WeixinJSBridgeReady', onBridgeReady);
//                document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
//            }
//        } else {
//            onBridgeReady();
//        }
    }

//    public void payMsgCallback(){
//            // 构建request，传入必要参数
//            NotificationRequest request = new NotificationRequest.Builder().withSerialNumber(wechatPaySerial)
//                    .withNonce(nonce)
//                    .withTimestamp(timestamp)
//                    .withSignature(signature)
//                    .withBody(body)
//                    .build();
//            NotificationHandler handler = new NotificationHandler(verifier, apiV3Key.getBytes(StandardCharsets.UTF_8));
//            // 验签和解析请求体
//            Notification notification = handler.parse(request);
//            Assert.assertNotNull(notification);
//            System.out.println(notification.toString());
//    }

    @After
    public void after() throws IOException {
        httpClient.close();
    }

    @Test
    public void getCertificateTest() throws Exception {
        URIBuilder uriBuilder = new URIBuilder("https://api.mch.weixin.qq.com/v3/certificates");
        uriBuilder.setParameter("p", "1&2");
        uriBuilder.setParameter("q", "你好");

        HttpGet httpGet = new HttpGet(uriBuilder.build());

        doSend(httpGet, null, response -> assertEquals(SC_OK, response.getStatusLine().getStatusCode()));

    }

    @Test
    public void getCertificatesWithoutCertTest() throws Exception {
        PrivateKey merchantPrivateKey = PemUtil.loadPrivateKey(privateKey);

        httpClient = WechatPayHttpClientBuilder.create()
                .withMerchant(merchantId, merchantSerialNumber, merchantPrivateKey)
                .withValidator(response -> true)
                .build();

        getCertificateTest();
    }

    @Test
    public void postNonRepeatableEntityTest() throws IOException {
        HttpPost httpPost = new HttpPost(
                "https://api.mch.weixin.qq.com/v3/marketing/favor/users/oHkLxt_htg84TUEbzvlMwQzVDBqo/coupons");

        final byte[] bytes = requestBody.getBytes(StandardCharsets.UTF_8);
        final InputStream stream = new ByteArrayInputStream(bytes);
        doSend(httpPost, new InputStreamEntity(stream, bytes.length, APPLICATION_JSON),
                response -> assertTrue(response.getStatusLine().getStatusCode() != SC_UNAUTHORIZED));
    }

    @Test
    public void postRepeatableEntityTest() throws IOException {
        HttpPost httpPost = new HttpPost(
                "https://api.mch.weixin.qq.com/v3/marketing/favor/users/oHkLxt_htg84TUEbzvlMwQzVDBqo/coupons");

        doSend(httpPost, new StringEntity(requestBody, APPLICATION_JSON),
                response -> assertTrue(response.getStatusLine().getStatusCode() != SC_UNAUTHORIZED));
    }

    protected void doSend(HttpUriRequest request, HttpEntity entity, Consumer<CloseableHttpResponse> responseCallback)
            throws IOException {
        if (entity != null && request instanceof HttpPost) {
            ((HttpPost) request).setEntity(entity);
        }
        request.addHeader(ACCEPT, APPLICATION_JSON.toString());

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            responseCallback.accept(response);
        }
    }

}
