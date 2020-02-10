/**
 * Author:   claire
 * Date:    2020-02-08 - 16:57
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2020-02-08 - 16:57          V1.3.1
 */
package com.mybatis.multidb.entity.db3;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 功能简述 <br/> 
 * 〈db3 产品〉
 *
 * @author claire
 * @date 2020-02-08 - 16:57
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("test_product")
public class TestProduct {
    private Integer id;
    private String tenantId;
    private String materialId;
    private String supplierId;
    private Integer unitPrice;
}
