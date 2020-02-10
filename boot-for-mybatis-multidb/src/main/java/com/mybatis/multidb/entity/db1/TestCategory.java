/**
 * Author:   claire
 * Date:    2020-02-08 - 16:36
 * Description: db1-分组测试
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2020-02-08 - 16:36          V1.3.1           db1-分组测试
 */
package com.mybatis.multidb.entity.db1;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 功能简述 <br/> 
 * 〈db1-分组测试〉
 *
 * @author claire
 * @date 2020-02-08 - 16:36
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("test_category")
public class TestCategory {
    private Integer id;
    private Integer categoryId;
    private String categoryName;
    private String categoryDesc;
}
