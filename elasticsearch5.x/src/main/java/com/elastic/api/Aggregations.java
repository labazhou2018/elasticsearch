package com.elastic.api;

import com.elastic.transprotclient.TransportClientBuild;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.filters.Filters;
import org.elasticsearch.search.aggregations.bucket.filters.FiltersAggregator;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.metrics.stats.Stats;
import org.elasticsearch.search.aggregations.metrics.stats.StatsAggregationBuilder;

import static org.elasticsearch.index.query.QueryBuilders.termQuery;

/**
 * @Date: 2019/3/22 11:32
 **/
public class Aggregations {


	Client client = TransportClientBuild.getClient();

	/**
	 * 4.1 Structuring aggregations
	 */
	public void structAgg() {
		SearchResponse sr = client.prepareSearch()
				.addAggregation(AggregationBuilders
						.terms("by_country")
						.field("country")
						.subAggregation(AggregationBuilders
								.dateHistogram("by_year")
								.field("dateOfBirth")
								.dateHistogramInterval(DateHistogramInterval.YEAR)
								.subAggregation(AggregationBuilders
										.avg("avg_children")
										.field("children"))
						)
				)
				.execute().actionGet();

	}

	/**
	 * 4.2 metrics_aggregations
	 */
	public void metricAgg() {

		StatsAggregationBuilder aggregation =
				AggregationBuilders
						/*聚合名称*/
						.stats("agg")
						.field("height");

		SearchResponse searchResponse = client.prepareSearch()
				.addAggregation(aggregation)
				.execute()
				.actionGet();

		Stats agg = searchResponse.getAggregations().get("agg");
		double min = agg.getMin();
		double max = agg.getMax();
		double avg = agg.getAvg();
		double sum = agg.getSum();
		long count = agg.getCount();

	}

	/**
	 * 4.3 Bucket aggregations
	 */
	public void bucketAgg() {
		AggregationBuilder aggregation =
				AggregationBuilders
						.filters("agg",
								new FiltersAggregator.KeyedFilter("men", QueryBuilders.termQuery("gender", "male")),
								new FiltersAggregator.KeyedFilter("women", QueryBuilders.termQuery("gender", "female")));


		SearchResponse searchResponse = client.prepareSearch()
				.addAggregation(aggregation)
				.execute()
				.actionGet();


		// searchResponse is here your SearchResponse object
		Filters agg = searchResponse.getAggregations().get("agg");

		// For each entry
		for (Filters.Bucket entry : agg.getBuckets()) {
			// bucket key
			String key = entry.getKeyAsString();
			// Doc count
			long docCount = entry.getDocCount();
			System.out.print("Key :" + key + "douCount:" + docCount);
		}
	}

}