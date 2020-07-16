package com.elastic.client.lowlevelclient;

import com.elastic.client.ClientBuilders;
import com.elastic.exception.ESIoException;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;

import java.io.IOException;

/**
 * @Date: 2019/3/4 16:36
 **/
public class LowLevelClient {


	public static RestClient restClient;

	private static RestClientBuilder restClientBuilder = null;

	/**
	 * RestHighLevelClient是基于RestClient构建
	 */
	public static RestClient getClient() {
		ClientBuilders clientBuilders = new ClientBuilders();
		return clientBuilders.getSimpleClientBuilder().build();
	}

	/**
	 * RestClient 类是线程安全的，理想状态下它与使用它的应用程序具有相同的生命周期。当不再使用时强烈建议关闭它：
	 */
	public static void closeClient() throws ESIoException {
		ClientBuilders clientBuilders = new ClientBuilders();
		restClientBuilder = clientBuilders.getSimpleClientBuilder();

		if (null != restClient) {
			try {
				restClient.close();
			} catch (IOException e) {
				throw new ESIoException("Rest Client close exception", e);
			}
		}
	}
}