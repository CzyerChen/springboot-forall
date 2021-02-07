/**
 * Author:   claire
 * Date:    2021-01-07 - 11:29
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2021-01-07 - 11:29          V1.14.0
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
 * @date 2021-01-07 - 11:29
 */
public class GenderShardingAlgorithm implements PreciseShardingAlgorithm<Integer>, RangeShardingAlgorithm<Integer> {
    /**
     * Sharding.
     *
     * @param availableTargetNames available data sources or tables's names
     * @param shardingValue        sharding value
     * @return sharding result for data source or table's name
     */
    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Integer> shardingValue) {
        String databaseName = availableTargetNames.stream().findFirst().get();

        for (String dbName : availableTargetNames) {
            if (dbName.endsWith(genderToTableSuffix(shardingValue.getValue()))) {
                databaseName = dbName;
            }
        }

        return databaseName;
    }

    /**
     * Sharding.
     *
     * @param availableTargetNames available data sources or tables's names
     * @param shardingValue        sharding value
     * @return sharding results for data sources or tables's names
     */
    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, RangeShardingValue<Integer> shardingValue) {
        Collection<String> dbs = new LinkedHashSet<>(availableTargetNames.size());

        Range<Integer> range = (Range<Integer>) shardingValue.getValueRange();
        for (Integer i = range.lowerEndpoint(); i <= range.upperEndpoint(); i++) {
            for (String dbName : availableTargetNames) {
                if (dbName.endsWith(genderToTableSuffix(i))) {
                    dbs.add(dbName);
                }
            }
        }
        return dbs;
    }

    /**
     * 字段与分库的映射关系
     *
     * @param gender
     * @return
     */
    private String genderToTableSuffix(Integer gender) {
        return gender==0 ? "0" : "1";
    }
}
