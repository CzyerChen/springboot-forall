/**
 * Author:   claire
 * Date:    2021-02-20 - 18:26
 * Description: 大于算法处理器
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2021-02-20 - 18:26          V1.17.0          大于算法处理器
 */
package com.learning.model;

/**
 * 功能简述 
 * 〈大于算法处理器〉
 *
 * @author claire
 * @date 2021-02-20 - 18:26
 * @since 1.17.0
 */
public class MoreThanOperHandler implements IStrategyHandler<Integer,Boolean> {
    @Override
    public Boolean apply(Integer param) {
        return param>100;
    }
}
