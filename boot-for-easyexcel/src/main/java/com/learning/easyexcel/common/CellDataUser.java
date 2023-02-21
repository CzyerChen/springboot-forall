/**
 * Author:   claire
 * Date:    2023/2/20 - 1:03 下午
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2023/2/20 - 1:03 下午          V1.0.0
 */
package com.learning.easyexcel.common;

import com.alibaba.excel.metadata.data.CellData;
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
public class CellDataUser {
    private CellData<String> cname;
    // 这里注意 虽然是日期 但是 类型 存储的是number 因为excel 存储的就是number
    private CellData<Date> createTime;
    private CellData<Double> score;
    // 这里并不一定能完美的获取 有些公式是依赖性的 可能会读不到 这个问题后续会修复
    private CellData<String> formulaValue;
}
