package com.elastic.transprotclient;

import com.elastic.utils.ConfigUtils;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @Date: 2019/2/28 14:19
 * <p>
 * 1.Client内部已经实现连接池，不需要手动close
 * 2.杜绝运行一段时间内不造成资源耗尽而无法创建连接，对于频繁连接集群时，最好使用单例模式
 **/
public class TransportClientBuild {

	private static final String CLUSTER_NAME = ConfigUtils.getEsClusterName();
	private static final String CLUSTER_CLIENT_TRANSPORTSNIFF = ConfigUtils.getClientTransportSniff();
	private static final String CLUSTER_HOSTNAME_PORT = ConfigUtils.getEsClusterDiscoverHostName();

	private static Client client;

	public static Client getClient() {
		if (client == null) {
			String [] hostNamesPort = CLUSTER_HOSTNAME_PORT.split(",");

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


			client = transportClient;
		}
		return client;
	}

	public static void main(String[] args) {
		/*client 测试*/
		System.out.println("获取client 对象地址：" + getClient().toString());
	}

}
