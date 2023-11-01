/**
 * Author:   claire
 * Date:    2023/8/14 - 6:07 下午
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2023/8/14 - 6:07 下午          V1.0.0
 */
package com.learning.k8sapp.controller;

import com.learning.k8sapp.runner.Constants;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 *
 * @author claire
 * @date 2023/8/14 - 6:07 下午
 * @since 1.0.0
 */
@RestController
@RequestMapping("cache")
public class CacheController {

    @PutMapping("/dynamic")
    public String updateValue(@RequestParam String value) {
        Constants.dynamicValue = value;
        System.out.println("controller cache update : " + value);
        return value;
    }
}
