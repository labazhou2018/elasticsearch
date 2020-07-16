package com.elastic.beans.domain;

import org.springframework.data.elasticsearch.annotations.Field;

import java.io.Serializable;

import static org.springframework.data.elasticsearch.annotations.FieldType.Text;

/**
 * 学生实体
 *
 * @Date: 2019/12/19 17:28
 **/
public class Student implements Serializable {

	private String name;

	@Field(type = Text, store = true, fielddata = true)
	private String message;
}
