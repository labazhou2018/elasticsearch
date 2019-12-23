package com.elastic.operations.admin.index;

import org.elasticsearch.action.admin.indices.settings.get.GetSettingsResponse;
import org.elasticsearch.common.settings.Settings;

/**
 * @Date: 2019/12/23 15:45
 **/
public interface IndexAdmin {

	/**
	 * Using an IndicesAdminClient, you can create an index with all default settings and no mapping
	 * @param indexName
	 */
	public void ctreateIndex(String indexName);

	/**
	 * Each index created can have specific settings associated with it.
	 * @param indexName
	 */
	public void indexSettings(String indexName, Settings settings);

	/**
	 * You can add mappings at index creation time
	 * @param indexName
	 */
	public void putMapping(String indexName,String type,String mappings);

	/**
	 * The refresh API allows to explicitly refresh one or more index
	 * @param indexNames
	 */
	public void refreshIndex(String ... indexNames);

	/**
	 * The get settings API allows to retrieve settings of index/indices
	 * @param indexNames
	 * @return
	 */
	public GetSettingsResponse getSettings(String indexNames);

	/**
	 * You can change index settings by calling
	 * @param indesName
	 */
	public void updateIndexSettings(String indesName);

	/**
	 * get indexNames's shards
	 * @param indesName
	 * @return
	 */
	public String getIndexShards(String indesName);
}
