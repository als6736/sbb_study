package com.mysite.sbb.apistudy;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubstationInfoRepository extends ElasticsearchRepository<SubstationInfo, Long>, CrudRepository<SubstationInfo, Long>{
    List<SubstationInfo> findBySbwyStnsNm(String sbwyStnsNm);
    List<SubstationInfo> findBySbwyRoutLnNm(String sbwyRoutLnNm);
    // null이면 안가지고옴
    List<SubstationInfo> findByUseYmdIsNotNullAndRegYmdIsNotNull();
}
