/**
 * Author:   claire
 * Date:    2022/10/21 - 4:03 下午
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2022/10/21 - 4:03 下午          V1.0.0
 */
package com.learning.doudian;

import com.alibaba.fastjson.JSON;
import com.doudian.open.api.order_batchDecrypt.OrderBatchDecryptRequest;
import com.doudian.open.api.order_batchDecrypt.OrderBatchDecryptResponse;
import com.doudian.open.api.order_batchDecrypt.param.CipherInfosItem;
import com.doudian.open.api.order_batchDecrypt.param.OrderBatchDecryptParam;
import com.doudian.open.api.order_logisticsAdd.OrderLogisticsAddRequest;
import com.doudian.open.api.order_logisticsAdd.OrderLogisticsAddResponse;
import com.doudian.open.api.order_logisticsAdd.param.OrderLogisticsAddParam;
import com.doudian.open.api.order_logisticsCompanyList.OrderLogisticsCompanyListRequest;
import com.doudian.open.api.order_logisticsCompanyList.OrderLogisticsCompanyListResponse;
import com.doudian.open.api.order_orderDetail.OrderOrderDetailRequest;
import com.doudian.open.api.order_orderDetail.OrderOrderDetailResponse;
import com.doudian.open.api.order_orderDetail.data.OrderOrderDetailData;
import com.doudian.open.api.order_orderDetail.data.ShopOrderDetail;
import com.doudian.open.api.order_orderDetail.param.OrderOrderDetailParam;
import com.doudian.open.api.order_review.OrderReviewRequest;
import com.doudian.open.api.order_review.OrderReviewResponse;
import com.doudian.open.api.order_review.param.OrderReviewParam;
import com.doudian.open.api.order_searchList.OrderSearchListRequest;
import com.doudian.open.api.order_searchList.OrderSearchListResponse;
import com.doudian.open.api.order_searchList.data.ShopOrderListItem;
import com.doudian.open.api.order_searchList.param.CombineStatusItem;
import com.doudian.open.api.order_searchList.param.OrderSearchListParam;
import com.doudian.open.api.token_create.TokenCreateRequest;
import com.doudian.open.api.token_create.TokenCreateResponse;
import com.doudian.open.api.token_create.param.TokenCreateParam;
import com.doudian.open.core.AccessToken;
import com.doudian.open.core.AccessTokenBuilder;
import com.doudian.open.core.GlobalConfig;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 功能简述
 * 〈〉
 *
 * @author claire
 * @date 2022/10/21 - 4:03 下午
 * @since 1.0.0
 */
public class OrderApiTest extends BaseJunitTest {

    @Test
    public void searchOrderList() {
        GlobalConfig.initAppKey("7156409536281069093");
        GlobalConfig.initAppSecret("ced33097-b847-4306-a7c8-5d8b86fc49dc");
//入参为shopId
        AccessToken accessToken = AccessTokenBuilder.build(65188572L);
        //123456是shopId

        OrderSearchListRequest request = new OrderSearchListRequest();
        OrderSearchListParam param = request.getParam();
        CombineStatusItem statusItem = new CombineStatusItem();
        statusItem.setMainStatus("2");
        statusItem.setOrderStatus("2");
        param.setCombineStatus(Arrays.asList(statusItem));
//        param.setProduct("3473196049974326153");
//        param.setBType(2L);
//        param.setAfterSaleStatusDesc("refund_success");
//        param.setTrackingNo("435435");
//        param.setPresellType(1L);
//        param.setOrderType(1L);
        param.setCreateTimeStart(1664553600L);
//        param.setCreateTimeEnd(1617355413L);
//        param.setAbnormalOrder(1L);
//        param.setTradeType(1L);
//        param.setUpdateTimeStart(1617355413L);
//        param.setUpdateTimeEnd(1617355413L);
        param.setSize(1L);
        param.setPage(0L);
        param.setOrderBy("create_time");
        param.setOrderAsc(false);
        OrderSearchListResponse response = request.execute(accessToken);
        System.out.println(response.toString());
    }

