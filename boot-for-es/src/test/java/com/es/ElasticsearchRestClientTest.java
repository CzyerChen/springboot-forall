/**
 * Author:   claire
 * Date:    2020-12-30 - 16:07
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2020-12-30 - 16:07          V1.13.0
 */
package com.es;

import com.alibaba.fastjson.JSON;
import com.es.entity.CountModelData;
import com.es.entity.SendCallBackPool;
import com.es.entity.SmsSendHistData;
import com.es.repository.CountModelDataRepository;
import com.es.repository.SmsSendHistDataRepository;
import com.es.util.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.client.ElasticsearchClient;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.rest.RestStatus;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.*;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestOperations;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.*;


/**
 * 功能简述 <br/> 
 * 〈〉
 *
 * @author claire
 * @date 2020-12-30 - 16:07
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("dev")
@SpringBootTest(classes = ElasticseachTestApplication.class)
public class ElasticsearchRestClientTest {
    @Autowired
    private CountModelDataRepository countModelDataRepository;
    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;
    @Autowired
    private SmsSendHistDataRepository smsSendHistDataRepository;
    @Autowired
    private RestHighLevelClient client;

    @Test
    public void testSaveEntity(){
        CountModelData countModelData = new CountModelData();
        countModelData.setTaskId("xx");
        countModelData.setHost("ab1");
        countModelData.setLabel("bc1");
        countModelData.setPosCountNum(0L);
        CountModelData save = countModelDataRepository.save(countModelData);
        Assert.assertNotNull(save);
    }

    @Test
    public void testSaveEntities(){
        for(int j =0 ; j<40 ;j++) {
            List<CountModelData> list =new ArrayList<>();

            for (int i = 0; i < 1000; i++) {
                int num = i*j+i;
                CountModelData countModelData = new CountModelData();
                countModelData.setHost("aa"+num);
                countModelData.setLabel("bb"+num);
                countModelData.setPosCountNum((long)num);
                countModelData.setNegCountNum((long)num*2);
                countModelData.setTotalCountNum((long)num);
                countModelData.setHosts("cc"+num);
                countModelData.setHostsLength(num);
                countModelData.setTgi((long)num-100);
                countModelData.setPosFreq(i);
                countModelData.setNegFreq(i);
                countModelData.setTaskId("xx");

                list.add(countModelData);
            }
            countModelDataRepository.saveAll(list);
        }
    }

    @Test
    public  void testListData(){
        long count = countModelDataRepository.count();
        Assert.assertNotNull(count);
    }

    @Test
    public void testQueryWithMethod(){
        long start = System.currentTimeMillis();
        List<CountModelData> taskList = new ArrayList<>();
        for(int i =0 ;i<4 ;i++) {
            Pageable pageable = PageRequest.of(i, 50);
            Page<CountModelData> tasks = countModelDataRepository.findByTaskId("xx", pageable);
            taskList.addAll(tasks.getContent());
        }
        System.out.println(System.currentTimeMillis()-start);
        Assert.assertNotNull(taskList);
    }

    @Test
    public void testQueryWithPageable(){
        BoolQueryBuilder queryBuilder = new BoolQueryBuilder();
        queryBuilder.must(termQuery("taskId", "xx"));
        queryBuilder.must(rangeQuery("posCountNum").gte(10));
        queryBuilder.must(rangeQuery("negCountNum").lte(100));
        queryBuilder.must(rangeQuery("totalCountNum").gte(10));
//        queryBuilder.must(termQuery("host","aa50"));
        queryBuilder.must(wildcardQuery("host","aa5*"));
        NativeSearchQuery query = new NativeSearchQuery(queryBuilder);
        query.setPageable(PageRequest.of(0, 500,Sort.by(Sort.Direction.DESC,"tgi")));

        SearchHits<CountModelData> searchHits = elasticsearchRestTemplate.search(query, CountModelData.class, IndexCoordinates.of("dmp_count_model"));
        List<CountModelData> resultList = searchHits.getSearchHits().stream().map(SearchHit::getContent).collect(Collectors.toList());
        Assert.assertNotNull(resultList);
    }


    @Test
    public void testQueryWithScroll() {
        List<CountModelData> dataList = new ArrayList<>();
        String scrollId = null;
        for(int i=0 ;i<= 6;i++ ){
            long start = System.currentTimeMillis();
            // 滚动查询 scroll api
            BoolQueryBuilder queryBuilder = new BoolQueryBuilder();
            queryBuilder.must(termQuery("taskId", "xx"));
            queryBuilder.must(rangeQuery("posCountNum").gte(10));
            queryBuilder.must(rangeQuery("negCountNum").lte(100));
            queryBuilder.must(rangeQuery("totalCountNum").gte(50));
            queryBuilder.must(wildcardQuery("host","a*"));
            NativeSearchQuery query = new NativeSearchQuery(queryBuilder);
            query.setPageable(PageRequest.of(i, 10000, Sort.by(Sort.Direction.DESC,"tgi")));
            SearchHits<CountModelData> searchHits = null;
            if (StringUtils.isEmpty(scrollId)) {
                // 开启一个滚动查询，设置该 scroll 上下文存在 60s
                searchHits = elasticsearchRestTemplate.searchScrollStart(6000, query, CountModelData.class, IndexCoordinates.of("dmp_count_model"));
                if (searchHits instanceof SearchHitsImpl) {
                    scrollId = ((SearchHitsImpl) searchHits).getScrollId();
                }
            } else {
                // 继续滚动
                searchHits = elasticsearchRestTemplate.searchScrollContinue(scrollId, 6000, CountModelData.class, IndexCoordinates.of("dmp_count_model"));
            }

            List<CountModelData> articles = searchHits.getSearchHits().stream().map(SearchHit::getContent).collect(Collectors.toList());
            if (articles.size() == 0) {
                // 结束滚动
                elasticsearchRestTemplate.searchScrollClear(Collections.singletonList(scrollId));
                break;
            }else{
                dataList.addAll(articles);
            }
            System.out.println("耗时："+ (System.currentTimeMillis()-start));
        }
        int resultCount = dataList.size();
        List<String> collect = dataList.stream().map(CountModelData::getTaskId).distinct().collect(Collectors.toList());
        Assert.assertNotNull(dataList);
    }

    @Test
    public void testDelete(){
        countModelDataRepository.deleteAll();
    }

    //这个由于低版本会有些参数不支持，所以不行
    @Test
    public void testCountHistory(){
        long count = smsSendHistDataRepository.count();
        Assert.assertNotEquals(count,0);
    }

    @Test
    public void testCountHistoryWithClient() throws IOException {
        CountResponse count = client.count(new CountRequest("sms_send_history_202208"), RequestOptions.DEFAULT);
        long count1 = count.getCount();
        Assert.assertNotEquals(count1,0);
    }

    public static SmsSendHistData convertKafkaSendhistory(SendCallBackPool send) {
        SmsSendHistData sendHistData = new SmsSendHistData();

        String msg = send.getMsgbody();
        if (send.getChannelid() == 22L) {
            if (org.apache.commons.lang.StringUtils.isNotBlank(msg)) {
                String[] msgArray = msg.split("\\|");
                if (null != msgArray && msgArray.length > 0) {
                    sendHistData.setSendsmsbody(msgArray[0]);
                    sendHistData.setMsgbody(msgArray[0]);
                }
            }
        } else {
            sendHistData.setSendsmsbody(send.getSendsmsbody());
            sendHistData.setMsgbody(msg);
        }
        sendHistData.setUserid(send.getUserid());
        sendHistData.setVendor(send.getVendor());
        sendHistData.setCity(send.getCity());
        sendHistData.setMobile(send.getMobile());
        sendHistData.setMsgcount(send.getMsgcount());
        sendHistData.setSender(send.getSender());
        sendHistData.setAccepttype(send.getAccepttype());
        sendHistData.setSendcode(send.getSendcode());
        sendHistData.setMsgid(send.getMsgid());
        sendHistData.setBmsgid(send.getBmsgid());
        sendHistData.setUsermsgid(send.getUsermsgid());
        sendHistData.setUserbulkmsgid(send.getUserbulkmsgid());
        sendHistData.setChannelmsgid(send.getChannelmsgid());
        sendHistData.setChannelid(send.getChannelid());
        sendHistData.setFileid(send.getFileid());
        sendHistData.setNotifyurl(send.getNotifyurl());
        sendHistData.setSmscode(send.getSmscode());
        sendHistData.setSmsstat(send.getSmsstat());
        sendHistData.setParentmsgid(send.getParentmsgid());
        sendHistData.setErrormessage(send.getErrormessage());
        sendHistData.setStartdeliveryid(send.getStartdeliveryid());
        sendHistData.setAcctime(send.getAcctime());
        sendHistData.setSendtime(send.getSendtime());
        sendHistData.setSmstype(send.getSmstype());

        sendHistData.setProvinceId(send.getProvinceId());
        sendHistData.setCityId(send.getCityId());
        return sendHistData;
    }

     @Test
   public void testAddHistoryWithClient() throws IOException {
       String id = UUID.randomUUID().toString().replaceAll("-", "");
       SendCallBackPool data = new SendCallBackPool();
       data.setMsgbody("友情提示：11111112222222222");
       data.setChannelid(3L);
       data.setUserid(2L);
       data.setVendor(1);
       data.setCity("苏州");
       data.setMobile("13777777777");
       data.setMsgcount(1);
       data.setSender("3");
       data.setAccepttype(1);
       data.setSendcode("001");
       data.setMsgid("1245972763423");
       data.setUsermsgid("1245972763423");
       data.setUserbulkmsgid("1245972763423");
       data.setBmsgid("1245972763423");
       data.setChannelmsgid("1245972763423");
       data.setFileid("1245972763423");
       data.setNotifyurl("http://127.0.0.1:8080");
       data.setSmscode("000");
       data.setSmsstat("DELIVED");
       data.setErrormessage("");
       data.setAcctimed(Date.from(LocalDate.now().minusDays(7).atStartOfDay().toInstant(ZoneOffset.of("+8"))));
       data.setAcctime(LocalDate.now().minusDays(7).atStartOfDay().toInstant(ZoneOffset.of("+8")).toEpochMilli());
       data.setSendtime(LocalDate.now().minusDays(7).atStartOfDay().plusHours(7).toInstant(ZoneOffset.of("+8")).toEpochMilli());
       data.setCityId(100010);
       SmsSendHistData sendHistData = convertKafkaSendhistory(data);

       //将上面的参数合在一起就变成下面的式子
       IndexRequest request = Requests.indexRequest("sms_send_history_202208").id(id).type("sms_send_history")
               .source(JSON.toJSONString(sendHistData), XContentType.JSON)
               .setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);

       IndexResponse index = client.index(request, RequestOptions.DEFAULT);
       DocWriteResponse.Result result = index.getResult();
       if(DocWriteResponse.Result.CREATED.equals(index.getResult())){
           System.out.println("创建索引 插入文档完毕！！");
       }
   }

   @Test
    public void testBulkAddHistories() throws IOException {
       final BulkRequest request = new BulkRequest();
        for(int i=18;i<21;i++) {
            String id = UUID.randomUUID().toString().replaceAll("-", "");
            SendCallBackPool data = new SendCallBackPool();
            data.setMsgbody("友情提示：11111112222222222"+i);
            data.setChannelid(3L);
            data.setUserid(2L);
            data.setVendor(1);
            data.setCity("青岛");
            data.setMobile("1377777777"+i);
            data.setMsgcount(1);
            data.setSender("3");
            data.setAccepttype(1);
            data.setSendcode("001");
            data.setMsgid("124597276342"+i);
            data.setUsermsgid("124597276342"+i);
            data.setUserbulkmsgid("124597276342"+i);
            data.setBmsgid("124597276342"+i);
            data.setChannelmsgid("124597276342"+i);
            data.setFileid("124597276342"+i);
            data.setNotifyurl("http://127.0.0.1:8080");
            data.setSmscode("000");
            data.setSmsstat("UNDELIV");
            data.setErrormessage("");
//            data.setAcctimed(Date.from(LocalDate.now().minusDays(2).atStartOfDay().toInstant(ZoneOffset.of("+8"))));
            data.setAcctime(LocalDate.now().minusDays(2).atStartOfDay().toInstant(ZoneOffset.of("+8")).toEpochMilli());
            data.setSendtime(LocalDate.now().minusDays(2).atStartOfDay().plusHours(7).toInstant(ZoneOffset.of("+8")).toEpochMilli());
            data.setCityId(100010);
            SmsSendHistData sendHistData = convertKafkaSendhistory(data);
            IndexRequest inrequest = Requests.indexRequest("sms_send_history_202208").id(id).type("sms_send_history")
                    .source(JSON.toJSONString(sendHistData), XContentType.JSON);
            request.add(inrequest);
        }
       request.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
       /** 执行批量插入并获取返回结果 **/
       final BulkResponse response = client.bulk(request, RequestOptions.DEFAULT);
       //判断执行结果是否有执行失败的条目
       if (response.hasFailures()) {
           for (final BulkItemResponse itemResponse : response) {
               //表示这条执行结果失败
               if (itemResponse.isFailed()) {
                   //执行相应的业务
                   System.out.println("FAILED");
               }
           }
       }
   }

}
