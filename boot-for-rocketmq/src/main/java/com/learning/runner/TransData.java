/**
 * Author:   claire
 * Date:    2023/11/1 - 1:32 下午
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2023/11/1 - 1:32 下午          V1.0.0
 */
package com.learning.runner;

/**
 *
 * @author claire
 * @date 2023/11/1 - 1:32 下午
 * @since 1.0.0
 */
public class TransData {
    private String param1;
    private String param2;

    public String getParam1() {
        return param1;
    }

    public void setParam1(String param1) {
        this.param1 = param1;
    }

    public String getParam2() {
        return param2;
    }

    public void setParam2(String param2) {
        this.param2 = param2;
    }

    @Override
    public String toString() {
        return "TransData{" +
                "param1='" + param1 + '\'' +
                ", param2='" + param2 + '\'' +
                '}';
    }
}
