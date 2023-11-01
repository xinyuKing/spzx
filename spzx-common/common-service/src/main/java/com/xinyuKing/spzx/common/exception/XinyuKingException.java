package com.xinyuKing.spzx.common.exception;

import com.xinyuKing.spzx.model.vo.common.ResultCodeEnum;
import lombok.Data;

/*自定义异常*/
@Data
public class XinyuKingException extends RuntimeException{
    private Integer code;
    private String message;
    private ResultCodeEnum resultCodeEnum;

    public XinyuKingException(ResultCodeEnum resultCodeEnum){
        this.resultCodeEnum=resultCodeEnum;
        this.code=resultCodeEnum.getCode();
        this.message=resultCodeEnum.getMessage();
    }
}
