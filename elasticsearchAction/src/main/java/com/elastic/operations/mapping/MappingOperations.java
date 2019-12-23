package com.elastic.operations.mapping;

import java.util.Map;

/**
 * @Date: 2019/12/20 15:17
 **/
public interface MappingOperations {

	/**
	 * Create mapping for a given indexName and type
	 *
	 * @param indexName
	 * @param type
	 * @param mappings
	 */
	boolean putMapping(String indexName, String type, Object mappings);

	/**
	 * Get mapping for a given indexName and type
	 *
	 * @param indexName
	 * @param type
	 */
	Map getMapping(String indexName, String type);

}
