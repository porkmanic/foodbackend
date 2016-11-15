package com.intelli5.back.repository.search;

import com.intelli5.back.domain.Payment;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Payment entity.
 */
public interface PaymentSearchRepository extends ElasticsearchRepository<Payment, Long> {
}
