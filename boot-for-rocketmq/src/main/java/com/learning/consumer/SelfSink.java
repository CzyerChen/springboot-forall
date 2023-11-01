/**
 * Author:   claire
 * Date:    2023/10/25 - 1:31 下午
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2023/10/25 - 1:31 下午          V1.0.0
 */
package com.learning.consumer;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 *
 * @author claire
 * @date 2023/10/25 - 1:31 下午
 * @since 1.0.0
 */
public interface SelfSink {
    public static String TEXT_INPUT="textinput";
    public static String TEXT_INPUT2="textinput2";
    public static String TEXT_INPUT3="textinput3";
    public static String POJO_INPUT="pojoinput";
    public static String TRANS_INPUT="transinput";

    @Input(TEXT_INPUT)
    SubscribableChannel textInput();

//    @Input(TEXT_INPUT2)
//    SubscribableChannel textInput2();

    @Input(TEXT_INPUT3)
    SubscribableChannel textInput3();

    @Input(POJO_INPUT)
    SubscribableChannel pojoInput();

    @Input(TRANS_INPUT)
    SubscribableChannel transInput();
}
