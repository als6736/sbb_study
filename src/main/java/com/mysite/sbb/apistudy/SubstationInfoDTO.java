package com.mysite.sbb.apistudy;

import lombok.Data;

@Data
public class SubstationInfoDTO {
    private String id;
    private String useYmd;
    private String sbwyRoutLnNm;
    private String sbwyStnsNm;
    private double gtonTnope;
    private double gtoffTnope;
    private String regYmd;

    public SubstationInfoDTO(String id, String useYmd, String sbwyRoutLnNm, String sbwyStnsNm, double gtonTnope, double gtoffTnope, String regYmd) {
        this.id = id;
        this.useYmd = useYmd;
        this.sbwyRoutLnNm = sbwyRoutLnNm;
        this.sbwyStnsNm = sbwyStnsNm;
        this.gtonTnope = gtonTnope;
        this.gtoffTnope = gtoffTnope;
        this.regYmd = regYmd;
    }
}
