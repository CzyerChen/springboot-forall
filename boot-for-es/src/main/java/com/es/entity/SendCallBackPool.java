package com.es.entity;

import java.io.Serializable;
import java.util.Date;

public class SendCallBackPool
        implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private long userid;
    private int vendor;
    private String city;
    private String biz;
    private String prod;
    private String mobile;
    private int msgcount;
    private String msgbody;
    private String sender;
    private int accepttype;
    private String sendcode;
    private String msgid;
    private String bmsgid;
    private String usermsgid;
    private String userbulkmsgid;
    private String channelmsgid;
    private long channelid;
    private String fileid;
    private String notifyurl;
    private String smscode;
    private String smsstat;
    private int notifycallnum;
    private String sendsmsbody;
    private String parentmsgid;
    private String errormessage;
    private String queuename;
    private long startdeliveryid;
    private Date acctimed;
    private Date sendtimed;
    private Date notifytimed;
    private long acctime;
    private long sendtime;
    private long notifytime;
    private Integer smstype;
    private Integer cityId;
    private Integer provinceId;
    private Long pid;
    private Integer msgSuccessCount;
    private Integer msgFailCount;
    private String identity;
    private String bizCode;
    private String bizSource;
    private String srcUidVersion;
    private String channel;
    private String ruleName;
    private Boolean innerflow;
    private int uidType;




    public SendCallBackPool() {
    }


    public SendCallBackPool(Long id, Long pid, String channelmsgid, long channelid, String smscode, String smsstat, Date acctimed, String identity, Integer smstype, long userid, String bmsgid, String userbulkmsgid, String msgid, String usermsgid, int msgcount, Integer provinceId, int vendor, int accepttype, String sendcode) {
        this.id = id;
        this.pid = pid;
        this.channelmsgid = channelmsgid;
        this.channelid = channelid;
        this.smscode = smscode;
        this.smsstat = smsstat;
        this.acctimed = acctimed;
        this.identity = identity;
        this.smstype = smstype;
        this.userid = userid;
        this.bmsgid = bmsgid;
        this.userbulkmsgid = userbulkmsgid;
        this.msgid = msgid;
        this.usermsgid = usermsgid;
        this.msgcount = msgcount;
        this.provinceId = provinceId;
        this.vendor = vendor;
        this.accepttype = accepttype;
        this.sendcode = sendcode;
    }
    public SendCallBackPool(Long id, Long pid, String channelmsgid, long channelid, String smscode, String smsstat, Date acctimed, String identity, Integer smstype, long userid, String bmsgid, String userbulkmsgid, String msgid, String usermsgid, int msgcount, Integer provinceId, int vendor, int accepttype, String sendcode, String biz, String prod, String city, String channel, String ruleName, Boolean innerflow) {
        this.id = id;
        this.pid = pid;
        this.channelmsgid = channelmsgid;
        this.channelid = channelid;
        this.smscode = smscode;
        this.smsstat = smsstat;
        this.acctimed = acctimed;
        this.identity = identity;
        this.smstype = smstype;
        this.userid = userid;
        this.bmsgid = bmsgid;
        this.userbulkmsgid = userbulkmsgid;
        this.msgid = msgid;
        this.usermsgid = usermsgid;
        this.msgcount = msgcount;
        this.provinceId = provinceId;
        this.vendor = vendor;
        this.accepttype = accepttype;
        this.sendcode = sendcode;
        this.biz = biz;
        this.prod = prod;
        this.city = city;
        this.channel = channel;
        this.ruleName = ruleName;
        this.innerflow = innerflow;
    }


    public SendCallBackPool(Long id, long channelid, long userid, Integer provinceId, Integer smstype, int vendor, String smsstat, String sender, Date sendtimed, Date acctimed, String mobile, String msgid, String bmsgid, String usermsgid, String userbulkmsgid, Integer accepttype) {
        this.id = id;
        this.channelid = channelid;
        this.userid = userid;
        this.provinceId = provinceId;
        this.smstype = smstype;
        this.sendtimed = sendtimed;
        this.acctimed = acctimed;
        this.vendor = vendor;
        this.smsstat = smsstat;
        this.sender = sender;
        this.mobile = mobile;
        this.msgid = msgid;
        this.bmsgid = bmsgid;
        this.usermsgid = usermsgid;
        this.userbulkmsgid = userbulkmsgid;
        this.accepttype = accepttype.intValue();
    }


    public SendCallBackPool(Long id, long channelid, long userid, Integer provinceId, Integer smstype, int vendor, String smsstat, String sender, Date sendtimed, Date acctimed, String mobile, String msgid, String bmsgid, String usermsgid, String userbulkmsgid, Integer accepttype, Long pid, String sendcode, Integer msgSuccessCount, Integer msgFailCount) {
        this.id = id;
        this.channelid = channelid;
        this.userid = userid;
        this.provinceId = provinceId;
        this.smstype = smstype;
        this.sendtimed = sendtimed;
        this.acctimed = acctimed;
        this.vendor = vendor;
        this.smsstat = smsstat;
        this.sender = sender;
        this.mobile = mobile;
        this.msgid = msgid;
        this.bmsgid = bmsgid;
        this.usermsgid = usermsgid;
        this.userbulkmsgid = userbulkmsgid;
        this.accepttype = accepttype.intValue();
        this.pid = pid;
        this.sendcode = sendcode;
        this.msgSuccessCount = msgSuccessCount;
        this.msgFailCount = msgFailCount;
    }

    public Integer getSmstype() {
        return this.smstype;
    }

    public void setSmstype(Integer smstype) {
        this.smstype = smstype;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getUserid() {
        return this.userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    public int getVendor() {
        return this.vendor;
    }

    public void setVendor(int vendor) {
        this.vendor = vendor;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getMobile() {
        return this.mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getMsgcount() {
        return this.msgcount;
    }

    public void setMsgcount(int msgcount) {
        this.msgcount = msgcount;
    }

    public String getMsgbody() {
        return this.msgbody;
    }

    public void setMsgbody(String msgbody) {
        this.msgbody = msgbody;
    }

    public String getSender() {
        return this.sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public int getAccepttype() {
        return this.accepttype;
    }

    public void setAccepttype(int accepttype) {
        this.accepttype = accepttype;
    }

    public String getSendcode() {
        return this.sendcode;
    }

    public void setSendcode(String sendcode) {
        this.sendcode = sendcode;
    }

    public String getMsgid() {
        return this.msgid;
    }

    public void setMsgid(String msgid) {
        this.msgid = msgid;
    }

    public String getBmsgid() {
        return this.bmsgid;
    }

    public void setBmsgid(String bmsgid) {
        this.bmsgid = bmsgid;
    }

    public String getUsermsgid() {
        return this.usermsgid;
    }

    public void setUsermsgid(String usermsgid) {
        this.usermsgid = usermsgid;
    }

    public String getUserbulkmsgid() {
        return this.userbulkmsgid;
    }

    public void setUserbulkmsgid(String userbulkmsgid) {
        this.userbulkmsgid = userbulkmsgid;
    }

    public String getChannelmsgid() {
        return this.channelmsgid;
    }

    public void setChannelmsgid(String channelmsgid) {
        this.channelmsgid = channelmsgid;
    }

    public long getChannelid() {
        return this.channelid;
    }

    public void setChannelid(long channelid) {
        this.channelid = channelid;
    }

    public String getFileid() {
        return this.fileid;
    }

    public void setFileid(String fileid) {
        this.fileid = fileid;
    }

    public String getNotifyurl() {
        return this.notifyurl;
    }

    public void setNotifyurl(String notifyurl) {
        this.notifyurl = notifyurl;
    }

    public String getSmscode() {
        return this.smscode;
    }

    public void setSmscode(String smscode) {
        this.smscode = smscode;
    }

    public String getSmsstat() {
        return this.smsstat;
    }

    public void setSmsstat(String smsstat) {
        this.smsstat = smsstat;
    }

    public int getNotifycallnum() {
        return this.notifycallnum;
    }

    public void setNotifycallnum(int notifycallnum) {
        this.notifycallnum = notifycallnum;
    }

    public String getSendsmsbody() {
        return this.sendsmsbody;
    }

    public void setSendsmsbody(String sendsmsbody) {
        this.sendsmsbody = sendsmsbody;
    }

    public String getParentmsgid() {
        return this.parentmsgid;
    }

    public void setParentmsgid(String parentmsgid) {
        this.parentmsgid = parentmsgid;
    }

    public String getErrormessage() {
        return this.errormessage;
    }

    public void setErrormessage(String errormessage) {
        this.errormessage = errormessage;
    }

    public String getQueuename() {
        return this.queuename;
    }

    public void setQueuename(String queuename) {
        this.queuename = queuename;
    }

    public long getStartdeliveryid() {
        return this.startdeliveryid;
    }

    public void setStartdeliveryid(long startdeliveryid) {
        this.startdeliveryid = startdeliveryid;
    }

    public Date getAcctimed() {
        return this.acctimed;
    }

    public void setAcctimed(Date acctimed) {
        this.acctimed = acctimed;
    }

    public Date getSendtimed() {
        return this.sendtimed;
    }

    public void setSendtimed(Date sendtimed) {
        this.sendtimed = sendtimed;
    }

    public Date getNotifytimed() {
        return this.notifytimed;
    }

    public void setNotifytimed(Date notifytimed) {
        this.notifytimed = notifytimed;
    }

    public long getAcctime() {
        return this.acctime;
    }

    public void setAcctime(long acctime) {
        this.acctime = acctime;
    }

    public long getSendtime() {
        return this.sendtime;
    }

    public void setSendtime(long sendtime) {
        this.sendtime = sendtime;
    }

    public long getNotifytime() {
        return this.notifytime;
    }

    public void setNotifytime(long notifytime) {
        this.notifytime = notifytime;
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

    public Long getPid() {
        return this.pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public Integer getMsgSuccessCount() {
        return this.msgSuccessCount;
    }

    public void setMsgSuccessCount(Integer msgSuccessCount) {
        this.msgSuccessCount = msgSuccessCount;
    }

    public Integer getMsgFailCount() {
        return this.msgFailCount;
    }

    public void setMsgFailCount(Integer msgFailCount) {
        this.msgFailCount = msgFailCount;
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

    public String getIdentity() {
        return this.identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getSrcUidVersion() {
        return this.srcUidVersion;
    }

    public void setSrcUidVersion(String srcUidVersion) {
        this.srcUidVersion = srcUidVersion;
    }

    public int getUidType() {
        return this.uidType;
    }

    public void setUidType(int uidType) {
        this.uidType = uidType;
    }

    public String getBiz() {
        return biz;
    }

    public void setBiz(String biz) {
        this.biz = biz;
    }

    public String getProd() {
        return prod;
    }

    public void setProd(String prod) {
        this.prod = prod;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public Boolean getInnerflow() {
        return innerflow;
    }

    public void setInnerflow(Boolean innerflow) {
        this.innerflow = innerflow;
    }
}


