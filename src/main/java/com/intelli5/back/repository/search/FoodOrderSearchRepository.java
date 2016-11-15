package com.intelli5.back.repository.search;

import com.intelli5.back.domain.FoodOrder;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the FoodOrder entity.
 */
public interface FoodOrderSearchRepository extends ElasticsearchRepository<FoodOrder, Long> {
}
