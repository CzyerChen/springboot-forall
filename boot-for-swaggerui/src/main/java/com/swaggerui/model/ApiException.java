package com.swaggerui.model;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -03 - 26 13:04
 */
public class ApiException  extends RuntimeException {
    private Long code;
    private Object data;

    public ApiException(Long code,String message,Object data,Throwable e){
        super(message,e);//RuntimeException会指出对应的提示信息和异常栈
        this.code = code;
        this.data = data;
    }
    public ApiException(Long code,String message,Object data){
        this(code,message,data,null);
    }

    public ApiException(Long code,String message){
        this(code,message,null,null);
    }

    public ApiException(String message,Throwable e){
        this(null,message,null,e);
    }
    public ApiException(Throwable e){
        super(e);
    }

    public ApiException(){

    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
