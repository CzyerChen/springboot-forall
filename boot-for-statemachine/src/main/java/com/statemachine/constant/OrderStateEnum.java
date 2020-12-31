/**
 * Author:   claire
 * Date:    2020-12-28 - 16:10
 * Description: 任务阶段枚举类
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2020-12-28 - 16:10          V1.13.0          任务阶段枚举类
 */
package com.statemachine.constant;

/**
 * 功能简述 <br/> 
 * 〈任务阶段枚举类〉
 *
 * @author claire
 * @date 2020-12-28 - 16:10
 * @since 1.13.0
 */
public enum OrderStateEnum {
    /**
     * 待支付
     */
    UNPAID,
    /**
     * 待收货
     */
    WAITING_FOR_RECEIVE,
    /**
     * 结束
     */
    DONE
}
