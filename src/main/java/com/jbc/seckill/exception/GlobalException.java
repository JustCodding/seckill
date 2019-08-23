package com.jbc.seckill.exception;

import com.jbc.seckill.result.CodeMsg;

public class GlobalException extends RuntimeException {
    private CodeMsg cm;
    public GlobalException(){

    }
    public GlobalException(String msg){
        super(msg);
    }
    public GlobalException(CodeMsg cm){
        this.cm = cm;
    }

    public CodeMsg getCm() {
        return cm;
    }
}
