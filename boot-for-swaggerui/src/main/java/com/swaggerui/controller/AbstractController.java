package com.swaggerui.controller;

import com.swaggerui.model.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -03 - 25 18:27
 */

public abstract class AbstractController {
    @Autowired
    private MessageSource messageSource;

    public String getLocaleMessage(String code,Object... objects){
        return messageSource.getMessage(code,objects, LocaleContextHolder.getLocale());
    }

    public ApiResponse getResponse(String code,String message,Object data){
        return ApiResponse.builder()
                .code(Long.valueOf(getLocaleMessage(code)))
                .responseMessage(getLocaleMessage(message))
                .data(data).build();
    }
}

