package com.elastic.operations.search;

import com.elastic.beans.es.StudentESEnity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Date: 2019/12/20 14:31
 **/
@Repository
public class StudentElasticSearchResposity extends PublicElasticSearchResposity<StudentESEnity> {

	public List<StudentESEnity> termQuery(String term, String value) {
		return null;
	}

}
