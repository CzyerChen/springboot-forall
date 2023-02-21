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

/**
 * 功能简述 
 * 〈〉
 *
 * @author claire
 * @date 2023/2/20 - 1:03 下午
 * @since 1.0.0
 */
@Data
public class UserFormat {
    /**
     * 我自定义 转换器，不管数据库传过来什么 。我给他加上“自定义：”
     */
    @ExcelProperty(converter = CustomStringFormatConverter.class)
    private String cname;
    /**
     * 这里用string 去接日期才能格式化。我想接收年月日格式
     */
    @DateTimeFormat("yyyy年MM月dd日HH时mm分ss秒")
    @ExcelProperty("时间")
    private String createTime;
    /**
     * 我想接收百分比的数字
     */
    @NumberFormat("#.##%")
    @ExcelProperty("学分")
    private String rate;
}
