package com.mysite.sbb.apistudy;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubstationInfoRepository extends JpaRepository<SubstationInfo, Long> {
    @Query(value = "SELECT * FROM substation_info u WHERE u.sbwy_stns_nm = ?1", nativeQuery = true)
    List<SubstationInfo> findByName(@Param("sbwy_stns_nm") String sbwy_stns_nm);

    @Query(value = "SELECT * FROM substation_info u WHERE u.sbwy_rout_ln_nm = ?1", nativeQuery = true)
    List<SubstationInfo> findByLine(@Param("sbwy_rout_ln_nm") String sbwy_rout_ln_nm);
}
