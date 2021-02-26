/**
 * Author:   claire
 * Date:    2021-02-20 - 18:29
 * Description: 路由实现类
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2021-02-20 - 18:29          V1.17.0          路由实现类
 */
package com.learning.model;

/**
 * 功能简述 
 * 〈路由实现类〉
 *
 * @author claire
 * @date 2021-02-20 - 18:29
 * @since 1.17.0
 */
public class StrategyRouterImpl extends AbstractStrategyRouter<OperEnum,IStrategyHandler> {
    @Override
    protected IStrategyMapper<OperEnum,IStrategyHandler> registerStrategyMapper() {

        return null;
    }
}
