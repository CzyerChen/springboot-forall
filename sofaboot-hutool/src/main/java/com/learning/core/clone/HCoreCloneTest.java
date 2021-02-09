/**
 * Author:   claire
 * Date:    2021-02-09 - 14:01
 * Description: 克隆测试
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2021-02-09 - 14:01          V1.17.0          克隆测试
 */
package com.learning.core.clone;


import cn.hutool.core.clone.CloneRuntimeException;
import cn.hutool.core.clone.Cloneable;

/**
 * 功能简述 
 * 〈克隆测试〉
 *
 * @author claire
 * @date 2021-02-09 - 14:01
 */
public class HCoreCloneTest implements Cloneable<HCoreCloneTest> {

    /*避免调用对象的clone()方法就会抛出CloneNotSupportedException异常*/
    @Override
    public HCoreCloneTest clone() {
        try {
            return (HCoreCloneTest) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new CloneRuntimeException(e);
        }
    }


}
