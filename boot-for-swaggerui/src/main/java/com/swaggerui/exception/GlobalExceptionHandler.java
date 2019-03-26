package com.swaggerui.exception;

import com.swaggerui.model.ApiException;
import com.swaggerui.model.ApiResponse;
import com.swaggerui.model.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * Desciption
 *
 * @author Claire.Chen
 * @create_time 2019 -03 - 26 13:37
 */
@RestControllerAdvice
/*@ControllerAdvice(annotations = RestController.class)
@ResponseBody*/
public class GlobalExceptionHandler {
    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(value = {ApiException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse bussinessExeption(ApiException e, HttpServletResponse httpServletResponse) {
        ApiResponse.Builder response = new ApiResponse.Builder();
        if (e instanceof ApiException) {
            response.code(((ApiException) e).getCode()).responseMessage(e.getMessage()).data(((ApiException) e).getData());
        }
        return response.build();
    }

    @ExceptionHandler(value = {Exception.class, RuntimeException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse commonExcption(Exception e) {
        ApiResponse.Builder response = new ApiResponse.Builder();
        return response.code(Long.valueOf(getLocaleMessage("api.internel.server.error.code"))).responseMessage(getLocaleMessage("api.internel.server.error.message")).build();
    }



    private String getLocaleMessage(String code, Object... objects) {
        return messageSource.getMessage(code, objects, LocaleContextHolder.getLocale());
    }


}
