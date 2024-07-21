package com.mysite.sbb.apistudy;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "OPENAPI")
public class OpenApiEntity {

    //openApiId
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "openApiId")
    private Long openApiId;

    //rnum
    @Column(name = "rNum")
    private Long rNum;

    //code
    @Column(name = "code")
    private String code;

    //서울시 구 이름
    @Column(name = "name")
    private String name;

    public OpenApiEntity(Long openApiId, Long rNum, String code, String name) {
        this.openApiId = openApiId;
        this.rNum = rNum;
        this.code = code;
        this.name = name;
    }
}
