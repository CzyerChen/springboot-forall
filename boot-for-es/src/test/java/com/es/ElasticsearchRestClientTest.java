/**
 * Author:   claire
 * Date:    2020-12-30 - 16:07
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2020-12-30 - 16:07          V1.13.0
 */
package com.es;

import com.es.entity.CountModelData;
import com.es.repository.CountModelDataRepository;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.client.ElasticsearchClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
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
}
