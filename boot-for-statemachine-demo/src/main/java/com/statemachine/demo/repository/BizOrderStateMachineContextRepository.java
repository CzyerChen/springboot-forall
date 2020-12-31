/**
 * Author:   claire
 * Date:    2020-12-28 - 21:12
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2020-12-28 - 21:12          V1.13.0
 */
package com.statemachine.demo.repository;

import java.awt.print.Pageable;

/**
 * 功能简述 <br/> 
 * 〈〉
 *
 * @author claire
 * @date 2020-12-28 - 21:12
 */
@Repository
public interface BizOrderStateMachineContextRepository {

    int deleteByPrimaryKey(Long id);

    BizOrderStateMachineContext selectByOrderId(String bizOrderId);

    int updateByPrimaryKey(BizOrderStateMachineContext BizOrderStateMachineContext);

    int updateByPrimaryKeySelective(BizOrderStateMachineContext BizOrderStateMachineContext);

    int insertSelective(BizOrderStateMachineContext BizOrderStateMachineContext);

    int selectCount(BizOrderStateMachineContext BizOrderStateMachineContext);

    List<BizOrderStateMachineContext> selectPage(@Param("BizOrderStateMachineContext") BizOrderStateMachineContext BizOrderStateMachineContext, @Param("pageable") Pageable pageable);

}
