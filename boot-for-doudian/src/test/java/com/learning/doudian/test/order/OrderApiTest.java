/**
 * Author:   claire
 * Date:    2022/10/21 - 4:03 下午
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2022/10/21 - 4:03 下午          V1.0.0
 */
package com.learning.doudian.test.order;

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
import com.learning.doudian.test.BaseJunitTest;
import org.junit.Test;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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
        GlobalConfig.initAppKey("");
        GlobalConfig.initAppSecret("");
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
        GlobalConfig.initAppKey("");
        GlobalConfig.initAppSecret("");
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
        param.setCode("82bdc25fd");
        param.setGrantType("authorization_code");
        param.setTestShop("1");
        param.setShopId("1111117239");
        param.setAuthId("11234");
        param.setAuthSubjectType("WuLiuShang");
        AccessToken accessToken = AccessTokenBuilder.build(65188572L);
        TokenCreateResponse response = request.execute(accessToken);
    }

    @Test
    public void testGetDetail() {
        GlobalConfig.initAppKey("");
        GlobalConfig.initAppSecret("");
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
    public void testLogisticApi() {
        GlobalConfig.initAppKey("");
        GlobalConfig.initAppSecret("");
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
    public void testLoadLogisticsCompanies() {
        OrderLogisticsCompanyListRequest request = new OrderLogisticsCompanyListRequest();
        GlobalConfig.initAppKey("");
        GlobalConfig.initAppSecret("");
        AccessToken accessToken = AccessTokenBuilder.build(65188572L);
        OrderLogisticsCompanyListResponse companyListResponse = request.execute(accessToken);
        System.out.println(companyListResponse.toString());
    }

    @Test
    public void testShunfeng() {
        GlobalConfig.initAppKey("");
        GlobalConfig.initAppSecret("");
//入参为shopId
        AccessToken accessToken = AccessTokenBuilder.build(4463798L);
        //123456是shopId

//        OrderSearchListRequest request = new OrderSearchListRequest();
//        OrderSearchListParam param = request.getParam();
//        CombineStatusItem statusItem = new CombineStatusItem();
////        statusItem.setMainStatus("2");
//        statusItem.setOrderStatus("2");
//        param.setCombineStatus(Arrays.asList(statusItem));
//        param.setCreateTimeStart(1690387200L);
//        param.setProduct("3628337352675582020");
//        param.setSize(10L);
//        param.setPage(0L);
//        param.setOrderBy("create_time");
//        param.setOrderAsc(false);
//        OrderSearchListResponse response = request.execute(accessToken);
////        System.out.println(response.toString());
//        //3628333747008580975、3628337352675582020
//        if(!CollectionUtils.isEmpty(response.getData().getShopOrderList())){
//            for(ShopOrderListItem item: response.getData().getShopOrderList()){
//
        OrderOrderDetailRequest request2 = new OrderOrderDetailRequest();
        OrderOrderDetailParam param2 = request2.getParam();
        param2.setShopOrderId("6920457222469523168");
        OrderOrderDetailResponse response2 = request2.execute(accessToken);
        if (Objects.nonNull(response2.getData())) {
            if (!CollectionUtils.isEmpty(response2.getData().getShopOrderDetail().getSkuOrderList())) {
                boolean containShunfeng = response2.getData().getShopOrderDetail().getSkuOrderList().stream().anyMatch(d -> d.getSkuOrderTagUi().stream().anyMatch(dd -> "sf_free_shipping".equals(dd.getKey())));
                if (containShunfeng) {
                    OrderLogisticsAddRequest request3 = new OrderLogisticsAddRequest();
                    OrderLogisticsAddParam param3 = request3.getParam();
                    param3.setOrderId("6920457222469523168");
//        param.setLogisticsId(0L);
//        param.setCompany("顺丰公司");
                    param3.setCompanyCode("shunfeng");
                    param3.setLogisticsCode("sf1231231231234");
                    OrderLogisticsAddResponse response3 = request3.execute(accessToken);
                    System.out.println(response3.toString());
                }
            }
//                }
//                break;
//            }
        }
    }

    @Test
    public void testSecretKey() {
        GlobalConfig.initAppKey("");
        GlobalConfig.initAppSecret("");
        //入参为shopId
        AccessToken accessToken = AccessTokenBuilder.build(65188572L);
//        OrderOrderDetailRequest request = new OrderOrderDetailRequest();
//        OrderOrderDetailParam param = request.getParam();
//        param.setShopOrderId("6919983150035309994");
//        OrderOrderDetailResponse response = request.execute(accessToken);
//        System.out.println(JSON.toJSONString(response));
        String res = "{\"code\":\"10000\",\"data\":{\"shopOrderDetail\":{\"appId\":1128,\"appointmentShipTime\":0,\"authorCostAmount\":0,\"bType\":2,\"bTypeDesc\":\"抖音\",\"biz\":53,\"bizDesc\":\"通信卡\",\"buyerWords\":\"\",\"cancelReason\":\"\",\"channelPaymentNo\":\"TP2023071017551633520680982186\",\"createTime\":1688982924,\"dCarShopBizData\":{},\"doudianOpenId\":\"1@#t6Qnl+i8OtKuouEfE9oOeSgBb0vScZtYIgyeWip9ai3B4Romc3tYph7aPoAo+/Hf9JU=\",\"earliestReceiptTime\":0,\"earlyArrival\":false,\"encryptPostReceiver\":\"##Vdf16B2BcJhoN5nMyfHx0DIR4a2wjR2GXgTwUO9MMUaXy7ykRnbYtY0zzbuWeVeQdW8Niwjugo4s+nU0xN4gdSqxAUypQOQFY7k+GirkNI0=*CgkIARCtHCABKAESPgo8q+pX1zKbwLw9gg7ytgAiq09NCyln8RiDbTQi7uX7Jh1DNm+H0QhTNJkIg3/o1bPIFs82nxGKZFllDe+NGgA=#1##\",\"encryptPostTel\":\"$$0dVe9n00YVB9Cbk6oS0NUv01ghRNWmTlybtBVaN/iMpAwKVJvli6EKqbs9RXOItH1kvcy+MB1XvsOSP+7dUREr+MraaFs3mbzBn/1ehxA8wi6Q==*CgkIARCtHCABKAESPgo8/a978E7L4B92zbQplfixvW8ENBk1NOcdmi+ZHK22sELcL4nUSSaGVO9o4BNYhj8W8TV0CBQIj+GiCy9ZGgA=$1$$\",\"expShipTime\":1689155724,\"finishTime\":0,\"latestReceiptTime\":0,\"logisticsInfo\":[],\"mainStatus\":21,\"mainStatusDesc\":\"发货前退款完成\",\"maskPostAddr\":{\"city\":{\"id\":\"320500\",\"name\":\"苏州市\"},\"detail\":\"崇文路*****************栋\",\"province\":{\"id\":\"32\",\"name\":\"江苏省\"},\"street\":{\"id\":\"320571052\",\"name\":\"斜塘街道\"},\"town\":{\"id\":\"320506\",\"name\":\"吴中区\"}},\"maskPostReceiver\":\"花**\",\"maskPostTel\":\"1********50\",\"modifyAmount\":0,\"modifyPostAmount\":0,\"onlyPlatformCostAmount\":0,\"openId\":\"\",\"orderAmount\":201,\"orderExpireTime\":1800,\"orderId\":\"6919983150035309994\",\"orderLevel\":2,\"orderPhaseList\":[],\"orderStatus\":4,\"orderStatusDesc\":\"已关闭\",\"orderType\":0,\"orderTypeDesc\":\"普通订单\",\"packingAmount\":0,\"payAmount\":201,\"payTime\":1688982938,\"payType\":8,\"platformCostAmount\":0,\"postAddr\":{\"city\":{\"id\":\"320500\",\"name\":\"苏州市\"},\"detail\":\"\",\"encryptDetail\":\"##JUBgTxyzNOBekWgjE3zLo9U7bRkUE9O0CDBxPgQkUu6gWrPP7DM9KPspFZa+0I2hieEV35gEOK5S8i5YdTsZBYwUG+6r4vDdwwRv29pKH/x3kNK34FDHloZpc0qs90dwcANS/igKB7pyM13nquL9yMZb5mN9s+Fq6RKIUtOH+4p/SDck*CgkIARCtHCABKAESPgo8sYSfioxaRNelxt6ehsgMzqXngUjuVxdA76VLKSF4TqWCoFY0tJQHtj4Pdbx3JKRibidkbGwcl+ClLQlwGgA=#1##\",\"province\":{\"id\":\"32\",\"name\":\"江苏省\"},\"street\":{\"id\":\"320571052\",\"name\":\"斜塘街道\"},\"town\":{\"id\":\"320506\",\"name\":\"吴中区\"}},\"postAmount\":0,\"postInsuranceAmount\":0,\"postOriginAmount\":0,\"postPromotionAmount\":0,\"postReceiver\":\"\",\"postTel\":\"\",\"promotionAmount\":0,\"promotionDetail\":{\"kolDiscountDetail\":{\"couponAmount\":0,\"couponInfo\":[],\"fullDiscountAmount\":0,\"fullDiscountInfo\":[],\"redpackAmount\":0,\"redpackInfo\":[],\"totalAmount\":0},\"platformDiscountDetail\":{\"couponAmount\":0,\"couponInfo\":[],\"fullDiscountAmount\":0,\"fullDiscountInfo\":[],\"goldCoinAmount\":0,\"redpackAmount\":0,\"redpackInfo\":[],\"totalAmount\":0},\"shopDiscountDetail\":{\"couponAmount\":0,\"couponInfo\":[],\"fullDiscountAmount\":0,\"fullDiscountInfo\":[],\"totalAmount\":0}},\"promotionPayAmount\":200,\"promotionPlatformAmount\":0,\"promotionRedpackAmount\":0,\"promotionRedpackPlatformAmount\":0,\"promotionRedpackTalentAmount\":0,\"promotionShopAmount\":0,\"promotionTalentAmount\":0,\"sellerRemarkStars\":0,\"sellerWords\":\"\",\"serialNumberList\":[],\"shipTime\":0,\"shopCostAmount\":0,\"shopId\":65188572,\"shopName\":\"瑞翼通信企业店\",\"shopOrderTagUi\":[],\"skuOrderList\":[{\"accountList\":{},\"adEnvType\":\"\",\"afterSaleInfo\":{\"afterSaleStatus\":53,\"afterSaleType\":5,\"refundStatus\":3},\"appId\":1128,\"appointmentShipTime\":0,\"authorCostAmount\":0,\"authorId\":0,\"authorName\":\"\",\"bType\":2,\"bTypeDesc\":\"抖音\",\"biz\":53,\"bizDesc\":\"通信卡\",\"bundleSkuInfo\":[],\"cBiz\":8,\"cBizDesc\":\"小店自卖\",\"campaignInfo\":[],\"cancelReason\":\"\",\"cardVoucher\":{\"validDays\":0,\"validEnd\":0,\"validStart\":0},\"channelPaymentNo\":\"TP2023071017551633520680982186\",\"cid\":0,\"code\":\"\",\"confirmReceiptTime\":0,\"contentId\":\"\",\"createTime\":1688982924,\"encryptPostReceiver\":\"##hIWNgihqQMGCHyCO+pfhpd1f8c8EIQNTGCyF7OSobki5K/sMtZwDQWd43fWqivx7jmW35Iw2SgRITGic94HESLoqBtBmPNTEnGhcZ9jjHZw=*CgkIARCtHCABKAESPgo8DMCwyox8K1A/SiFTPKVJMx9Ybkl/PfKg6NT0eOqRgSYXO6rDPKxQJjebIAR0OCn4Snhb+xxwn0UZFiR8GgA=#1##\",\"encryptPostTel\":\"$$ScWdY13lfp5zhu9j2KgtZtteSBsk0sfQzcL67U6bdsJfn1QShp1tCHSJyRxBrDz9cUjJwQkvuewxGIgRjOD98mbQvUGIEx8Dl631VEGZc0Mntg==*CgkIARCtHCABKAESPgo8a/QEe6hVPvyHiC/7J/dPbmJlyJzasRfpWdLCLTiGDkCDCV70nBM1F5H1HKH93kr3IvUG4edTrCXOdVFZGgA=$1$$\",\"expShipTime\":1689155724,\"finishTime\":0,\"firstCid\":20096,\"fourthCid\":0,\"givenProductType\":\"\",\"goodsType\":0,\"hasTax\":false,\"inventoryList\":[{\"count\":1,\"inventoryType\":1,\"inventoryTypeDesc\":\"普通库存\",\"outWarehouseId\":\"\",\"warehouseId\":\"\",\"warehouseType\":0}],\"inventoryType\":\"\",\"inventoryTypeDesc\":\"\",\"isActivity\":false,\"isComment\":0,\"itemNum\":1,\"logisticsReceiptTime\":0,\"mainStatus\":21,\"mainStatusDesc\":\"发货前退款完成\",\"maskPostAddr\":{\"city\":{\"id\":\"320500\",\"name\":\"苏州市\"},\"detail\":\"崇文路*****************栋\",\"province\":{\"id\":\"32\",\"name\":\"江苏省\"},\"street\":{\"id\":\"320571052\",\"name\":\"斜塘街道\"},\"town\":{\"id\":\"320506\",\"name\":\"吴中区\"}},\"maskPostReceiver\":\"花**\",\"maskPostTel\":\"1********50\",\"masterSkuOrderId\":\"\",\"modifyAmount\":0,\"modifyPostAmount\":0,\"needSerialNumber\":false,\"onlyPlatformCostAmount\":0,\"orderAmount\":201,\"orderExpireTime\":1800,\"orderId\":\"6919983150035309994\",\"orderLevel\":3,\"orderStatus\":4,\"orderStatusDesc\":\"已关闭\",\"orderType\":0,\"orderTypeDesc\":\"普通订单\",\"originAmount\":201,\"originId\":\"0\",\"outProductId\":\"0\",\"outSkuId\":\"\",\"outWarehouseIds\":[],\"pageId\":0,\"parentOrderId\":\"6919983150035309994\",\"payAmount\":201,\"payTime\":1688982938,\"payType\":8,\"platformCostAmount\":0,\"postAddr\":{\"city\":{\"id\":\"320500\",\"name\":\"苏州市\"},\"detail\":\"\",\"encryptDetail\":\"##3MzC7ORJ0IW+esau2NfTXbODU+KlvNWq/yaY5upwfjYU+op6D59K+ySH6XV7jE3KxJlHcxVyfbhC8zkRAEhN234WG+gAZ+8BDkM+iibaOWR0vXbA/OSnzb5c9KN0Sv9RGoUsWaWNpCEUDCH4cgSvP/2dpJ+PmP/rxFq9hQHyFGLh/PH9*CgkIARCtHCABKAESPgo8jpTOsI+8WPy2ILiChgYeaU4n2jwt10bRFL0by+QWvtgp69qlEv706iX7BoZuYD6JSngKNXNwc28FKHvaGgA=#1##\",\"province\":{\"id\":\"32\",\"name\":\"江苏省\"},\"street\":{\"id\":\"320571052\",\"name\":\"斜塘街道\"},\"town\":{\"id\":\"320506\",\"name\":\"吴中区\"}},\"postAmount\":0,\"postInsuranceAmount\":0,\"postReceiver\":\"\",\"postTel\":\"\",\"preSaleType\":0,\"productId\":3611988681541466000,\"productIdStr\":\"3611988681541466314\",\"productName\":\"中国联通29元30G专属流量手机电话卡王卡 首充50得100元话费\",\"productPic\":\"https://p9-aio.ecombdimg.com/obj/ecom-shop-material/KbQMPUeS_m_cda7121ca875ef2e67a1a4c9a045feb2_sx_111123_www800-800\",\"promiseInfo\":\"{\\\"identifier\\\":\\\"S#2023-07-12\\\"}\",\"promotionAmount\":0,\"promotionDetail\":{\"kolDiscountDetail\":{\"couponAmount\":0,\"couponInfo\":[],\"fullDiscountAmount\":0,\"fullDiscountInfo\":[],\"redpackAmount\":0,\"redpackInfo\":[],\"totalAmount\":0},\"platformDiscountDetail\":{\"couponAmount\":0,\"couponInfo\":[],\"fullDiscountAmount\":0,\"fullDiscountInfo\":[],\"goldCoinAmount\":0,\"redpackAmount\":0,\"redpackInfo\":[],\"totalAmount\":0},\"shopDiscountDetail\":{\"couponAmount\":0,\"couponInfo\":[],\"fullDiscountAmount\":0,\"fullDiscountInfo\":[],\"totalAmount\":0}},\"promotionPayAmount\":200,\"promotionPlatformAmount\":0,\"promotionRedpackAmount\":0,\"promotionRedpackPlatformAmount\":0,\"promotionRedpackTalentAmount\":0,\"promotionShopAmount\":0,\"promotionTalentAmount\":0,\"receiveType\":0,\"reduceStockType\":1,\"reduceStockTypeDesc\":\"下单减库存\",\"relationOrder\":{},\"roomId\":0,\"roomIdStr\":\"0\",\"secondCid\":21574,\"sendPay\":0,\"sendPayDesc\":\"-\",\"shipTime\":0,\"shopCostAmount\":0,\"skuCustomizationInfo\":[],\"skuId\":1763666345093204,\"skuOrderTagUi\":[{\"extra\":\"\",\"helpDoc\":\"\",\"hoverText\":\"不支持7天无理由\",\"key\":\"unsupport_7days_refund\",\"sort\":0,\"tagType\":\"grey\",\"text\":\"不支持7天\"},{\"extra\":\"\",\"helpDoc\":\"\",\"hoverText\":\"达人通过「我的店铺」、或通过链接添加人店一体店铺商品（无官方店铺情况下，添加渠道号店铺商品也算）到橱窗、视频、直播，产生的订单为自卖订单\",\"key\":\"c_biz_self_sell\",\"sort\":0,\"tagType\":\"grey\",\"text\":\"小店自卖\"},{\"extra\":\"\",\"helpDoc\":\"\",\"hoverText\":\"该订单通过自然流量成交\",\"key\":\"compass_source_not_ad_mark\",\"sort\":0,\"tagType\":\"grey\",\"text\":\"非广告\"},{\"extra\":\"{\\\"compass_first_level_entrance\\\":\\\"aftersale\\\",\\\"compass_first_level_entrance_text\\\":\\\"购后页面\\\"}\",\"helpDoc\":\"\",\"hoverText\":\"该订单通过商品卡方式成交，2023/3/1起创建的订单，将于第二天更新。更多流量来源请在订单导出明细查看\",\"key\":\"compass_source_content_type_product_card\",\"sort\":0,\"tagType\":\"grey\",\"text\":\"商品卡\"}],\"sourcePlatform\":\"\",\"spec\":[{\"name\":\"默认\",\"value\":\"默认\"}],\"subBType\":3,\"subBTypeDesc\":\"H5\",\"sumAmount\":201,\"supplierId\":\"\",\"themeType\":\"0\",\"themeTypeDesc\":\"-\",\"thirdCid\":28099,\"tradeType\":0,\"tradeTypeDesc\":\"普通\",\"updateTime\":1688983482,\"videoId\":\"\",\"warehouseIds\":[],\"writeoffInfo\":[]}],\"subBType\":3,\"subBTypeDesc\":\"H5\",\"targetArrivalTime\":0,\"totalPromotionAmount\":0,\"tradeType\":0,\"tradeTypeDesc\":\"普通\",\"updateTime\":1688983482,\"userCoordinate\":{\"userCoordinateLatitude\":\"31.2648868560791\",\"userCoordinateLongitude\":\"120.73419952392578\"},\"userIdInfo\":{\"encryptIdCardName\":\"##7nhgsUqqVUNwcyrmNvD1/JzxjEvJ25kH7Dfz+SqZTFEAOp660kRlKIFotNZWm49rPE9BqdzfZJdAdHC9VYtsr/w109d98xwbJpEhTaH00NU=*CgkIARCtHCABKAESPgo8uan3RtIWX/hsDAO7Qoh5BEYIQ/0yv5RVnxUL/iCn3rAW4aqxKecdQIuFMNDyv42GM0rG5Cm2xBdpSwGlGgA=#1##\",\"encryptIdCardNo\":\"~~M93j+gvI86EZWQnWks4YSiRgTHLAqjqNX+AbeJ1166/MD9RbCcEM0bU6QYDtXOI2k5cEhXANJG192/jVThBv7unnXEgHFlOe9zbo0CZh2vtHvBN9s4a8uKM=*CgkIARCtHCABKAESPgo85qdhfzcOoNu1zx3EUmT569Sssyfx9lhfbhojbUq9yt/9YazyEWtpx1cUerI2wqSciHrPztg9zoOG+fnmGgA=~1~~\",\"idCardName\":\"\",\"idCardNo\":\"\"},\"userTagUi\":[{\"key\":\"user_profile_shop_customer_type\",\"text\":\"店铺老客\"}]}},\"logId\":\"2023080313132442C986339FAB8216CC83\",\"msg\":\"success\",\"subCode\":\"\",\"subMsg\":\"\",\"success\":true}";
        OrderOrderDetailResponse orderOrderDetailResponse = JSON.parseObject(res, OrderOrderDetailResponse.class);
        ShopOrderDetail item = orderOrderDetailResponse.getData().getShopOrderDetail();
        OrderBatchDecryptRequest request2 = new OrderBatchDecryptRequest();
        OrderBatchDecryptParam param2 = request2.getParam();
        List<CipherInfosItem> list = new ArrayList<>();
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
        param2.setCipherInfos(list);
        request2.setParam(param2);
        OrderBatchDecryptResponse response2 = request2.execute(accessToken);
        System.out.println(JSON.toJSONString(response2));

    }
}
