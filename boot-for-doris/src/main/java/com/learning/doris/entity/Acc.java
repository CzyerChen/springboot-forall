/**
 * Author:   claire Date:    2025/5/23 - 16:37 Description: History:
 * <author>          <time>                   <version>          <desc>
 * claire          2025/5/23 - 16:37          V1.0.0
 */

package com.learning.doris.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.Date;

@Entity
public class Acc {

    @Id
    private Long id;

    /**
     * pDate
     */
    private Date pDate;

    /**
     * SIPCallID
     */
    private String callid;

    /**
     * Duration
     */
    private Integer duration;

    /**
     * ms_duration
     */
    private Integer msDuration;

    /**
     * 呼入主叫
     */
    private String callerIn;

    /**
     * 被叫经由网关
     */
    private String calleegateway;

    /**
     * 被叫结算账户
     */
    private String calleeaccount;




    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(final Integer duration) {
        this.duration = duration;
    }

    public Integer getMsDuration() {
        return msDuration;
    }

    public void setMsDuration(final Integer msDuration) {
        this.msDuration = msDuration;
    }

    public String getCalleegateway() {
        return calleegateway;
    }

    public void setCalleegateway(final String calleegateway) {
        this.calleegateway = calleegateway;
    }

    public String getCallerIn() {
        return callerIn;
    }

    public void setCallerIn(final String callerIn) {
        this.callerIn = callerIn;
    }

    public String getCalleeaccount() {
        return calleeaccount;
    }

    public void setCalleeaccount(final String calleeaccount) {
        this.calleeaccount = calleeaccount;
    }

    public String getCallid() {
        return callid;
    }

    public void setCallid(final String callid) {
        this.callid = callid;
    }

    public Date getpDate() {
        return pDate;
    }

    public void setpDate(final Date pDate) {
        this.pDate = pDate;
    }
}