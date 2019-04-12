package com.elastic.api;

import com.carrotsearch.hppc.cursors.ObjectObjectCursor;
import com.elastic.transprotclient.TransportClientBuild;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.action.admin.indices.settings.get.GetSettingsResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.ClusterAdminClient;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.cluster.health.ClusterHealthStatus;
import org.elasticsearch.cluster.health.ClusterIndexHealth;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;

/**
 * @Date: 2019/3/25 11:14
 **/
public class JavaAdmin {

	Client client = TransportClientBuild.getClient();

	/**
	 * 6.1 Indices Administration
	 */
	public void indexAdmin() {
		IndicesAdminClient indicesAdminClient = client.admin().indices();

		/*1.创建索引*/
		indicesAdminClient.prepareCreate("twitter").get();

		/*2.索引设置*/
		indicesAdminClient.prepareCreate("twitter")
				.setSettings(Settings.builder()
						.put("index.number_of_shards", 3)
						.put("index.number_of_replicas", 2)
				)
				.get();

		/*3.设置mapping*/
		indicesAdminClient.prepareCreate("twitter")
				.addMapping("tweet", "{\n" +
						"    \"tweet\": {\n" +
						"      \"properties\": {\n" +
						"        \"message\": {\n" +
						"          \"type\": \"string\"\n" +
						"        }\n" +
						"      }\n" +
						"    }\n" +
						"  }")
				.get();

		/*已经存在的索引设置mapping*/
		indicesAdminClient.preparePutMapping("twitter")
				.setType("user")
				.setSource("{\n" +
						"  \"properties\": {\n" +
						"    \"user_name\": {\n" +
						"      \"type\": \"string\"\n" +
						"    }\n" +
						"  }\n" +
						"}")
				.get();

		/*4.refresh*/
		/*更新所有索引*/
		indicesAdminClient.prepareRefresh().get();
		/*更新单个索引*/
		indicesAdminClient
				.prepareRefresh("twitter")
				.get();
		/*更新多个索引*/
		indicesAdminClient
				.prepareRefresh("twitter", "company")
				.get();

		/*5.Get Settings*/
		GetSettingsResponse response = client.admin().indices()
				.prepareGetSettings("company", "employee").get();
		for (ObjectObjectCursor<String, Settings> cursor : response.getIndexToSettings()) {
			String index = cursor.key;
			Settings settings = cursor.value;
			Integer shards = settings.getAsInt("index.number_of_shards", null);
			Integer replicas = settings.getAsInt("index.number_of_replicas", null);
		}

		/*6.Update Indices Settings*/
		indicesAdminClient.prepareUpdateSettings("twitter")
				.setSettings(Settings.builder()
						.put("index.number_of_replicas", 0)
				)
				.get();
	}


	/**
	 * 6.2 Cluster Administration
	 */
	public void clusterAdmin() {
		ClusterAdminClient clusterAdminClient = client.admin().cluster();

		/*1.集群状态*/

		/*所有索引信息*/
		ClusterHealthResponse healths = clusterAdminClient.prepareHealth().get();
		String clusterName = healths.getClusterName();
		int numberOfDataNodes = healths.getNumberOfDataNodes();
		int numberOfNodes = healths.getNumberOfNodes();


		/*迭代查看索引信息*/
		for (ClusterIndexHealth health : healths.getIndices().values()) {
			String index = health.getIndex();
			int numberOfShards = health.getNumberOfShards();
			int numberOfReplicas = health.getNumberOfReplicas();
			/*索引信息*/
			ClusterHealthStatus status = health.getStatus();
		}

		/*2.Wait for status*/
		clusterAdminClient.prepareHealth()
				.setWaitForYellowStatus()
				.get();

		clusterAdminClient.prepareHealth("company")
				.setWaitForGreenStatus()
				.get();

		clusterAdminClient.prepareHealth("employee")
				.setWaitForGreenStatus()
				.setTimeout(TimeValue.timeValueSeconds(2))
				.get();


		/*3.异常处理*/
		ClusterHealthResponse response = clusterAdminClient.prepareHealth("company")
				.setWaitForGreenStatus()
				.get();

		ClusterHealthStatus status = response.getIndices().get("company").getStatus();
		if (!status.equals(ClusterHealthStatus.GREEN)) {
			throw new RuntimeException("Index is in " + status + " state");
		}
	}

}
