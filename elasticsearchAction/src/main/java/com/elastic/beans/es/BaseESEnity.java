package com.elastic.beans.es;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;

/**
 * 用户实体对应的ES库实体
 *
 * @Date: 2019/12/19 15:25
 **/
@Document(indexName = "test-index-uuid-keyed", type = "test-type-uuid-keyed",
		shards = 1, replicas = 0, refreshInterval = "-1")
public class BaseESEnity implements Serializable {

	@Id
	private String id;

	@Version
	private Long version;
}
