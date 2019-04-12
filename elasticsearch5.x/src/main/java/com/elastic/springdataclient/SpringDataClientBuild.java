package com.elastic.springdataclient;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @Date: 2019/3/4 15:53
 * Spring boot -data elasticsearch 连接集群的方式
 *
 * 如果熟悉ES，不建议使用该方式，原因如下：
 *          集成版本的client和集群版本不，且集成版本包含其他第三方jar包和项目引用jar包容易冲突；
 *          集成版本的client API会被去掉一部分，不能完全体现ElasticSearch特性；
 **/
@Configuration("classpath:elasticsearch.properties")
public class SpringDataClientBuild {

	@Value("${elastic.cluster.name}")
	private String CLUSTER_NAME;

	@Value("${elastic.cluster.clientTransportSniff}")
	private String CLUSTER_CLIENT_TRANSPORTSNIFF;

	@Value("${elastic.cluster.discover.hostname}")
	private String CLUSTER_HOSTNAME_PORT;

	/**
	 * 在Spring中，bean可以被定义为两种模式：prototype（多例）和singleton（单例）
	 * singleton（单例）：只有一个共享的实例存在，所有对这个bean的请求都会返回这个唯一的实例。Spring bean 默认是单例模式.
	 * prototype（多例）：对这个bean的每次请求都会创建一个新的bean实例，类似于new。
	 */
	@Bean
	@Scope("prototype")
	public Client getClient() {

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

		if (0 != hostNamesPort.length){
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
