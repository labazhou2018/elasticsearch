package com.elastic.springdataclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @Date: 2019/12/25 17:25
 **/
@Configuration("classpath:elasticsearch-multi-client.properties")
public class SpringMultiClientConfig {

	@Value("${base.elastic.cluster.name}")
	private static String BASE_CLUSTER_NAME;

	@Value("${base.elastic.cluster.clientTransportSniff}")
	private static String BASE_CLUSTER_CLIENT_TRANSPORTSNIFF;

	@Value("${base.elastic.cluster.discover.hostname}")
	private static String BASE_CLUSTER_HOSTNAME_PORT;


	@Value("${local.elastic.cluster.name}")
	private static String LOCAL_CLUSTER_NAME;

	@Value("${local.elastic.cluster.clientTransportSniff}")
	private static String LOCAL_CLUSTER_CLIENT_TRANSPORTSNIFF;

	@Value("${local.elastic.cluster.discover.hostname}")
	private static String LOCAL_CLUSTER_HOSTNAME_PORT;

	public static String getBaseClusterName() {
		return BASE_CLUSTER_NAME;
	}

	public static void setBaseClusterName(String baseClusterName) {
		BASE_CLUSTER_NAME = baseClusterName;
	}

	public static String getBaseClusterClientTransportsniff() {
		return BASE_CLUSTER_CLIENT_TRANSPORTSNIFF;
	}

	public static void setBaseClusterClientTransportsniff(String baseClusterClientTransportsniff) {
		BASE_CLUSTER_CLIENT_TRANSPORTSNIFF = baseClusterClientTransportsniff;
	}

	public static String getBaseClusterHostnamePort() {
		return BASE_CLUSTER_HOSTNAME_PORT;
	}

	public static void setBaseClusterHostnamePort(String baseClusterHostnamePort) {
		BASE_CLUSTER_HOSTNAME_PORT = baseClusterHostnamePort;
	}

	public static String getLocalClusterName() {
		return LOCAL_CLUSTER_NAME;
	}

	public static void setLocalClusterName(String localClusterName) {
		LOCAL_CLUSTER_NAME = localClusterName;
	}

	public static String getLocalClusterClientTransportsniff() {
		return LOCAL_CLUSTER_CLIENT_TRANSPORTSNIFF;
	}

	public static void setLocalClusterClientTransportsniff(String localClusterClientTransportsniff) {
		LOCAL_CLUSTER_CLIENT_TRANSPORTSNIFF = localClusterClientTransportsniff;
	}

	public static String getLocalClusterHostnamePort() {
		return LOCAL_CLUSTER_HOSTNAME_PORT;
	}

	public static void setLocalClusterHostnamePort(String localClusterHostnamePort) {
		LOCAL_CLUSTER_HOSTNAME_PORT = localClusterHostnamePort;
	}
}
