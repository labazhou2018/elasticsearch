package com.elastic.pool.config;

/**
 * 每一个节点对应的属性
 *
 * @Date: 2019/3/7 14:13
 **/
public class TransportClientNodeConfig {
	/**
	 * 节点名称
	 */
	private String nodeName;
	/**
	 * 节点Host
	 */
	private String host;
	/**
	 * 节点端口号
	 */
	private int port;

	public TransportClientNodeConfig() {
	}

	public TransportClientNodeConfig(String nodeName, String host, int port) {
		this.nodeName = nodeName;
		this.host = host;
		this.port = port;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
}
