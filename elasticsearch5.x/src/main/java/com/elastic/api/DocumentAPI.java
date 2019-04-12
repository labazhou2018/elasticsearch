package com.elastic.api;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.elastic.transprotclient.TransportClientBuild;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.bulk.*;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptService;
import org.elasticsearch.search.aggregations.support.ValuesSource;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * @Date: 2019/3/18 16:08
 **/
public class DocumentAPI {

	Client client = TransportClientBuild.getClient();

	/*index API  begin*/

	/**
	 * 手动生成JSON
	 */
	public void CreateJSON() {

		String json = "{" +
				"\"user\":\"fendo\"," +
				"\"postDate\":\"2013-01-30\"," +
				"\"message\":\"Hell word\"" +
				"}";

		IndexResponse response = client.prepareIndex("fendo", "fendodate")
				.setSource(json)
				.get();
		System.out.println(response.getResult());

	}


	/**
	 * 使用集合
	 */
	public void CreateList() {

		Map<String, Object> json = new HashMap<String, Object>();
		json.put("user", "kimchy");
		json.put("postDate", "2013-01-30");
		json.put("message", "trying out Elasticsearch");

		IndexResponse response = client.prepareIndex("fendo", "fendodate")
				.setSource(json)
				.get();
		System.out.println(response.getResult());

	}

	/**
	 * 使用JACKSON序列化
	 *
	 * @throws Exception
	 */
	public void CreateJACKSON() throws Exception {

		MyBlog myBlog = new MyBlog();
		myBlog.setAuther("elastic");
		myBlog.setContent("es 测试");
		myBlog.setDate(new Date());

		// instance a json mapper
		ObjectMapper mapper = new ObjectMapper();

		// generate json
		byte[] json = mapper.writeValueAsBytes(myBlog);

		IndexResponse response = client.prepareIndex("elastic", "search")
				.setSource(json)
				.get();
		System.out.println(response.getResult());
	}

	/**
	 * 使用ElasticSearch 帮助类
	 *
	 * @throws IOException
	 */
	public void CreateXContentBuilder() throws IOException {

		XContentBuilder builder = jsonBuilder()
				.startObject()
				.field("user", "ccse")
				.field("postDate", new Date())
				.field("message", "this is Elasticsearch")
				.endObject();

		IndexResponse response = client.prepareIndex("fendo", "fendodata").setSource(builder).get();
		System.out.println("创建成功!");


	}

	private static class MyBlog implements Serializable {
		private String Auther;
		private String Content;
		private Date date;

		public String getAuther() {
			return Auther;
		}

		public void setAuther(String auther) {
			Auther = auther;
		}

		public String getContent() {
			return Content;
		}

		public void setContent(String content) {
			Content = content;
		}

		public Date getDate() {
			return date;
		}

		public void setDate(Date date) {
			this.date = date;
		}
	}

	/*index API  end*/

	/*get API  begin*/

	public void getAPI() {
		/*根据id查询 */
		GetResponse response = client.prepareGet("twitter", "tweet", "1")
				/*operationThreaded 设置为 true 是在不同的线程里执行此次操作，默认为true*/
				.setOperationThreaded(false)
				.get();
	}
	/*get API  end*/

	/*delete API  begin*/
	public void deleteAPI() {
		/*根据id删除*/
		DeleteResponse response = client.prepareDelete("twitter", "tweet", "1")
				.get();

	}
	/*delete API  end*/

	public void deleteByQuery() {
		/*DeleteByQuery API  begin*/
		/*通过查询条件删除*/
		BulkByScrollResponse response =
				DeleteByQueryAction.INSTANCE.newRequestBuilder(client)
						//查询条件
						.filter(QueryBuilders.matchQuery("gender", "male"))
						//index(索引名)
						.source("persons")
						//执行
						.get();
		//删除文档的数量
		long deleted = response.getDeleted();

		/*如果需要执行的时间比较长，可以使用异步的方式处理,结果在回调里面获取*/
		DeleteByQueryAction.INSTANCE.newRequestBuilder(client)
				.filter(QueryBuilders.matchQuery("gender", "male"))
				.source("persons")
				.execute(new ActionListener<BulkByScrollResponse>() {


					public void onResponse(BulkByScrollResponse response) {
						long deleted = response.getDeleted();
					}


					public void onFailure(Exception e) {
						// Handle the exception
					}
				});

	}
	/*DeleteByQuery API  end*/

	/*Update  API  begin*/

	public void updateAPI() throws IOException, ExecutionException, InterruptedException {
		/*UpdateRequest*/
		UpdateRequest updateRequest = new UpdateRequest();
		updateRequest.index("index");
		updateRequest.type("type");
		updateRequest.id("1");
		updateRequest.doc(jsonBuilder()
				.startObject()
				.field("gender", "male")
				.endObject());
		client.update(updateRequest).get();


		client.prepareUpdate("ttl", "doc", "1")
				.setDoc(jsonBuilder()
						.startObject()
						.field("gender", "male")
						.endObject())
				.get();
	}

	/*Update  API  end*/


	/*Multi Get API  begin*/
	public void multiGet() {

		MultiGetResponse multiGetItemResponses = client.prepareMultiGet()
				.add("twitter", "tweet", "1")
				.add("twitter", "tweet", "2", "3", "4")
				.add("another", "type", "foo")
				.get();

		for (MultiGetItemResponse itemResponse : multiGetItemResponses) {
			GetResponse response = itemResponse.getResponse();
			if (response.isExists()) {
				String json = response.getSourceAsString();
			}
		}

	}
	/*Multi Get API  end*/

	/*Bulk API  begin*/
	public void buliAPI() throws IOException {

		BulkRequestBuilder bulkRequest = client.prepareBulk();

		// either use client#prepare, or use Requests# to directly build index/delete requests
		bulkRequest.add(client.prepareIndex("twitter", "tweet", "1")
				.setSource(jsonBuilder()
						.startObject()
						.field("user", "kimchy")
						.field("postDate", new Date())
						.field("message", "trying out Elasticsearch")
						.endObject()
				)
		);

		bulkRequest.add(client.prepareIndex("twitter", "tweet", "2")
				.setSource(jsonBuilder()
						.startObject()
						.field("user", "kimchy")
						.field("postDate", new Date())
						.field("message", "another post")
						.endObject()
				)
		);

		BulkResponse bulkResponse = bulkRequest.get();
		if (bulkResponse.hasFailures()) {
			// process failures by iterating through each bulk response item
		}

	}
	/*Bulk API  end*/

	/*Bulk process  begin*/
	public void bulkProcess() {

		BulkProcessor bulkProcessor = BulkProcessor.builder(
				client,
				new BulkProcessor.Listener() {

					public void beforeBulk(long executionId,
					                       BulkRequest request) {

					}


					public void afterBulk(long executionId,
					                      BulkRequest request,
					                      BulkResponse response) {

					}


					public void afterBulk(long executionId,
					                      BulkRequest request,
					                      Throwable failure) {

					}
				})
				.setBulkActions(10000)
				.setBulkSize(new ByteSizeValue(5, ByteSizeUnit.MB))
				.setFlushInterval(TimeValue.timeValueSeconds(5))
				.setConcurrentRequests(1)
				.setBackoffPolicy(
						BackoffPolicy.exponentialBackoff(TimeValue.timeValueMillis(100), 3))
				.build();


	}
	/*Bulk process end*/

}
