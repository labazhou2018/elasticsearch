package com.elastic.beans.es;

import org.springframework.data.elasticsearch.annotations.Field;

import static org.springframework.data.elasticsearch.annotations.FieldType.Text;

/**
 * @Author: wang luqi
 * @Date: 2019/12/19 17:22
 **/
public class UserESEnity extends BaseESEnity {

	@Field(type = Text, store = true, fielddata = true)
	private String type;

	@Field(type = Text, store = true, fielddata = true)
	private String message;
}
