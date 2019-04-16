package com.elastic.pool.espool;

import com.elastic.pool.config.TransportClientNodeConfig;
import com.elastic.pool.config.TransportClientPoolConfig;
import com.elastic.pool.factory.TransportClientFactory;
import org.elasticsearch.client.transport.TransportClient;

import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @Date: 2019/3/7 11:39
 **/

public class TransportClientPool extends Pool<TransportClient> {

	/**
	 * es集群名称
	 */
	TransportClientPoolConfig transportClientPoolConfig;

	/**
	 * es 集群对应的节点
	 */
	AtomicReference<Set<TransportClientNodeConfig>> nodeSetsReference;


	public TransportClientPool(AtomicReference<Set<TransportClientNodeConfig>> nodesReference, TransportClientPoolConfig transportClientPoolConfig) {
		super(transportClientPoolConfig, new TransportClientFactory(nodesReference, transportClientPoolConfig));
		this.transportClientPoolConfig = transportClientPoolConfig;
		this.nodeSetsReference = nodesReference;
	}

}
