/**
 * Author:   claire
 * Date:    2022/10/24 - 12:56 下午
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2022/10/24 - 12:56 下午          V1.0.0
 */
package com.learning.doudian.domain;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.io.Serializable;

/**
 * 功能简述 
 * 〈〉
 *
 * @author claire
 * @date 2022/10/24 - 12:56 下午
 * @since 1.0.0
 */
@Data
public class DoudianPushData implements Serializable {
    /**
     * 消息种类
     */
    private String tag;

    /**
     * 消息记录ID
     */
    private String msgId;

    /**
     * 消息体具体数据
     */
    private String data;

    public <T> T toObject(Class<T> tClass) {
        if (StringUtils.isEmpty(data)) {
            return null;
        }
        return JSON.parseObject(data, tClass);
    }

}
