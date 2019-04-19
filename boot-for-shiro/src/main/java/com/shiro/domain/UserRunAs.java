package com.shiro.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -04 - 16 9:52
 */
@Entity
@Table(name = "sys_user_runas")
@IdClass(RunAsIdClass.class)
public class UserRunAs implements Serializable {
    private Long fromUserId;//授予身份帐号
    private Long toUserId;//被授予身份帐号
    private long timeStamp;

    @Id
    public Long getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(Long fromUserId) {
        this.fromUserId = fromUserId;
    }

    @Id
    public Long getToUserId() {
        return toUserId;
    }

    public void setToUserId(Long toUserId) {
        this.toUserId = toUserId;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