    @Test
    public void batchDecrypt() {
        GlobalConfig.initAppKey("7156409536281069093");
        GlobalConfig.initAppSecret("ced33097-b847-4306-a7c8-5d8b86fc49dc");
        AccessToken accessToken = AccessTokenBuilder.build(65188572L); //123456是shopId
//        OrderSearchListRequest request = new OrderSearchListRequest();
//        OrderSearchListParam param = request.getParam();
//        param.setCreateTimeStart(1664553600L);
//        param.setSize(1L);
//        param.setPage(0L);
//        param.setOrderBy("create_time");
//        param.setOrderAsc(false);
//        OrderSearchListResponse response = request.execute(accessToken);
//        List<ShopOrderListItem> shopOrderList = response.getData().getShopOrderList();
//        List<ShopOrderListItem> shopOrderList = JSON.parseArray(json,ShopOrderListItem.class);
        OrderOrderDetailRequest request = new OrderOrderDetailRequest();
        OrderOrderDetailParam param = request.getParam();
        param.setShopOrderId("4990796115875471270");
        OrderOrderDetailResponse response = request.execute(accessToken);
        List<CipherInfosItem> list = new ArrayList<>();

        OrderOrderDetailData data = response.getData();
        ShopOrderDetail item = data.getShopOrderDetail();
        CipherInfosItem i1 = new CipherInfosItem();
        i1.setAuthId(item.getOrderId());
        i1.setCipherText(item.getEncryptPostTel());

        CipherInfosItem i2 = new CipherInfosItem();
        i2.setAuthId(item.getOrderId());
        i2.setCipherText(item.getEncryptPostReceiver());

        System.out.println(item.getPostAddr().toString());

        CipherInfosItem i3 = new CipherInfosItem();
        i3.setAuthId(item.getOrderId());
        i3.setCipherText(item.getPostAddr().getEncryptDetail());

//        System.out.println(item.getUserIdInfo().toString());
//        CipherInfosItem i4 = new CipherInfosItem();
//        i4.setAuthId(item.getOrderId());
//        i4.setCipherText(item.getUserIdInfo().getEncryptIdCardName());
//
//        CipherInfosItem i5 = new CipherInfosItem();
//        i5.setAuthId(item.getOrderId());
//        i5.setCipherText(item.getUserIdInfo().getEncryptIdCardNo());

        list.add(i1);
        list.add(i2);
        list.add(i3);
//        list.add(i4);
//        list.add(i5);

        OrderBatchDecryptRequest request2 = new OrderBatchDecryptRequest();
        OrderBatchDecryptParam param2 = request2.getParam();
//        param.setAccountId("dy1001");
//        param.setAccountType("main_account");
        param2.setCipherInfos(list);
        OrderBatchDecryptResponse response2 = request2.execute(accessToken);
        System.out.println(response2.toString());
    }

    @Test
    public void testUpdateOrder() {
//        GlobalConfig.initAppKey("7156409536281069093");
//        GlobalConfig.initAppSecret("ced33097-b847-4306-a7c8-5d8b86fc49dc");
//        AccessToken accessToken = AccessTokenBuilder.build(65188572L);
//
//        OrderReviewRequest request = new OrderReviewRequest();
//        OrderReviewParam param = request.getParam();
//        param.setTaskType(3001L);
//        param.setRejectCode(0L);
//        param.setObjectId("4991418980614205412");
//        OrderReviewResponse response = request.execute(accessToken);

        TokenCreateRequest request = new TokenCreateRequest();
        TokenCreateParam param = request.getParam();
        param.setCode("82bdc687-eff1-4f63-8444-0b43086c25fd");
        param.setGrantType("authorization_code");
        param.setTestShop("1");
        param.setShopId("1111117239");
        param.setAuthId("11234");
        param.setAuthSubjectType("WuLiuShang");
        AccessToken accessToken = AccessTokenBuilder.build(65188572L);
        TokenCreateResponse response = request.execute(accessToken);
    }

