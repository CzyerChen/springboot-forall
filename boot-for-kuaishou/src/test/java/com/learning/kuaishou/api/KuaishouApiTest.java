/**
 * Author:   claire
 * Date:    2023/7/31 - 9:51 上午
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2023/7/31 - 9:51 上午          V1.0.0
 */
package com.learning.kuaishou.api;

import com.alibaba.fastjson.JSON;
import com.kuaishou.merchant.open.api.KsMerchantApiException;
import com.kuaishou.merchant.open.api.client.AccessTokenKsMerchantClient;
import com.kuaishou.merchant.open.api.client.oauth.OauthAccessTokenKsClient;
import com.kuaishou.merchant.open.api.client.oauth.OauthCredentialKsClient;
import com.kuaishou.merchant.open.api.domain.industry.DecryptParam;
import com.kuaishou.merchant.open.api.domain.order.DecryptBaseMetaInfo;
import com.kuaishou.merchant.open.api.request.industry.OpenIndustryVirtualOrderDecryptRequest;
import com.kuaishou.merchant.open.api.request.industry.OpenIndustryVirtualOrderDetailRequest;
import com.kuaishou.merchant.open.api.request.industry.OpenIndustryVirtualOrderReviewRequest;
import com.kuaishou.merchant.open.api.request.item.OpenItemGetRequest;
import com.kuaishou.merchant.open.api.request.order.OpenOrderCursorListRequest;
import com.kuaishou.merchant.open.api.request.order.OpenOrderDecryptBatchRequest;
import com.kuaishou.merchant.open.api.request.order.OpenOrderDetailRequest;
import com.kuaishou.merchant.open.api.request.order.OpenSellerOrderGoodsDeliverRequest;
import com.kuaishou.merchant.open.api.response.industry.OpenIndustryVirtualOrderDecryptResponse;
import com.kuaishou.merchant.open.api.response.industry.OpenIndustryVirtualOrderDetailResponse;
import com.kuaishou.merchant.open.api.response.industry.OpenIndustryVirtualOrderReviewResponse;
import com.kuaishou.merchant.open.api.response.item.OpenItemGetResponse;
import com.kuaishou.merchant.open.api.response.oauth.KsAccessTokenResponse;
import com.kuaishou.merchant.open.api.response.oauth.KsCredentialResponse;
import com.kuaishou.merchant.open.api.response.order.OpenOrderCursorListResponse;
import com.kuaishou.merchant.open.api.response.order.OpenOrderDecryptBatchResponse;
import com.kuaishou.merchant.open.api.response.order.OpenOrderDetailResponse;
import com.kuaishou.merchant.open.api.response.order.OpenSellerOrderGoodsDeliverResponse;
import com.learning.kuaishou.BaseJunitTest;
import com.learning.kuaishou.config.KuaishouConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能简述
 * 〈〉
 *
 * @author claire
 * @date 2023/7/31 - 9:51 上午
 * @since 1.0.0
 */
@Slf4j
public class KuaishouApiTest extends BaseJunitTest {

    @Autowired
    private KuaishouConfig kuaishouConfig;

