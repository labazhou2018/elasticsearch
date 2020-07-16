package com.elastic.beans.es;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.Mapping;

import static org.springframework.data.elasticsearch.annotations.FieldType.Text;

/**
 * 学生实体对应的ES实体
 *
 * @Date: 2019/12/19 17:28
 **/
@Document(indexName = "test-student-index", type = "test-student-index",
		shards = 1, replicas = 0, refreshInterval = "-1")
@Mapping(mappingPath = "/mappings/test-student-mappings.json")
public class StudentESEnity extends BaseESEnity {

	private String name;

	@Field(type = Text, store = true, fielddata = true)
	private String message;

	private String email;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
