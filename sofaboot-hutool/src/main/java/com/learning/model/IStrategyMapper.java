/**
 * Author:   claire
 * Date:    2021-02-22 - 10:14
 * Description: 策略映射
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2021-02-22 - 10:14          V1.17.0          策略映射
 */
package com.learning.model;

/**
 * 功能简述 
 * 〈策略映射〉
 *
 * @author claire
 * @date 2021-02-22 - 10:14
 * @since 1.17.0
 */
public interface IStrategyMapper<T,R> {
    //获取处理器
    IStrategyHandler<T,R> get(T param);
}
