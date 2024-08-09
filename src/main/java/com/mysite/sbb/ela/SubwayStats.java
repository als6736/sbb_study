package com.mysite.sbb.ela;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "openapi-data-2024.08.09")
@Getter
@Setter
public class SubwayStats {

    @Id
    private String id;
    private String SBWY_ROUT_LN_NM;
    private String SBWY_STNS_NM;
    private double GTOFF_TNOPE;
    private double GTON_TNOPE;
    private String REG_YMD;
    private String USE_YMD;

    // Getters and Setters
}
