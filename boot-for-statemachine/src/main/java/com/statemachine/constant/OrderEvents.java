/**
 * Author:   claire
 * Date:    2020-12-28 - 17:31
 * Description: 订单状态
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2020-12-28 - 17:31          V1.13.0          订单状态
 */
package com.statemachine.constant;

/**
 * 功能简述 <br/> 
 * 〈订单状态〉
 *
 * @author claire
 * @date 2020-12-28 - 17:31
 */
public enum OrderEvents {
    /**
     * 支付
     */
    PAY,
    /**
     * 收货
     */
    RECEIVE
}
