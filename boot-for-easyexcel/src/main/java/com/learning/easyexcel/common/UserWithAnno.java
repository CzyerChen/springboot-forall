/**
 * Author:   claire
 * Date:    2023/2/20 - 1:03 下午
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2023/2/20 - 1:03 下午          V1.0.0
 */
package com.learning.easyexcel.common;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * 功能简述 
 * 〈〉
 *
 * @author claire
 * @date 2023/2/20 - 1:03 下午
 * @since 1.0.0
 */
@Data
public class UserWithAnno {
    /**
     * 强制读取第三个 这里不建议 index 和 name 同时用，要么一个对象只用index，要么一个对象只用name去匹配
     */
    @ExcelProperty(index = 1)
    private String cname;
    /**
     * 用名字去匹配，这里需要注意，如果名字重复，会导致只有一个字段读取到数据
     */
    @ExcelProperty("英文名")
    private String ename;
    @ExcelProperty("年龄")
    private Integer age;
}
