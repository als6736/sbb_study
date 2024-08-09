package com.mysite.sbb.ela;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubwayStatsRepository extends ElasticsearchRepository<SubwayStats, String>, CrudRepository<SubwayStats, String> {
}
