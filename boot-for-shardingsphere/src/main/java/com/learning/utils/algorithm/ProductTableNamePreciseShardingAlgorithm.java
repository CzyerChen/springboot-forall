/**
 * Author:   claire
 * Date:    2021-01-07 - 15:45
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2021-01-07 - 15:45          V1.14.0
 */
package com.learning.utils.algorithm;

import com.alibaba.fastjson.JSON;
import io.shardingsphere.api.algorithm.sharding.PreciseShardingValue;
import io.shardingsphere.api.algorithm.sharding.standard.PreciseShardingAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * 功能简述 <br/> 
 * 〈〉
 *
 * @author claire
 * @date 2021-01-07 - 15:45
 */
@Slf4j
@Component
public class ProductTableNamePreciseShardingAlgorithm implements PreciseShardingAlgorithm<String> {

    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<String> shardingValue) {
        log.info("collection:" + JSON.toJSONString(availableTargetNames) + ",preciseShardingValue:" + JSON.toJSONString(shardingValue));

        // 根据配置的分表规则生成目标表的后缀
        String tableExt = shardingValue.getValue().substring(26);
        for (String availableTableName : availableTargetNames) {
            if (availableTableName.endsWith(tableExt)) {
                // 匹配成功返回正确表名
                System.out.println(availableTableName);
                return availableTableName;
            }
        }
        return null;
    }

}
