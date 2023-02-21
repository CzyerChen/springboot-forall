/**
 * Author:   claire
 * Date:    2022/8/8 - 11:31 上午
 * Description: 发送记录
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2022/8/8 - 11:31 上午          V1.0.0          发送记录
 */
package com.es.repository;

import com.es.entity.SmsSendHistData;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * 功能简述 
 * 〈发送记录〉
 *
 * @author claire
 * @date 2022/8/8 - 11:31 上午
 * @since 1.0.0
 */
@Repository
public interface SmsSendHistDataRepository extends ElasticsearchRepository<SmsSendHistData,String> {
}