    @Test
    public void testGetDetail(){
        GlobalConfig.initAppKey("7156409536281069093");
        GlobalConfig.initAppSecret("ced33097-b847-4306-a7c8-5d8b86fc49dc");
        AccessToken accessToken = AccessTokenBuilder.build(65188572L);
        OrderOrderDetailRequest request = new OrderOrderDetailRequest();
        OrderOrderDetailParam param = request.getParam();
        param.setShopOrderId("4992822326811109088");
        OrderOrderDetailResponse response = request.execute(accessToken);
        OrderBatchDecryptRequest request2 = new OrderBatchDecryptRequest();
        OrderBatchDecryptParam param2 = request2.getParam();
        List<CipherInfosItem> list = new ArrayList<>();

        OrderOrderDetailData data = response.getData();
        ShopOrderDetail item = data.getShopOrderDetail();
        CipherInfosItem i1 = new CipherInfosItem();
        i1.setAuthId(item.getOrderId());
        i1.setCipherText(item.getEncryptPostTel());

        CipherInfosItem i2 = new CipherInfosItem();
        i2.setAuthId(item.getOrderId());
        i2.setCipherText(item.getEncryptPostReceiver());

        System.out.println(item.getPostAddr().toString());

        CipherInfosItem i3 = new CipherInfosItem();
        i3.setAuthId(item.getOrderId());
        i3.setCipherText(item.getPostAddr().getEncryptDetail());

        CipherInfosItem i4 = new CipherInfosItem();
        i4.setAuthId(item.getOrderId());
        i4.setCipherText(item.getUserIdInfo().getEncryptIdCardName());

        CipherInfosItem i5 = new CipherInfosItem();
        i5.setAuthId(item.getOrderId());
        i5.setCipherText(item.getUserIdInfo().getEncryptIdCardNo());


        list.add(i1);
        list.add(i2);
        list.add(i3);
        list.add(i4);
        list.add(i5);

        param2.setCipherInfos(list);
        OrderBatchDecryptResponse response2 = request2.execute(accessToken);
        System.out.println(response2.toString());
    }

    @Test
    public void testLogisticApi(){
        GlobalConfig.initAppKey("7156409536281069093");
        GlobalConfig.initAppSecret("ced33097-b847-4306-a7c8-5d8b86fc49dc");
        AccessToken accessToken = AccessTokenBuilder.build(65188572L);

        OrderLogisticsAddRequest request = new OrderLogisticsAddRequest();
        OrderLogisticsAddParam param = request.getParam();
        param.setOrderId("4992851128865133842");
//        param.setLogisticsId(0L);
//        param.setCompany("顺丰公司");
        param.setCompanyCode("shunfeng");
        param.setLogisticsCode("sf1231231231234");
//        param.setIsRefundReject(false);
//        param.setIsRejectRefund(false);
//        param.setSerialNumberList(["546443524543534","646443524543534"]);
//        param.setAddressId(6L);
//        param.setStoreId(111L);
        OrderLogisticsAddResponse response = request.execute(accessToken);
        System.out.println(response.toString());
    }

    @Test
    public void testLoadLogisticsCompanies(){
        OrderLogisticsCompanyListRequest request = new OrderLogisticsCompanyListRequest();
        GlobalConfig.initAppKey("7156409536281069093");
        GlobalConfig.initAppSecret("ced33097-b847-4306-a7c8-5d8b86fc49dc");
        AccessToken accessToken = AccessTokenBuilder.build(65188572L);
        OrderLogisticsCompanyListResponse companyListResponse = request.execute(accessToken);
        System.out.println(companyListResponse.toString());
    }
}
