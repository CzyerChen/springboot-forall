package com.es.entity;


import com.es.util.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "sms_send_history_202208")
@Data
@AllArgsConstructor
public class SmsSendHistData
   implements IMsgData
 {
   @Id
   private Long id;
   private String es_uid;
   private Long userid;
   private Integer vendor;
   @Field(type = FieldType.Keyword)
   private String city;
   @Field(type = FieldType.Keyword)
   private String mobile;
   private Integer msgcount;
   @Field(type = FieldType.Keyword)
   private String msgbody;
   @Field(type = FieldType.Keyword)
   private String sender;
   private Integer accepttype;
   @Field(type = FieldType.Keyword)
   private String sendcode;
   @Field(type = FieldType.Keyword)
   private String msgid;
   @Field(type = FieldType.Keyword)
   private String bmsgid;
   @Field(type = FieldType.Keyword)
   private String usermsgid;
   @Field(type = FieldType.Keyword)
   private String userbulkmsgid;
   @Field(type = FieldType.Keyword)
   private String channelmsgid;
   private Long channelid;
   @Field(type = FieldType.Keyword)
   private String fileid;
   @Field(type = FieldType.Keyword)
   private String notifyurl;
   @Field(type = FieldType.Keyword)
   private String smscode;
   @Field(type = FieldType.Keyword)
   private String smsstat;
   private Integer notifycallnum;
   @Field(type = FieldType.Keyword)
   private String sendsmsbody;
   @Field(type = FieldType.Keyword)
   private String parentmsgid;
   @Field(type = FieldType.Keyword)
   private String errormessage;
   @Field(type = FieldType.Keyword)
   private String queuename;
   private Long startdeliveryid;
   private Long acctime;
   private Long sendtime;
   private Long notifytime;
   private Integer smstype;
   private Integer cityId;
   private Integer provinceId;
   private Long pid;
   private Integer msg_success_count;
   private Integer msg_fail_count;
   @Field(type = FieldType.Keyword)
   private String sequence_id;
   @Field(type = FieldType.Keyword)
   private String bizCode;
   @Field(type = FieldType.Keyword)
   private String bizSource;

   public String getEs_uid() {
     return this.es_uid;
   }

   public void setEs_uid(String es_uid) {
     this.es_uid = es_uid;
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

   public Long getUserid() {
     return this.userid;
   }


   public void setUserid(Long userid) {
     this.userid = userid;
   }


   public Integer getVendor() {
     return this.vendor;
   }


   public void setVendor(Integer vendor) {
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


   public Integer getMsgcount() {
     return this.msgcount;
   }


   public void setMsgcount(Integer msgcount) {
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


   public Integer getAccepttype() {
     return this.accepttype;
   }


   public void setAccepttype(Integer accepttype) {
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


   public Long getChannelid() {
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

   public Integer getNotifycallnum() {
     return notifycallnum;
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


   public Long getStartdeliveryid() {
     return this.startdeliveryid;
   }


   public void setStartdeliveryid(long startdeliveryid) {
     this.startdeliveryid = startdeliveryid;
   }


   public Long getAcctime() {
     return this.acctime;
   }


   public void setAcctime(long acctime) {
     this.acctime = acctime;
   }


   public Long getSendtime() {
     return this.sendtime;
   }


   public void setSendtime(Long sendtime) {
     this.sendtime = sendtime;
   }


   public Long getNotifytime() {
     return this.notifytime;
   }


   public void setNotifytime(Long notifytime) {
     this.notifytime = notifytime;
   }





   public Integer getSmstype() {
     return this.smstype;
   }





   public void setSmstype(Integer smstype) {
     this.smstype = smstype;
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


   public void setChannelid(Long channelid) {
     this.channelid = channelid;
   }

   public void setNotifycallnum(Integer notifycallnum) {
     this.notifycallnum = notifycallnum;
   }

   public void setStartdeliveryid(Long startdeliveryid) {
     this.startdeliveryid = startdeliveryid;
   }

   public void setAcctime(Long acctime) {
     this.acctime = acctime;
   }

   public void setProvinceId(Integer provinceId) {
     this.provinceId = provinceId;
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


   public SmsSendHistData() {}

   public SmsSendHistData(SmsSendData smsSendData) {
     setEs_uid(smsSendData.getUserId());
     setUserid(smsSendData.getConsumerId());
     setMobile(smsSendData.getMobile());
     setVendor(smsSendData.getOperators());
     setProvinceId(Integer.parseInt(smsSendData.getProvinceCode()));
     setCity(smsSendData.getCityCode());
     setMsgcount(smsSendData.getMsgCount());
     setMsgbody(smsSendData.getReqMsgBody());
     setSendsmsbody(smsSendData.getSendMsgBody());
     setAccepttype(smsSendData.getAcceptType());
     setSendcode(smsSendData.getSendCode());
     setMsgid(smsSendData.getMsgId());
     setBmsgid(smsSendData.getBulkMsgId());
     setUsermsgid(smsSendData.getUserMsgId());
     setUserbulkmsgid(smsSendData.getUserBulkMsgId());
     setChannelmsgid(smsSendData.getChannelMsgId());
     setChannelid(smsSendData.getChannelId().longValue());
     setFileid(smsSendData.getFileId());
     setSmscode(smsSendData.getSmsCode());
     setSmsstat(smsSendData.getSmsStatus());
     setErrormessage(smsSendData.getErrorMsg());
     setAcctime(smsSendData.getAccTime().longValue());
     setSendtime(smsSendData.getSendTime());
     setNotifytime(smsSendData.getNotifyTime());
     setSmstype(smsSendData.getSmsType());
     setId(smsSendData.getId());
     setPid(smsSendData.getPid());
     setMsg_success_count(smsSendData.getMsg_success_count());
     setMsg_fail_count(smsSendData.getMsg_fail_count());
     setSequence_id(smsSendData.getSequence_id());
   }



   @Override
   public String toJsonString() throws Exception {
     return JsonUtil.toJson(this);
   }
 }
