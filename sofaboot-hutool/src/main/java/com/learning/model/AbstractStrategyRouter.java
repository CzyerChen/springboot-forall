/**
 * Author:   claire
 * Date:    2021-02-20 - 18:07
 * Description: 抽象策略路由
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2021-02-20 - 18:07          V1.17.0          抽象策略路由
 */
package com.learning.model;



import lombok.Getter;
import lombok.Setter;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Objects;

/**
 * 功能简述 
 *  抽象策略路由
 *  通过树形结构实现分发和委托
 *  {@code AbstractStrategyRouter} 实现对策略的分发
 *  {@code Handler} 实现处理器方法
 *
 * @author claire
 * @date 2021-02-20 - 18:07
 */
public abstract class AbstractStrategyRouter<T,R> {
    private IStrategyMapper<T,R> strategyMapper;

    //配置一个默认的处理器
    @Getter
    @Setter
    @SuppressWarnings("unchecked")
    private IStrategyHandler<T,R> defaultStrategyHandler = IStrategyHandler.DEFAULT;

    //注册处理器映射，这是分发最终处理器的关键
    protected  abstract IStrategyMapper<T,R> registerStrategyMapper();

    //初始化，注册处理器映射
    @PostConstruct
    private void abstractInit(){
        strategyMapper = registerStrategyMapper();
        Objects.requireNonNull(strategyMapper,"strategyMapper need non null");
    }

    //分发策略
    public R applyStrategy(T param){
       final IStrategyHandler<T,R> strategyHandler = strategyMapper.get(param);
       if(strategyHandler != null){
           return strategyHandler.apply(param);
        }
       return defaultStrategyHandler.apply(param);
    }

}

