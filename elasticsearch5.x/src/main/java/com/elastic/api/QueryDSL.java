package com.elastic.api;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

/**
 * @Date: 2019/3/25 16:26
 **/
public class QueryDSL {


	public void matchAllQueries() {
		QueryBuilder qb = QueryBuilders.matchAllQuery();

	}

	public void fullTextQuery() {
		QueryBuilder qb1 = QueryBuilders.matchQuery(
				"name",
				"kimchy elasticsearch"
		);

		QueryBuilder qb2 = QueryBuilders.multiMatchQuery(
				"kimchy elasticsearch",
				"user", "message"
		);

		QueryBuilder qb3 = QueryBuilders.commonTermsQuery("name",
				"kimchy");


	}

	public void termQuery() {
		QueryBuilder qb1 = QueryBuilders.termQuery(
				"name",
				"kimchy"
		);

		QueryBuilder qb2 = QueryBuilders.termsQuery("tags",
				"blue", "pill");

		QueryBuilder qb3 = QueryBuilders.rangeQuery("price")
				.from(5)
				.to(10)
				.includeLower(true)
				.includeUpper(false);

		QueryBuilder qb = QueryBuilders.rangeQuery("age")
				.gte("10")
				.lt("20");


		QueryBuilder qb4 = QueryBuilders.existsQuery("name");


		QueryBuilder qb5 = QueryBuilders.wildcardQuery("user", "k?mc*");


		QueryBuilder qb6 = QueryBuilders.regexpQuery(
				"name.first",
				"s.*y");


		QueryBuilder qb7 = QueryBuilders.fuzzyQuery(
				"name",
				"kimzhy"
		);


		QueryBuilder qb8 = QueryBuilders.typeQuery("my_type");

		/*type  可选项*/
		QueryBuilder qb9 = QueryBuilders.idsQuery("my_type", "type2")
				.addIds("1", "4", "100");

		QueryBuilder qb10 = QueryBuilders.idsQuery()
				.addIds("1", "4", "100");

	}
}
