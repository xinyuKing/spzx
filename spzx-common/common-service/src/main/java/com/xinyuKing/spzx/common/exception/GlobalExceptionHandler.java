package com.xinyuKing.spzx.common.exception;

import com.xinyuKing.spzx.model.vo.common.Result;
import com.xinyuKing.spzx.model.vo.common.ResultCodeEnum;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {


    /*全局异常处理*/
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(){
        return Result.build(null,ResultCodeEnum.SYSTEM_ERROR);
    }

    /*自定义异常处理*/
    @ExceptionHandler(value=XinyuKingException.class)
    @ResponseBody
    public Result error(XinyuKingException e){
        return Result.build(null,e.getResultCodeEnum());
    }
}
