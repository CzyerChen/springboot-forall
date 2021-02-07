/**
 * Author:   claire
 * Date:    2021-01-07 - 11:30
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2021-01-07 - 11:30          V1.14.0
 */
package com.learning.utils.algorithm;

import com.google.common.collect.Range;
import io.shardingsphere.api.algorithm.sharding.PreciseShardingValue;
import io.shardingsphere.api.algorithm.sharding.RangeShardingValue;
import io.shardingsphere.api.algorithm.sharding.standard.PreciseShardingAlgorithm;
import io.shardingsphere.api.algorithm.sharding.standard.RangeShardingAlgorithm;

import java.util.Collection;
import java.util.LinkedHashSet;

/**
 * 功能简述 <br/> 
 * 〈〉
 *
 * @author claire
 * @date 2021-01-07 - 11:30
 */
public class IdShardingAlgorithm implements PreciseShardingAlgorithm<Long>, RangeShardingAlgorithm<Long> {
    /**
     * Sharding.
     *
     * @param availableTargetNames available data sources or tables's names
     * @param shardingValue        sharding value
     * @return sharding result for data source or table's name
     */
    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Long> shardingValue) {
        String table = availableTargetNames.stream().findFirst().get();

        for (String tableName : availableTargetNames) {
            if (tableName.endsWith(idToTableSuffix(shardingValue.getValue()))) {
                table = tableName;
            }
        }

        return table;
    }

    /**
     * Sharding.
     *
     * @param availableTargetNames available data sources or tables's names
     * @param shardingValue        sharding value
     * @return sharding results for data sources or tables's names
     */
    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, RangeShardingValue<Long> shardingValue) {
        Collection<String> dbs = new LinkedHashSet<>(availableTargetNames.size());

        Range<Long> range = (Range<Long>) shardingValue.getValueRange();
        for (long i = range.lowerEndpoint(); i <= range.upperEndpoint(); i++) {
            for (String dbName : availableTargetNames) {
                if (dbName.endsWith(idToTableSuffix(i))) {
                    dbs.add(dbName);
                }
            }
        }

        return dbs;
    }

    /**
     * 字段与分表的映射关系
     *
     * @param id
     * @return 表后缀（201906、201907等）
     */
    private String idToTableSuffix(Long id) {
        return String.valueOf(id % 2);
    }
}
