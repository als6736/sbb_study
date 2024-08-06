package com.mysite.sbb.elasticsearch;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(indexName = "products")
public class Product {

    @Id
    private Long id;
    private String name;
    private String description;
    private Double price;
}
