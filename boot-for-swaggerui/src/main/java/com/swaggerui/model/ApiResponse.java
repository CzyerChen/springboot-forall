package com.swaggerui.model;

import java.io.Serializable;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -03 - 25 17:27
 */
public class ApiResponse implements Serializable {
    public Long code;
    public String responseMessage;
    public Object data;

    public static class Builder {
        private Long code;
        private String responseMessage;
        private Object data;

        public ApiResponse.Builder code(Long code){
            this.code = code;
            return this;
        }
        public ApiResponse.Builder responseMessage(String responseMessage){
            this.responseMessage = responseMessage;
            return  this;
        }
        public ApiResponse.Builder data(Object data){
            this.data = data;
            return  this;
        }

        public ApiResponse build() {
            return new ApiResponse(this.code, this.responseMessage, this.data);
        }

    }

    public ApiResponse(Long code, String responseMessage, Object data) {
        this.code = code;
        this.responseMessage = responseMessage;
        this.data = data;
    }

    public static ApiResponse.Builder builder() {
        return new ApiResponse.Builder();
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Long getCode() {
        return code;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public Object getData() {
        return data;
    }
}
