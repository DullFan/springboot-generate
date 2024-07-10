package com.dullfan.generate.utils.extremely;

import com.dullfan.generate.utils.Result;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 处理自定义的业务异常
     */
    @ExceptionHandler(value = ServiceException.class)
    @ResponseBody
    public Result bizExceptionHandler(HttpServletRequest req, ServiceException e){
        e.printStackTrace();
        log.error("发生业务异常！原因是：{}",e.getErrorMsg());
        return Result.error(e.getErrorMsg());
    }

    /**
     * 处理其他异常
     */
    @ExceptionHandler(value =Exception.class)
    @ResponseBody
    public Result exceptionHandler(HttpServletRequest req, Exception e){
        log.error("未知异常！原因是:",e);
        return Result.error("未知异常！原因是:"+e.getMessage());
    }
}