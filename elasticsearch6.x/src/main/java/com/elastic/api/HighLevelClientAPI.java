package com.elastic.api;

import com.elastic.client.highlevelclient.HighLevelClient;
import org.elasticsearch.client.RestHighLevelClient;

/**
 * Description:high level rest client api
 *
 * @Date: 2019/3/13 15:48
 **/
public class HighLevelClientAPI {

	private RestHighLevelClient restHighLevelClient = HighLevelClient.getClient();

	public void searh() {

	}
}