    @Test
    public void testAccessTokenWithoutUserAuth() {
        OauthCredentialKsClient oauthCredentialKsClient = new OauthCredentialKsClient(kuaishouConfig.getAppKey(), kuaishouConfig.getAppSecret());
        // 生成AccessToken
        try {
            KsCredentialResponse response = oauthCredentialKsClient.getAccessToken();
            System.out.println(JSON.toJSONString(response));
            //{"accessToken":"-","expiresIn":172800,"result":1}
        } catch (KsMerchantApiException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAccessTokenWithUserAuth() {
        //指定服务器地址
        OauthAccessTokenKsClient oauthAccessTokenKsClient
                = new OauthAccessTokenKsClient(kuaishouConfig.getAppKey(), kuaishouConfig.getAppSecret(), kuaishouConfig.getServerUrl());
//不指定服务器地址，服务器地址默认为线上
//        OauthAccessTokenKsClient oauthAccessTokenKsClient
//                = new OauthAccessTokenKsClient(appKey, appSecret);

// 生成AccessToken
        try {
            KsAccessTokenResponse response
                    = oauthAccessTokenKsClient.getAccessToken(/*kuaishouConfig.getGrantCode()*/"5d5eae5da93aa4434335c4d3e9a1773d00dcee5cc65b965a72674c988b85699f63da3057");
            System.out.println(JSON.toJSONString(response));
//            {"accessToken":"-","expiresIn":172800,"openId":"f18759ac87a28ea514bf5724a5df5751","refreshToken":"ChJvYXV0aC5yZWZyZXNoVG9rZW4SwAFlRdyyTw_4eTSzGPNDEMavH79GVzfDcAD99dSK4qjwogwJtXSYiLp08hW80qgbfeuCPD5aztwGJ25eMhMTwm5z1zrWqWuWK35R6H5t94EGIM3zXyHb929fPBeAVyx0poDhcp8pikNfmDUznff4KKZDO2za6elrtyUK16fwz-thcnadHQ1SkdPZPeHtfolSmqYH3HWj8_zAjd8Q6uLY-VI5pgGqh8YxA6JY-ZnGTWQGVYZV2uFQAkOYCNId1awVxWEaEnBjJ5eZAsf9_PiQ6uLIZTWyVyIgNvLHkLGnnttBcxxTb0EQuPpdawRpfcLP4MyIKk00gSEoBTAB","refreshTokenExpiresIn":15552000,"result":1,"scopes":["merchant_shop","merchant_funds","merchant_refund","merchant_item","merchant_order","user_info","merchant_video","merchant_material","merchant_promotion","merchant_user","merchant_comment","merchant_logistics"]}
//            {
//                "accessToken": "-",
//                    "expiresIn": 172800,
//                    "openId": "",
//                    "refreshToken": "---",
//                    "refreshTokenExpiresIn": 15552000,
//                    "result": 1,
//                    "scopes": [
//                "merchant_shop",
//                        "merchant_funds",
//                        "merchant_refund",
//                        "merchant_item",
//                        "merchant_order",
//                        "user_info",
//                        "merchant_video",
//                        "merchant_material",
//                        "merchant_promotion",
//                        "merchant_user",
//                        "merchant_comment",
//                        "merchant_logistics"
//  ]
//            }
        } catch (KsMerchantApiException e) {
            e.printStackTrace();
        }

//        String refreshToken = "your app refreshToken";

// 刷新AccessToken
//        try {
//            KsAccessTokenResponse response
//                    = oauthAccessTokenKsClient.refreshAccessToken(refreshToken);
//            System.out.println(JSON.toJSONString(response));
//        } catch (KsMerchantApiException e) {
//            e.printStackTrace();
//        }
    }

//    {
//        "code": "807000",
//            "data": {
//        "beginTime": 0,
//                "cursor": "",
//                "endTime": 0,
//                "orderList": [],
//        "pageSize": 0
//    },
//        "errorMsg": "endTime无效",
//            "msg": "业务处理失败",
//            "requests": {
//        "http": "https://openapi.kwaixiaodian.com/open/order/cursor/list?access_token=ChFvYXV0aC5hY2Nlc3NUb2tlbhJwyD4YuOCNgr8bn-6vTynn33Zu82F2AF7Qn8DTfcBtky5yh7qFKkz_lXjILBEfLMTbimjsmWhulO6YTkv5B0s6FZBFWUU5HcjakqOXcNGSHGcUCUo_Z5uR9KjLivg1DdSOe7ugXVu5f84rbIdNCB6IdBoSiw1mSBTjSXC8E9eh6DgO295qIiDCToLKiAzClegvHxxcfFIOWhSbZS9idfdPLkXO7vIFuCgFMAE&method=open.order.cursor.list&param=%7B%22orderViewStatus%22%3A1%2C%22pageSize%22%3A10%2C%22sort%22%3A1%2C%22queryType%22%3A1%2C%22beginTime%22%3A1543817629000%2C%22endTime%22%3A1543817629000%2C%22cpsType%22%3A1%2C%22cursor%22%3A%22157356441188_2021345676543%22%7D&sign=5fc0ff74017f48e91cb62bf3ea26157f&appkey=ks660340295617067960&version=1&signMethod=MD5&timestamp=1690797000021"
//    },
//        "result": 2001,
//            "subCode": "2001",
//            "subMsg": "endTime无效",
//            "success": false
//    }
    @Test
    public void testQueryList() {

//服务器地址为空时，默认是线上环境地址
// 对应授权商家快手账号
        long sellerId = 1L;

        AccessTokenKsMerchantClient client = new AccessTokenKsMerchantClient(kuaishouConfig.getServerUrl(), kuaishouConfig.getAppKey(), kuaishouConfig.getSignSecret());

        OpenOrderCursorListRequest request = new OpenOrderCursorListRequest();
        request.setAccessToken("-");
        request.setApiMethodVersion(1L);

        request.setOrderViewStatus(1);
        request.setPageSize(10);
        request.setSort(1);
        request.setQueryType(1);
        request.setBeginTime(1690387200000L);
        request.setEndTime(1690794000000L);
        request.setCpsType(1);
        request.setCursor("");
//        request.setCursor("157356441188_2021345676543");

        try {
            OpenOrderCursorListResponse response = client.execute(request);

            System.out.println(JSON.toJSONString(response));
        } catch (Exception e) {
            log.error("err", e);
        }
    }

    @Test
    public void testLogisticsAdd() throws KsMerchantApiException {
        AccessTokenKsMerchantClient client = new AccessTokenKsMerchantClient(kuaishouConfig.getServerUrl(), kuaishouConfig.getAppKey(), kuaishouConfig.getSignSecret());

        OpenSellerOrderGoodsDeliverRequest request = new OpenSellerOrderGoodsDeliverRequest();
        request.setAccessToken("-");
        request.setApiMethodVersion(1L);

        request.setOrderId(2321300034950000L);
        request.setExpressNo("SF1515601210470");
        request.setExpressCode(4);
//        request.setReturnAddressId(295440542222);

        OpenSellerOrderGoodsDeliverResponse response = client.execute(request);

        System.out.println(JSON.toJSONString(response));
    }

    @Test
    public void testOrderDetail() throws KsMerchantApiException {

        AccessTokenKsMerchantClient client = new AccessTokenKsMerchantClient(kuaishouConfig.getServerUrl(), kuaishouConfig.getAppKey(), kuaishouConfig.getSignSecret());

        OpenOrderDetailRequest request = new OpenOrderDetailRequest();
        request.setAccessToken("-");
        request.setApiMethodVersion(1L);
        request.setOid(2321300034950000L);

        OpenOrderDetailResponse response = client.execute(request);

        System.out.println(JSON.toJSONString(response));
    }


    @Test
    public void testBatchDecrypt() throws KsMerchantApiException {
        AccessTokenKsMerchantClient client = new AccessTokenKsMerchantClient(kuaishouConfig.getServerUrl(), kuaishouConfig.getAppKey(), kuaishouConfig.getSignSecret());
        OpenOrderDetailRequest request2 = new OpenOrderDetailRequest();
        request2.setAccessToken("-");
        request2.setApiMethodVersion(1L);
        request2.setOid(2321300034950000L);

        OpenOrderDetailResponse response2 = client.execute(request2);

        System.out.println(JSON.toJSONString(response2));

        OpenOrderDecryptBatchRequest request = new OpenOrderDecryptBatchRequest();
        request.setAccessToken("-");
        request.setApiMethodVersion(1L);

        List<DecryptBaseMetaInfo> list1 = new ArrayList<>();
        DecryptBaseMetaInfo obj1 = new DecryptBaseMetaInfo();
        obj1.setEncryptedData(response2.getData().getOrderAddress().getEncryptedAddress());
        obj1.setBizId("2321300034950000");
        list1.add(obj1);

        DecryptBaseMetaInfo obj2 = new DecryptBaseMetaInfo();
        obj2.setEncryptedData(response2.getData().getOrderAddress().getEncryptedConsignee());
        obj2.setBizId("2321300034950000");
        list1.add(obj2);

        DecryptBaseMetaInfo obj3 = new DecryptBaseMetaInfo();
        obj3.setEncryptedData(response2.getData().getOrderAddress().getEncryptedMobile());
        obj3.setBizId("2321300034950000");
        list1.add(obj3);
        request.setBatchDecryptList(list1);

        OpenOrderDecryptBatchResponse response = client.execute(request);

        System.out.println(JSON.toJSONString(response));
    }

    @Test
    public void testBatchDecrypt2() throws KsMerchantApiException {
        AccessTokenKsMerchantClient client = new AccessTokenKsMerchantClient(kuaishouConfig.getServerUrl(), kuaishouConfig.getAppKey(), kuaishouConfig.getSignSecret());
        OpenOrderDetailRequest request2 = new OpenOrderDetailRequest();
//        request2.setAccessToken("-");
        request2.setAccessToken("----");
        request2.setApiMethodVersion(1L);
        request2.setOid(2321300034950000L);

        OpenOrderDetailResponse response2 = client.execute(request2);

        System.out.println(JSON.toJSONString(response2));

//        AccessTokenKsMerchantClient client = new AccessTokenKsMerchantClient(kuaishouConfig.getServerUrl(), kuaishouConfig.getAppKey(), kuaishouConfig.getSignSecret());
//
        OpenIndustryVirtualOrderDetailRequest request3 = new OpenIndustryVirtualOrderDetailRequest();
        request3.setAccessToken("----");
        request3.setApiMethodVersion(1L);
        request3.setOrderId(2321300034950000L);

        OpenIndustryVirtualOrderDetailResponse response3 = client.execute(request3);

        System.out.println(JSON.toJSONString(response3));

//        OpenIndustryVirtualOrderDecryptRequest request = new OpenIndustryVirtualOrderDecryptRequest();
//        request.setAccessToken("ChFvYXV0aC5hY2Nlc3NUb2tlbhJAgyD8bwdLoECH9HUg0AIDb7JaQ2FXcaaE6p6SpgEluktKTS7FFVr0z39z_-SeN8aFVUS0UbD50Wz-uCniGdX48hoSEwetz-evQ5KoZWoBsttSCXB-IiD2SPqDGOYjCziF2nrn31jNTcsr-LHk5GFGvHT9LS8uFCgFMAE");
//        request.setApiMethodVersion(1L);
//
//        List<DecryptParam> list1 = new ArrayList<>();
//        DecryptParam obj1 = new DecryptParam();
//        obj1.setEncryptedData(response2.getData().getOrderAddress().getEncryptedAddress());
//        obj1.setSceneType(1);
//        list1.add(obj1);
//
//        DecryptParam obj2 = new DecryptParam();
//        obj2.setEncryptedData(response2.getData().getOrderAddress().getEncryptedConsignee());
//        obj2.setSceneType(1);
//        list1.add(obj2);
//
//        DecryptParam obj3 = new DecryptParam();
//        obj3.setEncryptedData(response2.getData().getOrderAddress().getEncryptedMobile());
//        obj3.setSceneType(1);
//        list1.add(obj3);
//
//        DecryptParam obj4 = new DecryptParam();
//        obj4.setEncryptedData(response3.getData().getUserAuthenticationList().get(0).getIdCard());
//        obj4.setSceneType(1);
//        list1.add(obj4);
//
//        DecryptParam obj5 = new DecryptParam();
//        obj5.setEncryptedData(response3.getData().getUserAuthenticationList().get(0).getName());
//        obj5.setSceneType(1);
//        list1.add(obj5);
//
//        request.setDecryptList(list1);
//        request.setOrderId(2321300034950000L);
//
//        OpenIndustryVirtualOrderDecryptResponse response = client.execute(request);
//
//        System.out.println(JSON.toJSONString(response));
    }

    @Test
    public void testReview() throws KsMerchantApiException {
        AccessTokenKsMerchantClient client = new AccessTokenKsMerchantClient(kuaishouConfig.getServerUrl(), kuaishouConfig.getAppKey(), kuaishouConfig.getSignSecret());

        OpenIndustryVirtualOrderReviewRequest request = new OpenIndustryVirtualOrderReviewRequest();
        request.setAccessToken("-");
        request.setApiMethodVersion(1L);

        request.setReviewCode(200001);
        request.setOrderId(2321300034950000L);

        OpenIndustryVirtualOrderReviewResponse response = client.execute(request);

        System.out.println(JSON.toJSONString(response));
    }


    @Test
    public void testQueryProduct() throws KsMerchantApiException {
        AccessTokenKsMerchantClient client = new AccessTokenKsMerchantClient(kuaishouConfig.getServerUrl(), kuaishouConfig.getAppKey(), kuaishouConfig.getSignSecret());

        OpenItemGetRequest request = new OpenItemGetRequest();
        request.setAccessToken("ChFvYXV0aC5hY2Nlc3NUb2tlbhJAnDYJL9ZOnSrhVKnuR0XQxiWmNHxh70Aa6ooh88R83LwwIg-vHc2rRhJw09mpZPIfnZo0VfHqky_u96PGxSGNbRoSi-8_Ne3MSVWVS-0YrnO5pgCsIiCE8Ua9XfqWLZy7ThTVDtKbRL5LtlxB2SegpsKx8o-zwSgFMAE");
        request.setApiMethodVersion(1L);
                  //ChFvYXV0aC5hY2Nlc3NUb2tlbhJwSdIhUxfcxLXAYnpdQBerFRhgdQUfLAYwf09oBhUVe8h6oEHz2KWKH1ROR4hWDXsT8shR1gAF9mv1JN1ju1R-pwOfCqhzNfdkYa81yYGIXOZsA_JhmEUaYeKRTfMSRw0jE71dI4FNtEd0852iEdmPvBoSNobUQXjSSbKFNqfffV1RYgsfIiDyMhA4bz0pdteupvvxNfvSOIuwDy2Cz2HtebF-IoD40CgFMAE
        request.setKwaiItemId(21122311903819L);

        OpenItemGetResponse response = client.execute(request);

        System.out.println(JSON.toJSONString(response));
    }
}
