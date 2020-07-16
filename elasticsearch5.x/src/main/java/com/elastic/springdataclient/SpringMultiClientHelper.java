package com.elastic.springdataclient;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @Date: 2019/12/25 17:41
 **/
public class SpringMultiClientHelper {

	@Bean(name = "baseClient")
	@Primary
	@Scope("singleton")
	public Client getBaseClient() {
		return clientInit(SpringMultiClientConfig.getBaseClusterHostnamePort(),
				SpringMultiClientConfig.getBaseClusterName(),
				SpringMultiClientConfig.getBaseClusterClientTransportsniff());
	}

	@Bean(name = "localClient")
	@Scope("singleton")
	public Client getLocaClient() {
		return clientInit(SpringMultiClientConfig.getLocalClusterHostnamePort(),
				SpringMultiClientConfig.getLocalClusterName(),
				SpringMultiClientConfig.getLocalClusterClientTransportsniff());
	}

	public Client clientInit(String CLUSTER_HOSTNAME_PORT, String CLUSTER_NAME, String CLUSTER_CLIENT_TRANSPORTSNIFF) {
		String[] hostNamesPort = CLUSTER_HOSTNAME_PORT.split(",");

		Settings settings = Settings.builder()
				/*设置ES实例的名称*/
				.put("cluster.name", CLUSTER_NAME)
				/*自动嗅探整个集群的状态，把集群中其他ES节点的ip添加到本地的客户端列表中*/
				.put("client.transport.sniff", CLUSTER_CLIENT_TRANSPORTSNIFF)
				/*x-pack设置*/
				/*.put("xpack.security.transport.ssl.enabled", false)
				//x-pack用户密码 elastic:changme是默认的用户名:密码
				.put("xpack.security.user", "elastic:changme")*/
				.build();

		/*初始化client*/
		TransportClient transportClient = new PreBuiltTransportClient(settings);
		String host;
		int port;
		String[] temp;

		if (0 != hostNamesPort.length) {
			for (String hostPort : hostNamesPort) {
				try {
					temp = hostPort.split(":");
					host = temp[0].trim();
					port = Integer.parseInt(temp[1].trim());
					transportClient.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host), port));
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
			}
		}

		Client client = transportClient;
		return client;
	}
}
