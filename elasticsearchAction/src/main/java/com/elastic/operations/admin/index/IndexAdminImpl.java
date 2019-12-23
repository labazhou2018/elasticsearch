package com.elastic.operations.admin.index;

import com.elastic.operations.admin.PublicAdmin;
import org.elasticsearch.action.admin.indices.settings.get.GetSettingsResponse;
import org.elasticsearch.common.settings.Settings;

/**
 *
 * @Date: 2019/12/23 15:56
 **/
public class IndexAdminImpl extends PublicAdmin implements IndexAdmin {

	/**
	 * Using an IndicesAdminClient, you can create an index with all default settings and no mapping
	 * @param indexName
	 */
	@Override
	public void ctreateIndex(String indexName) {
		this.indicesAdminClient.prepareCreate(indexName);
	}

	/**
	 * Each index created can have specific settings associated with it.
	 *
	 * @param indexName
	 */
	@Override
	public void indexSettings(String indexName, Settings settings) {
		this.indicesAdminClient.prepareCreate(indexName)
			.setSettings(settings)
			.get();
	}

	/**
	 * You can add mappings at index creation time
	 *
	 * @param indexName
	 */
	@Override
	public void putMapping(String indexName,String type,String mappings) {
		this.indicesAdminClient.prepareCreate(indexName)
				.addMapping(type, mappings)
				.get();
	}

	/**
	 * The refresh API allows to explicitly refresh one or more index
	 *
	 * @param indexNames
	 */
	@Override
	public void refreshIndex(String... indexNames) {
		this.indicesAdminClient.prepareRefresh(indexNames).get();
	}

	/**
	 * The get settings API allows to retrieve settings of index/indices
	 *
	 * @param indexNames
	 * @return
	 */
	@Override
	public GetSettingsResponse getSettings(String indexNames) {
		GetSettingsResponse response = this.indicesAdminClient
				.prepareGetSettings(indexNames)
				.get();
		return response;
		/*
		for (ObjectObjectCursor<String, Settings> cursor : response.getIndexToSettings()) {
			String index = cursor.key;
			Settings settings = cursor.value;
			Integer shards = settings.getAsInt("index.number_of_shards", null);
			Integer replicas = settings.getAsInt("index.number_of_replicas", null);
		}
		*/
	}

	/**
	 * You can change index settings by calling
	 *
	 * @param indesName
	 */
	@Override
	public void updateIndexSettings(String indesName) {
		this.indicesAdminClient.prepareUpdateSettings(indesName);
	}

	/**
	 * get indexNames's shards
	 *
	 * @param indesName
	 * @return
	 */
	@Override
	public String getIndexShards(String indesName) {
		return indicesAdminClient.prepareGetSettings(indesName)
				.get()
				.getSetting(indesName,"index.number_of_shards");
	}
}
