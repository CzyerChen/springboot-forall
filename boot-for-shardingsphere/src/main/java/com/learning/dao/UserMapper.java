/**
 * Author:   claire
 * Date:    2021-01-07 - 11:29
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2021-01-07 - 11:29          V1.14.0
 */
package com.learning.dao;

import com.learning.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 功能简述 <br/> 
 * 〈〉
 *
 * @author claire
 * @date 2021-01-07 - 11:29
 */
public interface UserMapper {
    int deleteByPrimaryKey(Long userId);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Long userId);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

//    List<User> listByCondition(@Param("condition") ListUserVo query);
//
//    int count(@Param("condition") ListUserVo query);

}
