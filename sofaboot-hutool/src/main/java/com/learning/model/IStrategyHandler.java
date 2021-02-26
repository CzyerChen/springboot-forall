/**
 * Author:   claire
 * Date:    2021-02-20 - 18:12
 * Description: 策略处理器
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2021-02-20 - 18:12          V1.17.0          策略处理器
 */
package com.learning.model;

/**
 * 功能简述 
 * 〈策略处理器〉
 *
 * @author claire
 * @date 2021-02-20 - 18:12
 * @since 1.17.0
 */
public interface IStrategyHandler<T,R> {

    @SuppressWarnings("rawtypes")
    IStrategyHandler DEFAULT = t-> null;

    R apply(T param);
}
