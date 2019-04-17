package com.elastic.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.elastic.client.lowlevelclient.LowLevelClient;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.ResponseException;
import org.elasticsearch.client.RestClient;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

/**
 * Description:rest client api
 * @Date: 2019/3/13 10:55
 **/
public class LowLevelClientAPI {

	private RestClient restClient = LowLevelClient.getClient();

	public void search() throws IOException {
		Map<String, String> params = Collections.emptyMap();

		String queryString = "{" +
				"  \"size\": 20," +
				"  \"query\": {" +
				"   \"range\": {" +
				"     \"createTime\": {" +
				"       \"gte\": \"2018-06-01 00:00:00\"" +
				"     }" +
				"   }" +
				"  }" +
				"}";

		HttpEntity entity = new NStringEntity(queryString, ContentType.APPLICATION_JSON);

		try {

			Response response = restClient.performRequest("GET", "/some_important_index*/_search", params, entity);
			System.out.println(response.getStatusLine().getStatusCode());
			String responseBody = null;

			responseBody = EntityUtils.toString(response.getEntity());
			System.out.println("******************************************** ");

			JSONObject jsonObject = JSON.parseObject(responseBody);


			System.out.println(jsonObject.get("hits"));
		}catch (ResponseException e){
			e.printStackTrace();
		}
		System.out.println("23333");

	}


}
