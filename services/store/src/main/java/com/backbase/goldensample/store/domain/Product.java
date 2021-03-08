package com.backbase.goldensample.store.domain;

import static java.util.Collections.emptyList;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Product {

    private String name;

    private Long productId;

    private Integer weight;

    private LocalDate createDate;

    private List<Review> reviews = emptyList();
}
