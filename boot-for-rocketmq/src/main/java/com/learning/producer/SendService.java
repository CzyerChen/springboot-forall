/**
 * Author:   claire
 * Date:    2023/10/25 - 1:52 下午
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2023/10/25 - 1:52 下午          V1.0.0
 */
package com.learning.producer;

import com.alibaba.fastjson.JSON;
import com.learning.runner.MessageDto;
import com.learning.runner.TransData;
import org.apache.rocketmq.common.message.MessageConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

/**
 *
 * @author claire
 * @date 2023/10/25 - 1:52 下午
 * @since 1.0.0
 */
@Service
public class SendService {

    @Autowired
    private SelfSource source;

    public void sendText(String msg) {
        Message<String> message = (Message<String>) MessageBuilder.withPayload(msg).build();
        source.textOutput().send(message);
    }

    public void sendText2(String msg) {
        Message<String> message = (Message<String>) MessageBuilder.withPayload(msg).build();
        source.textOutput2().send(message);
    }

    public void sendWithTag(String msg, String tag) {
        Message<String> message = MessageBuilder.withPayload(msg)
                .setHeader(MessageConst.PROPERTY_TAGS, tag)
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                .build();
        source.textOutput2().send(message);
    }

    public void sendPojoOrderly(MessageDto messageDto) {
        Message<MessageDto> message = MessageBuilder.withPayload(messageDto)
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                .setHeader(MessageConst.PROPERTY_TAGS, "myTag")
                .build();
        System.out.println(message);
        source.pojoOutput().send(message);
    }

    public void sendTrans(String transMessage) {
        TransData data = new TransData();
        data.setParam1("data1");
        data.setParam2("data2");
        Message<String> message = MessageBuilder.withPayload(transMessage)
                .setHeader("args", JSON.toJSONString(data))
                .build();
        source.transOutput().send(message);
    }
}
