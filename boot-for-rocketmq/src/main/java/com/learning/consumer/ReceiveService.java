/**
 * Author:   claire
 * Date:    2023/10/25 - 1:51 下午
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2023/10/25 - 1:51 下午          V1.0.0
 */
package com.learning.consumer;

import com.learning.runner.MessageDto;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.support.ErrorMessage;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 *
 * @author claire
 * @date 2023/10/25 - 1:51 下午
 * @since 1.0.0
 */
@Service
public class ReceiveService {

    @StreamListener(value = SelfSink.TEXT_INPUT)
    public void textInput(String message) {
        System.out.println("receive group1 content:" + message);
        throw new IllegalStateException("异常信息1");
    }

//    @StreamListener(value = SelfSink.TEXT_INPUT2)
//    public void textInput2(String message) {
//        System.out.println("receive group2 content:" + message);
//    }

    @StreamListener(value = SelfSink.TEXT_INPUT3,condition = "headers['ROCKET_TAGS'] == 'tagfilter'")
    public void textInput3WithTag(String message, @Headers Map headers, @Header(name = "ROCKET_TAGS")String name) {
        System.out.println("receive group2 tagfilter content:" + message +", headers="+headers +", name="+name);
        throw new IllegalArgumentException("异常信息3");
    }

    @StreamListener(value = SelfSink.POJO_INPUT)
    public void pojoInout(@Payload MessageDto messageDto){
//        System.out.println("receive pojo-group content:" + messageDto.toString());
        System.out.println("[onMessage][线程编号:{} 消息内容：{}]" + Thread.currentThread().getId()+", "+messageDto.toString());
    }

    @StreamListener(value = SelfSink.TRANS_INPUT)
    public void transInput(String message,@Header(name = "args")String args){
        System.out.println("receive trans content:" + message +", args="+args);
    }

    @ServiceActivator(inputChannel = "text-topic.text-group.errors")
    public void handleError(ErrorMessage errorMessage){
        Throwable throwable = errorMessage.getPayload();
        System.out.println("定向异常："+throwable);
        Message<?> originalMessage = errorMessage.getOriginalMessage();
//        System.out.println(Objects.nonNull(originalMessage)?"处理定向异常原始信息: "+ originalMessage :"无");
        assert originalMessage != null;
        System.out.println("处理定向异常原始信息："+new String((byte[])originalMessage.getPayload()));
    }

    @StreamListener("errorChannel")
    public void error(Message<?> message){
        ErrorMessage errorMessage = (ErrorMessage)message;
        System.out.println("其他异常:"+errorMessage);
    }
}
