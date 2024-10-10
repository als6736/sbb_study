package com.mysite.sbb.ela;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubwayStatsRepository extends ElasticsearchRepository<SubwayStats, String>, CrudRepository<SubwayStats, String> {

    List<SubwayStats> findByRouteLine(String routeLine);

    List<SubwayStats> findByStationName(String stationName);

    List<SubwayStats> findByUseYmd(String useYmd);
}
