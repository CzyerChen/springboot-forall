/**
 * Author:   claire
 * Date:    2022/3/30 - 11:27 上午
 * Description: 测试控制器
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2022/3/30 - 11:27 上午          V1.0.0          测试控制器
 */
package com.learning.minio.controller;

import com.learning.minio.model.Result;
import com.learning.minio.service.MinioService;
import io.minio.http.Method;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 功能简述
 * 〈测试控制器〉
 *
 * @author claire
 * @date 2022/3/30 - 11:27 上午
 * @since 1.0.0
 */
@RestController
public class TestController {
    @Autowired
    private MinioService minioService;

    @PostMapping("/upload")
    public Result upload(@RequestParam("file") MultipartFile file, @RequestParam("fileName") String fileName) {
        minioService.upload(file, fileName);
        String url = minioService.getUrl(fileName, 7, TimeUnit.DAYS);
        return Result.success(url);
    }

    @GetMapping("/policy")
    public Result policy(@RequestParam("fileName") String fileName) {
        Map policy = minioService.getPolicy(fileName, ZonedDateTime.now().plusMinutes(10));
        return Result.success(policy);
    }

    @GetMapping("/uploadUrl")
    public Result uploadUrl(@RequestParam("fileName") String fileName) {
        String url = minioService.getPolicyUrl(fileName, Method.PUT, 2, TimeUnit.MINUTES);
        return Result.success(url);
    }

    @GetMapping("/url")
    public Result getUrl(@RequestParam("fileName") String fileName) {
        String url = minioService.getUrl(fileName, 7, TimeUnit.DAYS);
        return Result.success(url);
    }

}
