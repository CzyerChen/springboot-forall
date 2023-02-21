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
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.format.NumberFormat;
import com.learning.easyexcel.read.CustomStringFormatConverter;
import lombok.Data;

import java.util.Date;

/**
 * 功能简述 
 * 〈〉
 *
 * @author claire
 * @date 2023/2/20 - 1:03 下午
 * @since 1.0.0
 */
@Data
public class UserWriteFormat {
    @ExcelProperty(value = "学生姓名", index = 0)
    private String cname;
    @ExcelProperty(value = "创建时间", index = 1)
    private Date createTime;
    /**
     * 这里设置3 会导致第二列空的
     */
    @ExcelProperty(value = "学分", index = 3)
    private Double score;
}
