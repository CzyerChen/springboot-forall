/**
 * Author:   claire
 * Date:    2020-12-28 - 21:48
 * Description:
 * History:
 * <author>          <time>                   <version>          <desc>
 * claire          2020-12-28 - 21:48          V1.13.0
 */
package com.statemachine.demo.exception;

/**
 * 功能简述 <br/> 
 * 〈〉
 *
 * @author claire
 * @date 2020-12-28 - 21:48
 */
public class BusinessException extends Throwable{
    private Integer code;
    private String message;

    public BusinessException(Throwable e){
        super(e);
    }
    public BusinessException(Integer code,String message,Throwable e){
        super(e);
        this.code =code;
        this.message = message;
    }

    public BusinessException(Integer code,String message){
        this.code =code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
