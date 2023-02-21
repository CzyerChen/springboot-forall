/**
 * Author:   claire
 * Date:    2022/8/8 - 10:17 上午
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2022/8/8 - 10:17 上午          V1.0.0
 */
package com.es.entity;

import com.es.enums.*;
import com.es.util.JsonUtil;

/**
 * 功能简述 
 * 〈〉
 *
 * @author claire
 * @date 2022/8/8 - 10:17 上午
 * @since 1.0.0
 */
public class SmsSendData  implements IMsgData {
    private String userId;
    private String uid;
    private String mobile;
    private Integer operators;
    private Long provinceId;
    private String provinceCode;
    private Long cityId;
    private String cityCode;
    private Long consumerId;
    private Integer msgCount;
    private String reqMsgBody;
    private String sendMsgBody;
    private Integer acceptType;
    private String sendCode;
    private String msgId;
    private String bulkMsgId;
    private String userMsgId;
    private String userBulkMsgId;
    private String channelMsgId;
    private Long channelId;
    private String fileId;
    private String smsCode;
    private String smsStatus;
    private String errorMsg;
    private Long accTime;
    private Long sendTime;
    private Long notifyTime;
    private Integer smsType;
    private Integer source;
    private Long id;
    private Long pid;
    private Integer msg_success_count;
    private Integer msg_fail_count;
    private String sequence_id;
    private String bizCode;
    private String bizSource;
    private Integer uidType;

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMobile() {
        return this.mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getOperators() {
        return this.operators;
    }

    public void setOperators(Integer operators) {
        this.operators = operators;
    }

    public String getProvinceCode() {
        return this.provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getCityCode() {
        return this.cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public Long getConsumerId() {
        return this.consumerId;
    }

    public void setConsumerId(Long consumerId) {
        this.consumerId = consumerId;
    }

    public Integer getMsgCount() {
        return this.msgCount;
    }

    public void setMsgCount(Integer msgCount) {
        this.msgCount = msgCount;
    }

    public String getReqMsgBody() {
        return this.reqMsgBody;
    }

    public void setReqMsgBody(String reqMsgBody) {
        this.reqMsgBody = reqMsgBody;
    }

    public String getSendMsgBody() {
        return this.sendMsgBody;
    }

    public void setSendMsgBody(String sendMsgBody) {
        this.sendMsgBody = sendMsgBody;
    }

    public Integer getAcceptType() {
        return this.acceptType;
    }

    public void setAcceptType(Integer acceptType) {
        this.acceptType = acceptType;
    }

    public String getSendCode() {
        return this.sendCode;
    }

    public void setSendCode(String sendCode) {
        this.sendCode = sendCode;
    }

    public String getMsgId() {
        return this.msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getBulkMsgId() {
        return this.bulkMsgId;
    }

    public void setBulkMsgId(String bulkMsgId) {
        this.bulkMsgId = bulkMsgId;
    }

    public String getUserMsgId() {
        return this.userMsgId;
    }

    public void setUserMsgId(String userMsgId) {
        this.userMsgId = userMsgId;
    }

    public String getUserBulkMsgId() {
        return this.userBulkMsgId;
    }

    public void setUserBulkMsgId(String userBulkMsgId) {
        this.userBulkMsgId = userBulkMsgId;
    }

    public String getChannelMsgId() {
        return this.channelMsgId;
    }

    public void setChannelMsgId(String channelMsgId) {
        this.channelMsgId = channelMsgId;
    }

    public Long getChannelId() {
        return this.channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public String getFileId() {
        return this.fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getSmsCode() {
        return this.smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }

    public String getSmsStatus() {
        return this.smsStatus;
    }

    public void setSmsStatus(String smsStatus) {
        this.smsStatus = smsStatus;
    }

    public String getErrorMsg() {
        return this.errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Long getAccTime() {
        return this.accTime;
    }

    public void setAccTime(Long accTime) {
        this.accTime = accTime;
    }

    public Long getSendTime() {
        return this.sendTime;
    }

    public void setSendTime(Long sendTime) {
        this.sendTime = sendTime;
    }

    public Long getNotifyTime() {
        return this.notifyTime;
    }

    public void setNotifyTime(Long notifyTime) {
        this.notifyTime = notifyTime;
    }

    public Integer getSmsType() {
        return this.smsType;
    }

    public void setSmsType(Integer smsType) {
        this.smsType = smsType;
    }

    public Integer getSource() {
        return this.source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public String getUid() {
        return this.uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Long getProvinceId() {
        return this.provinceId;
    }

    public void setProvinceId(Long provinceId) {
        this.provinceId = provinceId;
    }

    public Long getCityId() {
        return this.cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPid() {
        return this.pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public Integer getMsg_success_count() {
        return this.msg_success_count;
    }

    public void setMsg_success_count(Integer msg_success_count) {
        this.msg_success_count = msg_success_count;
    }

    public Integer getMsg_fail_count() {
        return this.msg_fail_count;
    }

    public void setMsg_fail_count(Integer msg_fail_count) {
        this.msg_fail_count = msg_fail_count;
    }

    public String getSequence_id() {
        return this.sequence_id;
    }

    public void setSequence_id(String sequence_id) {
        this.sequence_id = sequence_id;
    }

    public String getBizCode() {
        return this.bizCode;
    }

    public void setBizCode(String bizCode) {
        this.bizCode = bizCode;
    }

    public String getBizSource() {
        return this.bizSource;
    }

    public void setBizSource(String bizSource) {
        this.bizSource = bizSource;
    }

    public Integer getUidType() {
        return this.uidType;
    }

    public void setUidType(Integer uidType) {
        this.uidType = uidType;
    }


    public String toJsonString() throws Exception {
        return JsonUtil.toJson(this);
    }


    public SmsSendData() {
    }


    public SmsSendData(String userId, String mobile, OperatorsEnum operators, String provinceCode, String cityCode, Long consumerId, Integer msgCount, String reqMsgBody, String sendMsgBody, AcceptTypeEnum acceptType, String sendCode, String msgId, String bulkMsgId, String userMsgId, String userBulkMsgId, String channelMsgId, Long channelId, String fileId, String smsCode, SmsStatusEnum smsStatus, String errorMsg, Long accTime, Long sendTime, Long notifyTime, SmsTypeEnum smsType, SmsDataSourceEnum sourceEnum, String uid, Long provinceId, Long cityId, Long id, Long pid, Integer msg_success_count, Integer msg_fail_count, String sequence_id, String bizCode, String bizSource) {
        this(userId, mobile, operators, provinceCode, cityCode, consumerId, msgCount, reqMsgBody, sendMsgBody, acceptType, sendCode, msgId, bulkMsgId, userMsgId, userBulkMsgId, channelMsgId, channelId, fileId, smsCode, smsStatus, errorMsg, accTime, sendTime, notifyTime, smsType, sourceEnum, uid, provinceId, cityId, id, pid, msg_success_count, msg_fail_count, sequence_id, bizCode, bizSource, SmsUidTypeEnum.MOBILE);
    }


    public SmsSendData(String userId, String mobile, OperatorsEnum operators, String provinceCode, String cityCode, Long consumerId, Integer msgCount, String reqMsgBody, String sendMsgBody, AcceptTypeEnum acceptType, String sendCode, String msgId, String bulkMsgId, String userMsgId, String userBulkMsgId, String channelMsgId, Long channelId, String fileId, String smsCode, SmsStatusEnum smsStatus, String errorMsg, Long accTime, Long sendTime, Long notifyTime, SmsTypeEnum smsType, SmsDataSourceEnum sourceEnum, String uid, Long provinceId, Long cityId, Long id, Long pid, Integer msg_success_count, Integer msg_fail_count, String sequence_id, String bizCode, String bizSource, SmsUidTypeEnum uidTypeEnum) {
        this.userId = userId;
        this.mobile = mobile;
        this.operators = Integer.valueOf((operators == null) ? OperatorsEnum.UNKNOW.getValue() : operators.getValue());
        this.provinceCode = provinceCode;
        this.cityCode = cityCode;
        this.consumerId = consumerId;
        this.msgCount = msgCount;
        this.reqMsgBody = reqMsgBody;
        this.sendMsgBody = sendMsgBody;
        this.acceptType = (acceptType == null) ? null : Integer.valueOf(acceptType.getValue());
        this.sendCode = sendCode;
        this.msgId = msgId;
        this.bulkMsgId = bulkMsgId;
        this.userMsgId = userMsgId;
        this.userBulkMsgId = userBulkMsgId;
        this.channelMsgId = channelMsgId;
        this.channelId = channelId;
        this.fileId = fileId;
        this.smsCode = smsCode;
        this.smsStatus = (smsStatus == null) ? null : smsStatus.getValue();
        this.errorMsg = errorMsg;
        this.accTime = accTime;
        this.sendTime = sendTime;
        this.notifyTime = notifyTime;
        this.smsType = (smsType == null) ? null : Integer.valueOf(smsType.getValue());
        this.source = Integer.valueOf(sourceEnum.getValue());
        this.uid = uid;
        this.provinceId = provinceId;
        this.cityId = cityId;
        this.id = id;
        this.pid = pid;
        this.msg_success_count = msg_success_count;
        this.msg_fail_count = msg_fail_count;
        this.sequence_id = sequence_id;
        this.bizCode = bizCode;
        this.bizSource = bizSource;
        this.uidType = Integer.valueOf(uidTypeEnum.getValue());
    }
}
