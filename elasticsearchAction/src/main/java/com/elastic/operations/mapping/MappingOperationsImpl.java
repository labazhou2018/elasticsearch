package com.elastic.operations.mapping;

import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.springframework.data.elasticsearch.ElasticsearchException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * @Date: 2019/12/20 15:18
 **/
@Repository
public class MappingOperationsImpl implements MappingOperations {

	private Client client;

	@Override
	public boolean putMapping(String indexName, String type, Object mapping) {
		Assert.notNull(indexName, "No index defined for putMapping()");
		Assert.notNull(type, "No type defined for putMapping()");
		PutMappingRequestBuilder requestBuilder = client.admin().indices().preparePutMapping(indexName).setType(type);
		if (mapping instanceof String) {
			requestBuilder.setSource(String.valueOf(mapping));
		} else if (mapping instanceof Map) {
			requestBuilder.setSource((Map) mapping);
		} else if (mapping instanceof XContentBuilder) {
			requestBuilder.setSource((XContentBuilder) mapping);
		}
		return requestBuilder.execute().actionGet().isAcknowledged();
	}

	@Override
	public Map getMapping(String indexName, String type) {
		Assert.notNull(indexName, "No index defined for putMapping()");
		Assert.notNull(type, "No type defined for putMapping()");
		Map mappings = null;
		try {
			mappings = client.admin()
					.indices()
					.getMappings(new GetMappingsRequest().indices(indexName).types(type))
					.actionGet()
					.getMappings()
					.get(indexName)
					.get(type)
					.getSourceAsMap();
		} catch (Exception e) {
			throw new ElasticsearchException(
					"Error while getting mapping for indexName : " + indexName + " type : " + type + " " + e.getMessage());
		}
		return mappings;
	}
}
