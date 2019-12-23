package com.elastic.operations.admin;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.ClusterAdminClient;
import org.elasticsearch.client.IndicesAdminClient;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Date: 2019/12/23 16:16
 **/
public class PublicAdmin {

	@Autowired
	Client client;

	public IndicesAdminClient indicesAdminClient = client.admin().indices();

	public ClusterAdminClient clusterAdminClient = client.admin().cluster();

}
