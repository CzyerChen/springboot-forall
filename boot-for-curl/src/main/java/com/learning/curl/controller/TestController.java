/**
 * Author:   claire
 * Date:    2021-05-08 - 00:07
 * Description: 测试类
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2021-05-08 - 00:07          V1.0.0          测试类
 */
package com.learning.curl.controller;

import cn.hutool.extra.servlet.ServletUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 功能简述 
 * 〈测试类〉
 *
 * @author claire
 * @date 2021-05-08 - 00:07
 * @since 1.0.0
 */
@RestController
@RequestMapping("v3")
public class TestController {


    @GetMapping
    @RequestMapping(path = {"smsblackBatch"}, consumes = {"multipart/form-data"}, produces = {"text/plain;charset=utf-8"})
    public String testUploadFile(HttpServletRequest request, HttpServletResponse response, MultipartFile file){
        Map<String, String[]> parameterMap = request.getParameterMap();
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        MultipartFile file1 = fileMap.get("file");
        
        if (file != null){
            return "success";
        }
        return "fail";
    }

    @GetMapping("ip")
    public String testGetIp(HttpServletRequest request){
        return ServletUtil.getClientIP(request);
    }
}
