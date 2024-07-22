package com.mysite.sbb.apistudy;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class SubstationInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String USE_YMD;
    private String SBWY_ROUT_LN_NM;
    private String SBWY_STNS_NM;
    private double GTON_TNOPE;
    private double GTOFF_TNOPE;
    private String REG_YMD;

    private SubstationInfo() {}

    public SubstationInfo(Long id, String USE_YMD, String SBWY_ROUT_LN_NM, String SBWY_STNS_NM, double GTON_TNOPE, double GTOFF_TNOPE, String REG_YMD) {
        this.id = id;
        this.USE_YMD = USE_YMD;
        this.SBWY_ROUT_LN_NM = SBWY_ROUT_LN_NM;
        this.SBWY_STNS_NM = SBWY_STNS_NM;
        this.GTON_TNOPE = GTON_TNOPE;
        this.GTOFF_TNOPE = GTOFF_TNOPE;
        this.REG_YMD = REG_YMD;
    }
}
