package com.elastic.beans.es;

import org.springframework.data.elasticsearch.annotations.Field;

import static org.springframework.data.elasticsearch.annotations.FieldType.Text;

/**
 * 用户实体对应的ES实体
 *
 * @Date: 2019/12/19 17:22
 **/
public class UserESEnity extends BaseESEnity {

	private String name;

	@Field(type = Text, store = true, fielddata = true)
	private String type;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
