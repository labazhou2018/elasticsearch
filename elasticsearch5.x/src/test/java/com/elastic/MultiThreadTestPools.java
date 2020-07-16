package com.elastic;

import com.elastic.pool.config.TransportClientNodeConfig;
import com.elastic.pool.config.TransportClientPoolConfig;
import com.elastic.pool.espool.TransportClientPool;
import org.elasticsearch.client.transport.TransportClient;
import org.junit.Test;

import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 测试类描述
 *
 * @Date: 2019/3/15 11:24
 **/
public class MultiThreadTestPools {


	/**
	 * 多线程线程池测试TransportClient连接池
	 */
	@Test
	public void testESPoolClient() {

		/*配置节点熟悉*/
		Set<TransportClientNodeConfig> nodeSets = TransportClientPoolConfig.getNodesConfig();

		AtomicReference<Set<TransportClientNodeConfig>> nodesReference = new AtomicReference<Set<TransportClientNodeConfig>>();
		nodesReference.set(nodeSets);

		/*配置线程池*/
		TransportClientPoolConfig transportClientPoolConfig = new TransportClientPoolConfig();

		final TransportClientPool pool = new TransportClientPool(nodesReference, transportClientPoolConfig);

		System.setProperty("es.set.netty.runtime.available.processors", "false");

		LinkedBlockingQueue<Runnable> linkBlockingQuene = new LinkedBlockingQueue<Runnable>();
		ExecutorService cachedThreadPool = new ThreadPoolExecutor(10, 10, 0L, TimeUnit.MILLISECONDS, linkBlockingQuene);
		for (int i = 0; i < 10; i++) {
			final int index = i;
			cachedThreadPool.execute(new Runnable() {

				public void run() {
					TransportClient client = null;
					try {
						client = pool.getResource();
						System.out.println("当前线程" + index + "获取线程地址:" + client.toString());
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						pool.returnResource(client);
					}
				}
			});
		}
		cachedThreadPool.shutdown();

		try {
			while (!cachedThreadPool.awaitTermination(10000, TimeUnit.MILLISECONDS)) {
				System.out.println("10秒没有执行完，强制关闭线程池");
				cachedThreadPool.shutdownNow();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
