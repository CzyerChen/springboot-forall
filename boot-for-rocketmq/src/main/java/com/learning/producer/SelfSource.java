/**
 * Author:   claire
 * Date:    2023/10/25 - 1:31 下午
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2023/10/25 - 1:31 下午          V1.0.0
 */
package com.learning.producer;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 *
 * @author claire
 * @date 2023/10/25 - 1:31 下午
 * @since 1.0.0
 */
public interface SelfSource {
    public static String TEXT_OUTPUT="textoutput";
    public static String TEXT_OUTPUT2="textoutput2";
    public static String POJO_OUTPUT="pojooutput";
    public static String TRANS_OUTPUT="transoutput";

    @Output(TEXT_OUTPUT)
    MessageChannel textOutput();

    @Output(TEXT_OUTPUT2)
    MessageChannel textOutput2();

    @Output(POJO_OUTPUT)
    MessageChannel pojoOutput();

    @Output(TRANS_OUTPUT)
    MessageChannel transOutput();
}
