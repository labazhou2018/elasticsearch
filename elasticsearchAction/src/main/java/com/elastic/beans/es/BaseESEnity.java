package com.elastic.beans.es;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import static org.springframework.data.elasticsearch.annotations.FieldType.Text;

/**
 * 用户实体对应的ES库实体
 * @Date: 2019/12/19 15:25
 **/
@Document(indexName = "test-index-uuid-keyed", type = "test-type-uuid-keyed",
		shards = 1, replicas = 0, refreshInterval = "-1")
public class BaseESEnity {

	@Id
	private String id;

	@Field(type = Text, store = true, fielddata = true)
	private String type;

	@Field(type = Text, store = true, fielddata = true)
	private String message;

	@Version
	private Long version;
}
