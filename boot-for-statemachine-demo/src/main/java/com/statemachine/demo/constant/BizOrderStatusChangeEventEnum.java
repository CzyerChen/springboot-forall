/**
 * Author:   claire
 * Date:    2020-12-28 - 21:15
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2020-12-28 - 21:15          V1.13.0
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
 * @date 2020-12-28 - 21:15
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum BizOrderStatusChangeEventEnum {

    EVT_CREATE("evt_create"),

    EVT_CANCEL("evt_cancel"), // 取消

    EVT_NAME_AUTH("evt_name_auth"), // 实名

    EVT_SYS_CLOSE("evt_sys_close"), // 系统关单，如超时或风控关单等

    EVT_AUDIT("evt_audit"), // 审核

    EVT_COMPLEMENT("evt_complement"), // 补全材料

    EVT_UPLOAD_IMG("evt_upload_img"), // 上传影像

    EVT_APPROVED("evt_approved"), // 批准

    EVT_REFUSE("evt_refuse"), // 拒绝

    EVT_SIGN("evt_sign"), // 签约

    EVT_LOAN("evt_loan"), // 放款

    EVT_LOAN_FAILED("evt_loan_failed"),

    EVT_REFUND("evt_refund"), // 退款

    EVT_REPAY("evt_repay"), // 还款

    EVT_GEN_BILL("evt_gen_bill"), // 生成账单

    EVT_TOSUCCESS("evt_tosuccess"), // 销账

    EVT_RETRY("evt_retry"), // 重试

    EVT_OVERDUE("evt_overdue")  // 逾期，用户无动作，由系统定时任务发起
    ,
    EVT_LOAN_SUCC("evt_loan_succ"),

    EVT_NEED_NAME_AUTH("evt_need_name_auth");

    String event;

    /**
     * 判断
     * @param eventName
     * @return
     */
    public static BizOrderStatusChangeEventEnum getEvent(String eventName) {
        if (StringUtils.isBlank(eventName)) {
            return null;
        }

        Optional<BizOrderStatusChangeEventEnum> resultOptional = Arrays.asList(BizOrderStatusChangeEventEnum.values()).parallelStream().filter(eventEnum ->
                StringUtils.equals(eventName, eventEnum.getEvent())).findAny();

        if (resultOptional.isPresent()) {
            return resultOptional.get();
        }
        return null;
    }
}