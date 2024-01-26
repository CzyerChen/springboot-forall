/**
 * Author:   claire
 * Date:    2023/11/23 - 8:10 下午
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2023/11/23 - 8:10 下午          V1.0.0
 */
package com.learning.bootforfeign.demos.web;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author claire
 * @date 2023/11/23 - 8:10 下午
 * @since 1.0.0
 */
@FeignClient(
        name = "demoservice",
        url = "http://localhost:8088"
)
public interface FeignService {

    @RequestMapping(value = "/hello",method = RequestMethod.GET)
    public String sayRemoteHello(@RequestParam("name")String name);
}

