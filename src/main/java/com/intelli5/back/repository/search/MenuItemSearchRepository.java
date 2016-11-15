package com.intelli5.back.repository.search;

import com.intelli5.back.domain.MenuItem;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the MenuItem entity.
 */
public interface MenuItemSearchRepository extends ElasticsearchRepository<MenuItem, Long> {
}
