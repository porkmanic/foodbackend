package com.intelli5.back.repository.search;

import com.intelli5.back.domain.OrderItem;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the OrderItem entity.
 */
public interface OrderItemSearchRepository extends ElasticsearchRepository<OrderItem, Long> {
}
