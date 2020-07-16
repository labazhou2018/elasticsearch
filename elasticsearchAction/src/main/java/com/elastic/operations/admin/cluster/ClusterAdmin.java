package com.elastic.operations.admin.cluster;

import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;

/**
 * @Date: 2019/12/23 15:45
 **/
public interface ClusterAdmin {

	/**
	 * The cluster health API allows to get a very simple status on the health of
	 * the cluster and also can give you some technical information about the cluster status per index
	 *
	 * @return
	 */
	public ClusterHealthResponse getClusterStatus();


	/**
	 * You can use the cluster health API to wait for a specific status
	 * for the whole cluster or for a given index
	 */
	public void waitForStatus();
}
