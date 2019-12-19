package com.elastic;

import com.elastic.transprotclient.TransportClientBuild;
import org.elasticsearch.client.Client;
import org.junit.Test;

import java.util.concurrent.*;

public class MultiThreadTest {


	/**
	 * 多线程测试TransportClient连接
	 */
	@Test
	public void testTransprotClient() {

		//System.out.println(TransportClientBuild.getClient());
		System.setProperty("es.set.netty.runtime.available.processors", "false");

		ExecutorService executorService = new ThreadPoolExecutor(10, 10,
				0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());

		for (int i = 0; i < 10; i++) {
			final int index = i;

			if (!executorService.isShutdown()) {
				executorService.execute(new Runnable() {
					public void run() {
						Client client = TransportClientBuild.getClient();
						System.out.println(("第" + index + "次获取到了连接对象————地址:" + client));
					}
				});
			}

		}
		executorService.shutdown();

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