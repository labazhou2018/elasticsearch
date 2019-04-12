package com.elastic.exception;

import java.io.IOException;

/**
 * @Date: 2019/3/1 9:48
 **/
public class ESIoException extends IOException {

    public ESIoException(String messaget, Throwable throwable){
        super(messaget,throwable);
    }

    public ESIoException(String messaget){
        super(messaget);
    }

    public ESIoException(Throwable throwable){
        super(throwable);
    }
}
