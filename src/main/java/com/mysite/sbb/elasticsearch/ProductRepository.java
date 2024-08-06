package com.mysite.sbb.elasticsearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends ElasticsearchRepository<Product, Long>, CrudRepository<Product, Long> {
}
