package com.elastic.Utils;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @Date: 2019/12/20 17:26
 **/
public class JacksonHelper {

	private static ObjectMapper objectMapper;

	public static ObjectMapper getObjectMapper() {
		if (objectMapper == null) {
			synchronized (ObjectMapper.class) {
				if (null == objectMapper) {
					return new ObjectMapper();
				}
			}
		}
		return objectMapper;
	}
}
