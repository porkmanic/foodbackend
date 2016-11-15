package com.intelli5.back.repository.search;

import com.intelli5.back.domain.FoodJoint;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the FoodJoint entity.
 */
public interface FoodJointSearchRepository extends ElasticsearchRepository<FoodJoint, Long> {
}
