package com.elastic;

import com.elastic.transprotclient.TransportClientBuild;
import org.elasticsearch.client.Client;
import org.junit.Test;

/**
 *
 * @Date: 2019/3/15 10:46
 **/
public class SingleThreadTest {

	/**
	 * 单线程 创建一个简单的TransportClient连接
	 */
	@Test
	public void testTransportClient() {
		for (int i = 0; i < 10; i++) {
			Client client = TransportClientBuild.getClient();
			System.out.println(("连接对象————地址:" + client));

		}
	}

}
