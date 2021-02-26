/**
 * Author:   claire
 * Date:    2021-02-22 - 10:16
 * Description: 策略映射实现
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2021-02-22 - 10:16          V1.17.0          策略映射实现了哦
 */
package com.learning.model;

/**
 * 功能简述 
 * 〈策略映射实现〉
 *
 * @author claire
 * @date 2021-02-22 - 10:16
 * @since 1.17.0
 */
public class StrategyMapperImpl implements IStrategyMapper<Integer,Boolean>{

    @Override
    public IStrategyHandler<Integer, Boolean> get(Integer param) {
        if("=".equals(param)){
            return new BetweenOperHandler();
        }else if(">=".equals(param)){
            return new MoreThanOperHandler();
        }else if("<=".equals(param)){
            return new LessThanOperHandler();
        }
        return null;
    }
}
