package com.elastic.operations.admin.cluster;

import com.elastic.operations.admin.PublicAdmin;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;

/**
 * @Date: 2019/12/23 15:56
 **/
public class ClusterAdminImpl extends PublicAdmin implements ClusterAdmin {

	/**
	 * The cluster health API allows to get a very simple status on the health of
	 * the cluster and also can give you some technical information about the cluster status per index
	 *
	 * @return
	 */
	@Override
	public ClusterHealthResponse getClusterStatus() {
		return this.clusterAdminClient.prepareHealth().get();
		/*
		String clusterName = healths.getClusterName();
		int numberOfDataNodes = healths.getNumberOfDataNodes();
		int numberOfNodes = healths.getNumberOfNodes();

		for (ClusterIndexHealth health : healths.getIndices().values()) {
			String index = health.getIndex();
			int numberOfShards = health.getNumberOfShards();
			int numberOfReplicas = health.getNumberOfReplicas();
			ClusterHealthStatus status = health.getStatus();
		}
		*/
	}

	/**
	 * You can use the cluster health API to wait for a specific status
	 * for the whole cluster or for a given index
	 */
	@Override
	public void waitForStatus() {
		this.clusterAdminClient.prepareHealth()
				.setWaitForGreenStatus()
				.get();
		/*
		ClusterHealthStatus status = response.getIndices().get("company").getStatus();
		if (!status.equals(ClusterHealthStatus.GREEN)) {
			throw new RuntimeException("Index is in " + status + " state");
		}
		*/
	}
}
