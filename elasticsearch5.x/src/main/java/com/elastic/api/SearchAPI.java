package com.elastic.api;

import com.elastic.transprotclient.TransportClientBuild;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

/**
 * @Date: 2019/3/19 15:36
 **/
public class SearchAPI {

	Client client = TransportClientBuild.getClient();

	public void search(){
		SearchResponse response = client.prepareSearch("index1", "index2")
				.setTypes("type1", "type2")
				.setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
				// Query
				.setQuery(termQuery("multi", "test"))
				// Filter
				.setPostFilter(QueryBuilders.rangeQuery("age").from(12).to(18))
				.setFrom(0).setSize(60).setExplain(true)
				.get();
	}

	public void searchScroll(){
		QueryBuilder qb = termQuery("multi", "test");
		String test = "test";
		SearchResponse scrollResp = client.prepareSearch(test)
				.addSort(FieldSortBuilder.DOC_FIELD_NAME, SortOrder.ASC)
				.setScroll(new TimeValue(60000))
				.setQuery(qb)
				//max of 100 hits will be returned for each scroll
				.setSize(100)
				.get();

		//Scroll until no hits are returned
		do {
			for (SearchHit hit : scrollResp.getHits().getHits()) {
				//Handle the hit...
			}

			scrollResp = client.prepareSearchScroll(scrollResp.getScrollId()).setScroll(new TimeValue(60000)).execute().actionGet();
		}
		// Zero hits mark the end of the scroll and the while loop.
		while(scrollResp.getHits().getHits().length != 0);

	}


	public void mutliSearch(){
		SearchRequestBuilder srb1 = client
				.prepareSearch().setQuery(QueryBuilders.queryStringQuery("elasticsearch")).setSize(1);
		SearchRequestBuilder srb2 = client
				.prepareSearch().setQuery(QueryBuilders.matchQuery("name", "kimchy")).setSize(1);

		MultiSearchResponse sr = client.prepareMultiSearch()
				.add(srb1)
				.add(srb2)
				.get();

		// You will get all individual responses from MultiSearchResponse#getResponses()
		long nbHits = 0;
		for (MultiSearchResponse.Item item : sr.getResponses()) {
			SearchResponse response = item.getResponse();
			nbHits += response.getHits().getTotalHits();
		}
	}

	public void aggregationSearch(){
		SearchResponse sr = client.prepareSearch()
				.setQuery(QueryBuilders.matchAllQuery())
				.addAggregation(
						AggregationBuilders.terms("agg1").field("field")
				)
				.addAggregation(
						AggregationBuilders.dateHistogram("agg2")
								.field("birth")
								.dateHistogramInterval(DateHistogramInterval.YEAR)
				)
				.get();

		// Get your facet results
		Terms agg1 = sr.getAggregations().get("agg1");
		Histogram agg2 = sr.getAggregations().get("agg2");
	}

}
