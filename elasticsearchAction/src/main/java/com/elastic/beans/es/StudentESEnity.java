package com.elastic.beans.es;

import org.springframework.data.elasticsearch.annotations.Field;

import static org.springframework.data.elasticsearch.annotations.FieldType.Text;

/**
   学生实体对应的ES实体
 * @Date: 2019/12/19 17:28
 **/
public class StudentESEnity extends BaseESEnity{

	private String name;

	@Field(type = Text, store = true, fielddata = true)
	private String message;
}
