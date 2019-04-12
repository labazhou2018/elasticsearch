package com.elastic.exception;


/**
 * @Date: 2019/3/1 9:48
 **/
public class ESParamException extends RuntimeException{

    public ESParamException(String messaget,Throwable throwable){
        super(messaget,throwable);
    }

    public ESParamException(String messaget){
        super(messaget);
    }

    public ESParamException(Throwable throwable){
        super(throwable);
    }
}
