package com.elastic.utils;


import com.elastic.exception.ESParamException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * @Date: 2019/2/28 15:39
 **/
public class ConfigUtils {

	/**
	 * 配置文件
	 */
	private static String esConfigFileName = "elasticsearch.properties";

	/**
	 * es集群名
	 */
	private static String esClusterName;
	/**
	 * es集群ip地址
	 */
	private static String esClusterDiscoverHostName;
	/**
	 * es集群是否加入嗅探功能
	 */
	private static String clientTransportSniff;

	/**
	 * 连接时间
	 */
	private static String connectTimeMillis;
	/**
	 * 连接池最大值
	 */
	private static String maxTotal;
	/**
	 * 最大空闲
	 */
	private static String maxIdle;
	/**
	 * 最小空闲
	 */
	private static String minIdle;

	private static Properties properties = new Properties();

	static {
		try {
			ClassLoader classLoader = ConfigUtils.class.getClassLoader();
			InputStream resourceAsStream = classLoader.getResourceAsStream(esConfigFileName);
			properties.load(resourceAsStream);
			init();
		} catch (ESParamException e) {
			throw new ESParamException("es cluster init parameter exception", e);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static void init() {

		esClusterName = properties.getProperty("elastic.cluster.name");
		esClusterDiscoverHostName = properties.getProperty("elastic.cluster.discover.hostname");
		clientTransportSniff = properties.getProperty("elastic.cluster.clientTransportSniff");
		connectTimeMillis = properties.getProperty("elastic.cluster.pool.connectTimeMillis");
		maxTotal = properties.getProperty("elastic.cluster.pool.maxTotal");
		maxIdle = properties.getProperty("elastic.cluster.pool.maxIdle");
		minIdle = properties.getProperty("elastic.cluster.pool.minIdle");

		if ("".equals(esClusterName) || "".equals(esClusterName) || "".equals(clientTransportSniff)) {
			throw new RuntimeException("elasticsearch 集群参数为空异常");
		}

		if ("".equals(connectTimeMillis) || "".equals(maxTotal) || "".equals(maxIdle) || "".equals(minIdle)) {
			throw new RuntimeException("elasticsearch 连接池参数为空异常");
		}
	}

	public static String getEsClusterName() {
		return esClusterName;
	}

	public static String getEsClusterDiscoverHostName() {
		return esClusterDiscoverHostName;
	}

	public static void setEsClusterDiscoverHostName(String esClusterDiscoverHostName) {
		ConfigUtils.esClusterDiscoverHostName = esClusterDiscoverHostName;
	}

	public static String getClientTransportSniff() {
		return clientTransportSniff;
	}

	public static void setClientTransportSniff(String clientTransportSniff) {
		ConfigUtils.clientTransportSniff = clientTransportSniff;
	}

	public static String getConnectTimeMillis() {
		return connectTimeMillis;
	}

	public static void setConnectTimeMillis(String connectTimeMillis) {
		ConfigUtils.connectTimeMillis = connectTimeMillis;
	}

	public static String getMaxTotal() {
		return maxTotal;
	}

	public static void setMaxTotal(String maxTotal) {
		ConfigUtils.maxTotal = maxTotal;
	}

	public static String getMaxIdle() {
		return maxIdle;
	}

	public static void setMaxIdle(String maxIdle) {
		ConfigUtils.maxIdle = maxIdle;
	}

	public static String getMinIdle() {
		return minIdle;
	}

	public static void setMinIdle(String minIdle) {
		ConfigUtils.minIdle = minIdle;
	}
}
