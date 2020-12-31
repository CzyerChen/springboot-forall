/**
 * Author:   claire
 * Date:    2020-12-28 - 21:14
 * Description:
 * History:
 * <author>          <time>                    <version>          <desc>
 * claire          2020-12-28 - 21:14          V1.13.0
 */
package com.statemachine.demo.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Optional;


/**
 * 功能简述 <br/> 
 * 〈〉
 *
 * @author claire
 * @date 2020-12-28 - 21:14
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum BizOrderStatusEnum {

    CREATE("1", "创建"),

    WYD_INITIAL_JUMP("2", "初始创建时可能为待实名校验，也可能为待发起借款，需要用guard判断"),

    WAIT_REAL_NAME_AUTH("3", "待实名认证"),

    WAIT_BORROW("4", "待发起借款"),

    CANCEL("999", "用户取消"),

    CLOSE("996", "订单关闭"),

    SUCCESS("888", "成功，指已销账，订单完全终结"),

    /**
     * 审核相关状态定义-start
     */
    AUDITING("100", "审核中"),

    WAIT_BIZ_AUDIT("101", "待业务审核"),

    BIZ_APPROVED("102", "业务审核通过"),

    WAIT_COMPLEMENT("103", "待补全审核资料"),

    CHECK_COMPLEMENT("104", "补全资料检查"),// --流程内部使用

    WAIT_UPLOAD_IMG("105", "待上传影像资料"),

    CHECK_UPLOAD("106", "上传影像检查"),//  --- 流程内部使用

    WAIT_BEF_DEAL_RISK_AUDIT("111", "待贷前额度评估"),

    IN_DEAL_RISK_AUDITING("121", "贷中风控审核"),

    WAIT_AF_DEAL_RISK_AUDIT("131", "待贷后审核"),

    AF_DEAL_RISK_APPROVED("132", "贷后审核通过"),

    APPROVED("198", "审核通过"),

    /**
     * 审核相关状态定义--end
     */
    WAIT_SIGN("200", "待签约"),

    SIGNED("288", "签约完成"),

    LOANING("300", "放款中"),

    LOANED("388", "放款完成"),

    REFUNDING("401", "退款中"), // 对于消费贷，存在退款情况

    REFUNDED("488", "退款完成"), // 对于消费贷，存在退款情况

    BILL_GEN("501", "生成账单"),

    REPAYING("600", "还款中"),  // 内部使用的中间瞬时状态，外部传入时不要使用，部分还款采用PART_REPAID类型

    PART_REPAID("601", "部分还款"),

    REPAID("688", "已还清"),

    OVERDUE("700", "已逾期"),;


    private String status;

    private String desc;

    /**
     * status是否合法
     *
     * @param status
     * @return
     */
    public static boolean isIn(String status) {
        return Arrays.asList(BizOrderStatusEnum.values()).parallelStream().
                anyMatch(value -> StringUtils.equals(value.getStatus(), status));

    }

    /**
     * 判断status是否相等
     *
     * @param status
     * @param statusEnum
     * @return
     */
    public static boolean equals(String status, BizOrderStatusEnum statusEnum) {
        return StringUtils.equalsIgnoreCase(status, statusEnum.getStatus());

    }

    /**
     * status-->statusEnum
     *
     * @param status
     * @return
     */
    public static BizOrderStatusEnum getByStatus(String status) {
        Optional<BizOrderStatusEnum> statusEnumOptional = Arrays.asList(BizOrderStatusEnum.values()).parallelStream()
                .filter(statusEnum -> StringUtils.equalsIgnoreCase(status, statusEnum.getStatus())).findAny();

        if (statusEnumOptional.isPresent()) {
            return statusEnumOptional.get();
        }

        return null;

    }

    /**
     * 判断status是否合法
     *
     * @param status
     * @param statusEnums
     * @return
     */
    public static boolean isIn(String status, BizOrderStatusEnum... statusEnums) {
        return Arrays.asList(statusEnums).parallelStream().
                anyMatch(value -> StringUtils.equals(value.getStatus(), status));

    }

    /**
     * 判断是否订单已终结，取消、关闭、成功、拒绝都属于终结状态
     *
     * @param status
     * @return
     */
    public static boolean isFinish(String status) {
        return isIn(status, SUCCESS, CANCEL, CLOSE);
    }

    /**
     * 判断订单是否是初始创建状态
     * 对于： WAIT_REAL_NAME_AUTH, WAIT_BORROW 都可能是初始状态
     * 对于其他：暂时为CREATE状态
     *
     * @param status
     * @return
     */
    public static boolean isInitialStatus(String status) {
        return isIn(status, CREATE, WAIT_REAL_NAME_AUTH, WAIT_BORROW);
    }

}