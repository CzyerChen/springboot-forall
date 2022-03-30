/**
 * Author:   claire
 * Date:    2022/3/30 - 2:29 下午
 * Description: 异常处理类
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2022/3/30 - 2:29 下午          V1.0.0          异常处理类
 */
package com.learning.minio.exception;

import com.learning.minio.model.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 功能简述 
 * 〈异常处理类〉
 *
 * @author claire
 * @date 2022/3/30 - 2:29 下午
 * @since 1.0.0
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Result handle(Exception e){
        return Result.error(e.getMessage());
    }
}
