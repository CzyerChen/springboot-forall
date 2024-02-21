/**
 * Author:   claire Date:    2024/2/18 - 4:26 下午 Description: History:
 * <author>          <time>                   <version>          <desc>
 * claire          2024/2/18 - 4:26 下午          V1.0.0
 */

package com.learning.bootforclickhouse.domain;

import java.util.Date;

/**
 * 功能简述 
 * 〈〉
 *
 * @author claire
 * @date 2024/2/18 - 4:26 下午
 * @since 1.0.0
 */
public class MyFirstTable {
    private Integer userId;
    private String message;
    private Date timestamp;
    private Float metric;

    public MyFirstTable(final Integer userId, final String message, final Date timestamp, final Float metric) {
        this.userId = userId;
        this.message = message;
        this.timestamp = timestamp;
        this.metric = metric;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(final Integer userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(final Date timestamp) {
        this.timestamp = timestamp;
    }

    public Float getMetric() {
        return metric;
    }

    public void setMetric(final Float metric) {
        this.metric = metric;
    }
}
