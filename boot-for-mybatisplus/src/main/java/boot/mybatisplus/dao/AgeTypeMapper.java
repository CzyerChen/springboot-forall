/**
 * Author:   claire
 * Date:    2020-02-11 - 10:40
 * Description: 年龄code-名称映射DAO类
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2020-02-11 - 10:40          V1.3.1           年龄code-名称映射DAO类
 */
package boot.mybatisplus.dao;

import boot.mybatisplus.domain.AgeTypeMapping;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 功能简述
 * 〈年龄code-名称映射DAO类〉
 *
 * @author claire
 * @date 2020-02-11 - 10:40
 * @since 1.3.1
 */
@DS("metadata")
public interface AgeTypeMapper extends BaseMapper<AgeTypeMapping> {
}
