package com.elastic.exception;

/**
 * @Author: wang luqi
 * @Date: 2019/3/7 11:46
 **/
public class ElasticSearchException extends RuntimeException {

	public ElasticSearchException(String messaget,Throwable throwable){
		super(messaget,throwable);
	}

	public ElasticSearchException(String messaget){
		super(messaget);
	}

	public ElasticSearchException(Throwable throwable){
		super(throwable);
	}

}
