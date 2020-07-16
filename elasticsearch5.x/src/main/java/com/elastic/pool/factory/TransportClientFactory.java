package com.elastic.pool.factory;

import com.elastic.exception.ElasticSearchHostException;
import com.elastic.pool.config.TransportClientNodeConfig;
import com.elastic.pool.config.TransportClientPoolConfig;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.http.HttpHost;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @Date: 2019/3/7 11:41
 **/
public class TransportClientFactory implements PooledObjectFactory<TransportClient> {

	/**
	 * es集群节点设置
	 */
	private AtomicReference<Set<TransportClientNodeConfig>> nodesReference = new AtomicReference<Set<TransportClientNodeConfig>>();

	/**
	 * es 集群连接池配置
	 */
	private TransportClientPoolConfig transportClientPoolConfig;

	public TransportClientFactory(AtomicReference<Set<TransportClientNodeConfig>> nodesReference, TransportClientPoolConfig transportClientPoolConfig) {
		this.nodesReference = nodesReference;
		this.transportClientPoolConfig = transportClientPoolConfig;
	}

	public PooledObject<TransportClient> makeObject() throws ElasticSearchHostException {
		HttpHost[] nodes = new HttpHost[nodesReference.get().size()];

		Settings settings = Settings.builder()
				/*设置ES实例的名称*/
				.put("cluster.name", TransportClientPoolConfig.getClusterName())
				/*自动嗅探整个集群的状态，把集群中其他ES节点的ip添加到本地的客户端列表中*/
				.put("client.transport.sniff", TransportClientPoolConfig.getClusterClientTransportsniff())
				.build();

		TransportClient transportClient = new PreBuiltTransportClient(settings);
		for (TransportClientNodeConfig each : nodesReference.get()) {
			try {
				transportClient.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(each.getHost()), each.getPort()));
			} catch (UnknownHostException e) {
				throw new ElasticSearchHostException("host exception ");
			}
		}

		return new DefaultPooledObject(transportClient);
	}

	public void destroyObject(PooledObject<TransportClient> pooledObject) throws Exception {
		TransportClient client = pooledObject.getObject();
		if (client != null) {
			try {
				client.close();
			} catch (Exception e) {
				//ignore
			}
		}
	}

	public boolean validateObject(PooledObject<TransportClient> pooledObject) {
		TransportClient client = pooledObject.getObject();
		try {
			client.connectedNodes();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public void activateObject(PooledObject<TransportClient> pooledObject) {
		TransportClient client = pooledObject.getObject();
		client.connectedNodes();
	}

	public void passivateObject(PooledObject<TransportClient> pooledObject) throws Exception {
		//nothing
	}


}
