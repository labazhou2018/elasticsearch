package com.elastic.exception;

import java.net.UnknownHostException;

/**
 * @Date: 2019/3/7 11:45
 **/
public class ElasticSearchHostException extends UnknownHostException {

	public ElasticSearchHostException(String messaget) {
		super(messaget);
	}

}
