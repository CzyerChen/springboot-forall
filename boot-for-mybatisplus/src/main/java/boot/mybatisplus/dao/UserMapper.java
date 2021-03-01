/**
 * Author:   claire
 * Date:    2021-02-25 - 16:54
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2021-02-25 - 16:54          V1.17.0
 */
package boot.mybatisplus.dao;

import boot.mybatisplus.domain.AuthUser;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 功能简述 
 * 〈〉
 *
 * @author claire
 * @date 2021-02-25 - 16:54
 */
@DS("app")
public interface UserMapper extends BaseMapper<AuthUser> {
}
