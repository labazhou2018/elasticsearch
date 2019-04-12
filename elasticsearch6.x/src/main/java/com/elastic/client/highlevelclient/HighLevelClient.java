package com.elastic.client.highlevelclient;

import com.elastic.client.ClientBuilders;
import com.elastic.exception.ESIoException;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;

/**
 * @Date: 2019/2/28 14:26
 **/
public class HighLevelClient {

	private static RestClientBuilder restClientBuilder = ClientBuilders.getClientBulider();

	private static RestHighLevelClient restHighLevelClient;

	/**
	 * RestHighLevelClient是基于RestClient构建
	 */
	public static RestHighLevelClient getClient() {

		restHighLevelClient = new RestHighLevelClient(restClientBuilder);
		return restHighLevelClient;
	}

	/**
	 * 关闭连接
	 * */
	public static void closeRestHighLevelClient() throws ESIoException {
		if (null != restHighLevelClient) {
			try {
				restHighLevelClient.close();
			} catch (IOException e) {
				throw new ESIoException("RestHighLevelClient Client close exception", e);
			}
		}
	}

}
