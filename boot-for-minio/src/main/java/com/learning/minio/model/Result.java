/**
 * Author:   claire
 * Date:    2022/3/30 - 11:28 上午
 * Description: 结果类
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2022/3/30 - 11:28 上午          V1.0.0          结果嘞
 */
package com.learning.minio.model;

import lombok.Data;

/**
 * 功能简述 
 * 〈结果类〉
 *
 * @author claire
 * @date 2022/3/30 - 11:28 上午
 * @since 1.0.0
 */
@Data
public class Result {
    private int code;

    private String message;

    private Object data;

    private Result() {
    }

    private Result(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static Result success() {
        return success("操作成功");
    }


    public static Result success(String message,Object data) {
        return new Result(200,message,data);
    }
    public static Result success(Object data) {
        return new Result(200,"操作成功",data);
    }

    public static Result error() {
        return new Result(500,"操作失败",null);
    }
    public static Result error(String message) {
        return new Result(500,message,null);
    }
}
