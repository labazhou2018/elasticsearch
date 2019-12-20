package com.elastic.operations;

import com.elastic.annotations.Document;
import com.elastic.beans.es.BaseESEnity;
import org.elasticsearch.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.elasticsearch.repository.support.SimpleElasticsearchRepository;

import javax.annotation.PostConstruct;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @Date: 2019/12/19 17:46
 **/
public abstract class ElasticSearchOperation<T extends BaseESEnity> {

	@Autowired
	private Client client;

	private String indexName;

	/**
	 * ES7.0过时，8.0将废弃
	 */
	private String type;

	/**
	 * 操作实体对应的类型
	 */
	private Class<T> rawType;

	@PostConstruct
	private void init(){
		this.rawType = resolveReturnedClassFromGenericType();

	}

	/**
	 * 确定index 及 type
	 * @throws Exception
	 */
	private void resolveConfigEnity() throws Exception {
		if (!AnnotatedElementUtils.isAnnotated(Document.class, this.type)){
			throw new Exception("注解异常");
		}
		Document document = AnnotatedElementUtils.getMergedAnnotation(rawType,Document.class);
		this.indexName = document.indexName();
		this.type = document.type();
	}

	/**
	 * 查找具体实体类型
	 * @return
	 */
	private Class<T> resolveReturnedClassFromGenericType() {
		ParameterizedType parameterizedType = resolveReturnedClassFromGenericType(getClass());
		return (Class<T>) parameterizedType.getActualTypeArguments()[0];
	}

	private ParameterizedType resolveReturnedClassFromGenericType(Class<?> clazz) {
		Object genericSuperclass = clazz.getGenericSuperclass();

		if (genericSuperclass instanceof ParameterizedType) {
			ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
			Type rawtype = parameterizedType.getRawType();
			if (SimpleElasticsearchRepository.class.equals(rawtype)) {
				return parameterizedType;
			}
		}

		return resolveReturnedClassFromGenericType(clazz.getSuperclass());
	}
}
