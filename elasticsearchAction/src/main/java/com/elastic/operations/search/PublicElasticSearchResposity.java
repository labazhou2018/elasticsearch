package com.elastic.operations.search;

import com.elastic.Utils.JacksonHelper;
import com.elastic.beans.es.BaseESEnity;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.rest.RestStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @Date: 2019/12/20 17:10
 **/
public class PublicElasticSearchResposity<T extends BaseESEnity> extends AbstractElasticSearchResposity<T>
		implements ElasticsearchRepository<T> {

	private static final Logger LOGGER = LoggerFactory.getLogger(PublicElasticSearchResposity.class);

	@Autowired
	private Client client;

	/**
	 * User实体和Student实体，需要ES操作公共方法，特殊方法可以分别放到
	 * StudentElasticSearchResposity和UserElasticSearchResposity两个类中
	 *
	 * @param id
	 * @return
	 */
	@Override
	public Optional<T> search(String id) {
		String className = ClassUtils.getShortName(entityClass);
		LOGGER.info(String.format("find begin , class:[%s] , id:[%s]:", className, id));
		GetResponse getResponse = client.prepareGet(this.indexName, this.type, id).get();
		if (getResponse.isExists() && !getResponse.isSourceEmpty()) {
			String sourceDoc = getResponse.getSourceAsString();
			try {
				T entiy = JacksonHelper.getObjectMapper().readValue(sourceDoc, this.entityClass);
				LOGGER.info("find success enity:" + className);
				return Optional.ofNullable(entiy);
			} catch (IOException e) {
				LOGGER.error("conver es source to enity fail");
			}
		}
		return Optional.empty();
	}

	@Override
	public void insert(T enity) throws Exception {
		String className = ClassUtils.getShortName(entityClass);
		if (StringUtils.isEmpty(enity.getId())) {
			throw new Exception("id is empty");
		}
		String createTime = LocalDateTime.now().toString();
		if (StringUtils.isEmpty(createTime)) {
			enity.setCreateTime(createTime);
		}
		LOGGER.info(String.format("insert begin , class:[%s] , id:[%s]:", className, enity.getId()));
		String insertSource = JacksonHelper.getObjectMapper().writeValueAsString(enity);
		IndexResponse indexResponse = this.client
				.prepareIndex(this.indexName, this.type, enity.getId())
				.setSource(insertSource)
				.execute()
				.actionGet();
		LOGGER.info(String.format("insert end , class:[%s] , id:[%s]:", className, indexResponse.getId()));

	}

	@Override
	public void update(T enity) {
		String className = ClassUtils.getShortName(entityClass);
		LOGGER.info(String.format("update begin , class:[%s] , id:[%s]:", className, enity.getId()));

		String updateTime = LocalDateTime.now().toString();
		enity.setUpdateTime(updateTime);

		UpdateResponse updateResponse = this.client
				.prepareUpdate(this.indexName, this.type, enity.getId())
				.setDoc(enity)
				.execute()
				.actionGet();
		LOGGER.info(String.format("update end , class:[%s] , id:[%s]:", className, updateResponse.getId()));
	}

	@Override
	public void delete(String id) {
		String className = ClassUtils.getShortName(entityClass);
		LOGGER.info(String.format("delete begin , class:[%s] , id:[%s]:", className, id));

		DeleteResponse deleteResponse = this.client
				.prepareDelete(this.indexName, this.type, id)
				.execute()
				.actionGet();
		if (deleteResponse.status() == RestStatus.OK) {
			LOGGER.info(String.format("delete end, class:[%s] , id:[%s]:", className, id));
		} else {
			LOGGER.info(String.format("delete error, class:[%s] , id:[%s]:", className, id));
		}
	}

}
