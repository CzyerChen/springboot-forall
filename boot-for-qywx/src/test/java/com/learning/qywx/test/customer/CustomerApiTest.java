/**
 * Author:   claire
 * Date:    2022/4/14 - 6:24 下午
 * Description: 客户API测试
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2022/4/14 - 6:24 下午          V1.0.0          客户API测试
 */
package com.learning.qywx.test.customer;

import chat.qiye.wechat.sdk.api.contact.ContactDeptApi;
import chat.qiye.wechat.sdk.api.contact.ContactUserApi;
import chat.qiye.wechat.sdk.api.contact.resp.ContactUserGetResp;
import chat.qiye.wechat.sdk.api.contact.resp.ContactUserListResp;
import chat.qiye.wechat.sdk.api.contact.vo.ContactDeptVo;
import chat.qiye.wechat.sdk.api.customer.CustomerApi;
import chat.qiye.wechat.sdk.api.customer.resp.CustomerGetResp;
import chat.qiye.wechat.sdk.api.customer.resp.CustomerListResp;
import chat.qiye.wechat.sdk.service.ApiConfigurationDefaultProvider;
import chat.qiye.wechat.sdk.service.ApiFactory;
import com.learning.qywx.test.BaseJunitTest;
import org.junit.Test;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * 功能简述 
 * 〈客户API测试〉
 *
 * @author claire
 * @date 2022/4/14 - 6:24 下午
 * @since 1.0.0
 */
public class CustomerApiTest extends BaseJunitTest {
    @Resource
    private ContactDeptApi contactDeptApi;
    @Resource
    private ContactUserApi contactUserApi;
    @Resource
    private CustomerApi customerApi;

    //使用SDK进行请求，需要配置qiye-wechat.properties文件，适用于所有java项目
    @Test
    public void testGetCustomerFromSdk(){
        // 获取Api实例， 所有封装的Api 都在 chat.qiye.wechat.sdk.api 包下，并以Api结尾
        ContactDeptApi contactDeptApi = ApiFactory.getApiBean(ContactDeptApi.class, new ApiConfigurationDefaultProvider());
        // 查询 通讯录 部门列表
        List<ContactDeptVo> deptVoList = contactDeptApi.list(0).getDepartment();
        System.out.println("DeptList:" + deptVoList);
        if(!CollectionUtils.isEmpty(deptVoList)) {
            ContactUserApi contactUserApi = ApiFactory.getApiBean(ContactUserApi.class, new ApiConfigurationDefaultProvider());
            Integer id = deptVoList.get(0).getId();
            ContactUserListResp userListResp = contactUserApi.list(id, 0);
            if(userListResp.success()){
                List<ContactUserGetResp> userlist = userListResp.getUserlist();
                if(!CollectionUtils.isEmpty(userlist)){
                    System.out.println("userList:" + userlist);
                    CustomerApi customerApi = ApiFactory.getApiBean(CustomerApi.class, new ApiConfigurationDefaultProvider());
                    CustomerListResp customerListResp = customerApi.list(userlist.get(1).getUserid());
                    if(customerListResp.success()){
                        List<String> externalUserids = customerListResp.getExternalUserid();
                        if(!CollectionUtils.isEmpty(externalUserids)){
                            CustomerGetResp customerGetResp = customerApi.get(externalUserids.get(0), null);
                            if(customerGetResp.success()){
                                List<String> remarkMobiles = customerGetResp.getFollowUser().get(0).getRemarkMobiles();
                                System.out.println("remarkMobiles:"+remarkMobiles.get(0));
                            }

                        }
                    }
                }
            }
        }
    }

    //使用springboot-starter进行请求，需要spring配置文件，适用于所有springboot-java项目
    @Test
    public void testGetCustomerAuto(){
        List<ContactDeptVo> departments = contactDeptApi.list(0).getDepartment();
        if(!CollectionUtils.isEmpty(departments)){
            ContactUserListResp list = contactUserApi.list(departments.get(0).getId(), 0);
            if(!CollectionUtils.isEmpty(list.getUserlist())){
                List<String> externalUserids = customerApi.list(list.getUserlist().get(1).getUserid()).getExternalUserid();
                List<CustomerGetResp.FollowUser> followUsers = customerApi.get(externalUserids.get(0), null).getFollowUser();
                if(!CollectionUtils.isEmpty(followUsers)){
                    List<String> remarkMobiles = followUsers.get(0).getRemarkMobiles();
                    System.out.println("remarkMobiles:"+remarkMobiles.get(0));
                }
            }
        }
    }
}
