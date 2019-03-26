package com.swaggerui.model;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -03 - 26 13:09
 */
public class UserNotFoundException extends ApiException {

    public UserNotFoundException(String message){
        super(UserErrorCode.UserNotFoundErrorCode.longValue(),message,null);
    }
}
