/**
 * Author:   claire
 * Date:    2020-02-08 - 15:42
 * Description: age_type表测试
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2020-02-08 - 15:42          V1.3.1           age_type表测试
 */
package com.mybatis.multidb.entity.db2;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 功能简述 <br/> 
 * 〈db2-age_type表〉
 *
 * @author claire
 * @date 2020-02-08 - 15:42
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("dim_age_type")
public class TestAgeType {
    private Integer ageCode;
    private String codeDescription;
}
