package com.intelli5.back.repository.search;

import com.intelli5.back.domain.Ticket;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Ticket entity.
 */
public interface TicketSearchRepository extends ElasticsearchRepository<Ticket, Long> {
}
