package com.elastic;

import com.elastic.client.highlevelclient.HighLevelClient;
import org.elasticsearch.client.Client;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Date: 2019/3/15 11:18
 **/
public class MultiThreadTest {

	/**
	 * 多线程测试HighLevelClient连接
	 */
	@Test
	public void testRestClient() {

		//System.out.println(TransportClientBuild.getClient());
		System.setProperty("es.set.netty.runtime.available.processors", "false");

		ExecutorService executorService = new ThreadPoolExecutor(10, 10,
				0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());

		for (int i = 0; i < 10; i++) {
			int index = i;

			if (!executorService.isShutdown()) {
				executorService.execute(new Runnable() {
					@Override
					public void run() {
						System.out.println(("第" + index + "次获取到了连接对象————地址:" + HighLevelClient.getClient()));
					}
				});
			}

		}
		executorService.shutdown();

        //关闭线程池
		try {
			while (!executorService.awaitTermination(10000, TimeUnit.MILLISECONDS)) {
				System.out.println("10秒没有执行完，强制关闭线程池");
				executorService.shutdownNow();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
