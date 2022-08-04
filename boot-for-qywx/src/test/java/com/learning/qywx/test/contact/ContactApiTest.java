/**
 * Author:   claire
 * Date:    2022/4/14 - 6:23 下午
 * Description: 通讯录功能测试
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2022/4/14 - 6:23 下午          V1.0.0          通讯录功能测试
 */
package com.learning.qywx.test.contact;

import chat.qiye.wechat.sdk.api.contact.ContactDeptApi;
import chat.qiye.wechat.sdk.api.contact.vo.ContactDeptVo;
import chat.qiye.wechat.sdk.service.ApiConfigurationDefaultProvider;
import chat.qiye.wechat.sdk.service.ApiFactory;
import com.learning.qywx.test.BaseJunitTest;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.List;

/**
 * 功能简述
 * 〈通讯录功能测试〉
 *
 * @author claire
 * @date 2022/4/14 - 6:23 下午
 * @since 1.0.0
 */
public class ContactApiTest extends BaseJunitTest {
    @Resource
    private ContactDeptApi contactDeptApi;

    //使用SDK进行请求，需要配置properties文件，适用于所有java项目
    @Test
    public void testGetDeptFromSdk() {
        // 获取Api实例， 所有封装的Api 都在 chat.qiye.wechat.sdk.api 包下，并以Api结尾
        ContactDeptApi contactDeptApi = ApiFactory.getApiBean(ContactDeptApi.class, new ApiConfigurationDefaultProvider());
        // 查询 通讯录 部门列表
        List<ContactDeptVo> deptVoList = contactDeptApi.list(0).getDepartment();
        System.out.println("DeptList:" + deptVoList);
    }

    //使用springboot-starter进行请求，需要spring配置文件，适用于所有springboot-java项目
    @Test
    public void testGetDeptAuto(){
        List<ContactDeptVo> departments = contactDeptApi.list(0).getDepartment();
        System.out.println("DeptList:" + departments);
    }

}
