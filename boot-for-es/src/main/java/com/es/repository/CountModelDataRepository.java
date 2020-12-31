/**
 * Author:   claire
 * Date:    2020-12-30 - 15:52
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2020-12-30 - 15:52          V1.13.0
 */
package com.es.repository;

import com.es.entity.CountModelData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 功能简述 <br/> 
 * 〈〉
 *
 * @author claire
 * @date 2020-12-30 - 15:52
 */
@Repository
public interface CountModelDataRepository extends ElasticsearchRepository<CountModelData,String> {
    Page<CountModelData> findByTaskId(String taskId, Pageable pageable);
}

