package com.mysite.sbb.apistudy;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "substation_info")
@Data
public class SubstationInfo {

    @Id
    private Long id;
    private String useYmd;
    private String sbwyRoutLnNm;
    private String sbwyStnsNm;
    private double gtonTnope;
    private double gtoffTnope;
    private String regYmd;

    private SubstationInfo() {}

    public SubstationInfo(Long id, String useYmd, String sbwyRoutLnNm, String sbwyStnsNm, double gtonTnope, double gtoffTnope, String regYmd) {
        this.id = id;
        this.useYmd = useYmd;
        this.sbwyRoutLnNm = sbwyRoutLnNm;
        this.sbwyStnsNm = sbwyStnsNm;
        this.gtonTnope = gtonTnope;
        this.gtoffTnope = gtoffTnope;
        this.regYmd = regYmd;
    }
}
