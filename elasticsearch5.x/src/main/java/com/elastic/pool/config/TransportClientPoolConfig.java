package com.elastic.pool.config;

import com.elastic.utils.ConfigUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.util.HashSet;
import java.util.Set;

/**
 * 集群属性配置
 *
 * @Date: 2019/3/7 11:40
 **/

public class TransportClientPoolConfig extends GenericObjectPoolConfig {

	/**
	 * 连接时间
	 */
	public static final String CLUSTER_NAME = ConfigUtils.getEsClusterName();
	/**
	 * 自动嗅探整个集群的状态，把集群中其他ES节点的ip添加到本地的客户端列表中
	 */
	public static final String CLUSTER_CLIENT_TRANSPORTSNIFF = ConfigUtils.getClientTransportSniff();

	/**
	 * 连接时间
	 */
	private static final String CONNECTTIMEMILLIS = ConfigUtils.getConnectTimeMillis();
	/**
	 * 连接池最大值
	 */
	private static final String MAXTOTAL = ConfigUtils.getMaxTotal();
	/**
	 * 最大空闲
	 */
	private static final String MAXIDLE = ConfigUtils.getMaxIdle();
	/**
	 * 最小空闲
	 */
	private static final String MINIDLE = ConfigUtils.getMinIdle();

	/**
	 * es 集群对应的节点
	 */
	Set<TransportClientNodeConfig> nodes = getNodesConfig();

	private static final String CLUSTER_HOSTNAME_PORT = ConfigUtils.getEsClusterDiscoverHostName();

	/**
	 * 读取配置文件，配置每一个节点
	 */
	public static Set<TransportClientNodeConfig> getNodesConfig() {
		Set<TransportClientNodeConfig> transportClientNodeConfigHashSet = new HashSet<TransportClientNodeConfig>();

		String[] hostNamesPort = CLUSTER_HOSTNAME_PORT.split(",");
		String[] temp;

		if (0 != hostNamesPort.length) {
			for (String each : hostNamesPort) {
				temp = each.split(":");
				TransportClientNodeConfig transportClientNodeConfig = new TransportClientNodeConfig();
				transportClientNodeConfig.setHost(temp[0].trim());
				transportClientNodeConfig.setPort(Integer.parseInt(temp[1].trim()));

				transportClientNodeConfigHashSet.add(transportClientNodeConfig);
			}
		}


		return transportClientNodeConfigHashSet;
	}


	public static String getClusterName() {
		return CLUSTER_NAME;
	}

	public static String getClusterClientTransportsniff() {
		return CLUSTER_CLIENT_TRANSPORTSNIFF;
	}

	public static String getCONNECTTIMEMILLIS() {
		return CONNECTTIMEMILLIS;
	}

	public static String getMAXTOTAL() {
		return MAXTOTAL;
	}

	public static String getMAXIDLE() {
		return MAXIDLE;
	}

	public static String getMINIDLE() {
		return MINIDLE;
	}

	public Set<TransportClientNodeConfig> getNodes() {
		return nodes;
	}

	public void setNodes(Set<TransportClientNodeConfig> nodes) {
		this.nodes = nodes;
	}
}