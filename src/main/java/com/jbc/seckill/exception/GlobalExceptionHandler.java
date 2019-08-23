package com.jbc.seckill.exception;

import com.jbc.seckill.result.CodeMsg;
import com.jbc.seckill.result.Result;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public Result<CodeMsg> exceptionHandler(HttpServletRequest request, Exception e){
        if(e instanceof GlobalException){
            GlobalException ex = (GlobalException)e;
            return Result.error(ex.getCm());
        }
        if(e instanceof BindException){//参数校验异常
            BindException ex = (BindException)e;
            List<ObjectError> errors = ex.getAllErrors();
            ObjectError error0 = errors.get(0);
            String msg = error0.getDefaultMessage();
            return Result.error(CodeMsg.BIND_ERROR.fillArgs(msg));

        }
        return Result.error(CodeMsg.SERVER_ERROR);
    }
}
