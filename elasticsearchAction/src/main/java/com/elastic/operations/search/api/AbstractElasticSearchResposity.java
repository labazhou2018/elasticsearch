package com.elastic.operations.search.api;

import com.elastic.annotations.Document;
import com.elastic.beans.es.BaseESEnity;
import com.elastic.operations.mapping.MappingOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.elasticsearch.annotations.Mapping;
import org.springframework.data.elasticsearch.repository.support.SimpleElasticsearchRepository;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;


/**
 * @Date: 2019/12/19 17:46
 **/
public abstract class AbstractElasticSearchResposity<T extends BaseESEnity> {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractElasticSearchResposity.class);


	public MappingOperations mappingOperations;

	/**
	 * 索引名称
	 */
	public String indexName;

	/**
	 * ES7.0过时，8.0将废弃
	 */
	public String type;

	/**
	 * 操作实体对应的类型
	 */
	public Class<T> entityClass;

	@PostConstruct
	private void init() throws Exception {
		this.entityClass = resolveReturnedClassFromGenericType();
		this.resolveConfigEnity();
		this.putMappings(entityClass);

	}


	/**
	 * 确定index 及 type
	 *
	 * @throws Exception
	 */
	private void resolveConfigEnity() throws Exception {
		if (!AnnotatedElementUtils.isAnnotated(Document.class, this.type)) {
			throw new Exception("ES实体缺少注解异常");
		}
		Document document = AnnotatedElementUtils.getMergedAnnotation(entityClass, Document.class);
		this.indexName = document.indexName();
		this.type = document.type();
	}

	/**
	 * 查找具体实体类型
	 *
	 * @return
	 */
	private Class<T> resolveReturnedClassFromGenericType() {
		ParameterizedType parameterizedType = resolveReturnedClassFromGenericType(getClass());
		return (Class<T>) parameterizedType.getActualTypeArguments()[0];
	}

	/**
	 * 实体类mappings设置
	 *
	 * @param clazz
	 * @return
	 */
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

	private void putMappings(Class<T> clazz) {
		if (clazz.isAnnotationPresent(Mapping.class)) {
			String mappingPath = clazz.getAnnotation(Mapping.class).mappingPath();
			if (StringUtils.hasText(mappingPath)) {
				String mappings = readFileFromClasspath(mappingPath);
				mappingOperations.putMapping(indexName, type, mappings);
			}
		} else {
			LOGGER.info("Class:" + ClassUtils.getShortName(clazz) + "using default mappings");
		}
	}

	private String readFileFromClasspath(String url) {
		StringBuilder stringBuilder = new StringBuilder();

		BufferedReader bufferedReader = null;

		try {
			ClassPathResource classPathResource = new ClassPathResource(url);
			InputStreamReader inputStreamReader = new InputStreamReader(classPathResource.getInputStream());
			bufferedReader = new BufferedReader(inputStreamReader);
			String line;

			String lineSeparator = System.getProperty("line.separator");
			while ((line = bufferedReader.readLine()) != null) {
				stringBuilder.append(line).append(lineSeparator);
			}
		} catch (Exception e) {
			LOGGER.debug(String.format("Failed to load file from url: %s: %s", url, e.getMessage()));
			return null;
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					LOGGER.debug(String.format("Unable to close buffered reader.. %s", e.getMessage()));
				}
			}
		}
		return stringBuilder.toString();
	}
}