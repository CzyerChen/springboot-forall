/**
 * Author:   claire
 * Date:    2021-02-20 - 18:28
 * Description: 小于算法处理器
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2021-02-20 - 18:28          V1.17.0          小于算法处理器
 */
package com.learning.model;

/**
 * 功能简述 
 * 〈小于算法处理器〉
 *
 * @author claire
 * @date 2021-02-20 - 18:28
 * @since 1.17.0
 */
public class LessThanOperHandler implements IStrategyHandler<Integer,Boolean> {
    @Override
    public Boolean apply(Integer param) {
        return param<0;
    }
}
