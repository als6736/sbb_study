package com.mysite.sbb.apistudy;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

@Document(indexName = "substation_info")
@Data
public class SubstationInfo {

    private static final Logger logger = LoggerFactory.getLogger(SubstationInfo.class);

    @Id
    private String id; // Elasticsearch에서 사용하는 ID
    private Long useYmd;
    private Long regYmd;
    private String sbwyRoutLnNm;
    private String sbwyStnsNm;
    private double gtonTnope;
    private double gtoffTnope;

    private SubstationInfo() {}

    public SubstationInfo(String id, Long useYmd, String sbwyRoutLnNm, String sbwyStnsNm, double gtonTnope, double gtoffTnope, Long regYmd) {
        this.id = id;
        this.useYmd = useYmd != null ? useYmd : 0L;
        this.sbwyRoutLnNm = sbwyRoutLnNm;
        this.sbwyStnsNm = sbwyStnsNm;
        this.gtonTnope = gtonTnope;
        this.gtoffTnope = gtoffTnope;
        this.regYmd = regYmd != null ? regYmd : 0L;
        logger.info("SubstationInfo created: {}", this);
    }
}
