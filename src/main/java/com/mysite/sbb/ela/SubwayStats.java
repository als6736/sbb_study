package com.mysite.sbb.ela;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "subway_stats")
@Getter
@Setter
public class SubwayStats {

    @Id
    private String id;
    private String useYmd;
    private String routeLine;
    private String stationName;
    private double gtonTnope;
    private double gtoffTnope;
    private String regYmd;

    // Getters and Setters
}
