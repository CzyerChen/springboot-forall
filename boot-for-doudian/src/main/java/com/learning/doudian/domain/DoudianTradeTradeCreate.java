/**
 * Author:   claire
 * Date:    2022/10/24 - 12:59 下午
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2022/10/24 - 12:59 下午          V1.0.0
 */
package com.learning.doudian.domain;

import lombok.Data;

import java.util.List;

/**
 * 功能简述 
 * 〈〉
 *
 * @author claire
 * @date 2022/10/24 - 12:59 下午
 * @since 1.0.0
 */
@Data
public class DoudianTradeTradeCreate {
    /**
     * 父订单ID
     */
    private Long pId;

    /**
     * 子订单ID列表
     */
    private List<Long> sIds;

    /**
     * 店铺ID
     */
    private Long shopId;

    /**
     * 订单创建时间
     */
    private Long createTime;

    /**
     * 父订单状态，订单创建消息的order_status值为"1"
     */
    private Integer orderStatus;

    /**
     * 订单类型
     */
    private Integer orderType;

    /**
     * 订单业务类型
     */
    private Integer biz;
}
