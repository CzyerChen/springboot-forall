package com.boot.entity;

import javax.persistence.*;
import java.beans.Transient;
import java.io.Serializable;
import java.util.Date;


@Entity
@Table(name = "smsreply")
public class SmsReplyReceive
        implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private Long id;
    @Column(name = "destaddress")
    private String destAddress;
    transient private String sourceAddress;

    @Column(name = "datacode")
    private String dataCode;

    @Column(name = "mocontent")
    private String moContent;

    @Column(name = "spid")
    private String spid;

    @Column(name = "momsgid")
    private String momsgid;

    @Column(name = "channelid")
    private Long channelId;

    @Column(name = "acctime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date acctime;

    @Column(name = "uid")
    private Long uid;

    transient private Long callbacktime;


    transient private String msgid;


    transient private String usermsgid;


    transient private Integer replyNum = Integer.valueOf(0);


    transient private String replyUrl;


    @Column(name = "createtime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;


    @Column(name = "likeuids")
    private String likeUids;


    transient private Integer cityId;


    transient private Integer provinceId;


    @Column(name = "identity")
    private String identity;


    transient private String bizCode;


    transient private String bizSource;


    transient private long lastHandleTime;


    transient private int uidType;


    public long getLastHandleTime() {
        return this.lastHandleTime;
    }

    public void setLastHandleTime(long lastHandleTime) {
        this.lastHandleTime = lastHandleTime;
    }

    public String getDestAddress() {
        return this.destAddress;
    }

    public void setDestAddress(String destAddress) {
        this.destAddress = destAddress;
    }

    public String getSourceAddress() {
        return this.sourceAddress;
    }

    public void setSourceAddress(String sourceAddress) {
        this.sourceAddress = sourceAddress;
    }

    public String getDataCode() {
        return this.dataCode;
    }

    public void setDataCode(String dataCode) {
        this.dataCode = dataCode;
    }

    public String getMoContent() {
        return this.moContent;
    }

    public void setMoContent(String shortMessage) {
        this.moContent = shortMessage;
    }

    public String getSpid() {
        return this.spid;
    }

    public void setSpid(String spid) {
        this.spid = spid;
    }

    public String getMomsgid() {
        return this.momsgid;
    }

    public void setMomsgid(String momsgid) {
        this.momsgid = momsgid;
    }

    public Long getChannelId() {
        return this.channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public Date getAcctime() {
        return this.acctime;
    }

    public void setAcctime(Date acctime) {
        this.acctime = acctime;
    }

    public Integer getReplyNum() {
        return this.replyNum;
    }

    public void setReplyNum(Integer replyNum) {
        this.replyNum = replyNum;
    }

    public Long getUid() {
        return this.uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReplyUrl() {
        return this.replyUrl;
    }

    public void setReplyUrl(String replyUrl) {
        this.replyUrl = replyUrl;
    }

    public Long getCallbacktime() {
        return this.callbacktime;
    }

    public void setCallbacktime(Long callbacktime) {
        this.callbacktime = callbacktime;
    }

    public String getMsgid() {
        return this.msgid;
    }

    public void setMsgid(String msgid) {
        this.msgid = msgid;
    }

    public String getUsermsgid() {
        return this.usermsgid;
    }

    public void setUsermsgid(String usermsgid) {
        this.usermsgid = usermsgid;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getLikeUids() {
        return this.likeUids;
    }

    public void setLikeUids(String likeUids) {
        this.likeUids = likeUids;
    }

    public Integer getCityId() {
        return this.cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public Integer getProvinceId() {
        return this.provinceId;
    }

    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }

    public String getIdentity() {
        return this.identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
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

    public int getUidType() {
        return this.uidType;
    }

    public void setUidType(int uidType) {
        this.uidType = uidType;
    }
}