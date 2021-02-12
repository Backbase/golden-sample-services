package com.backbase.goldensample.store.domain;

import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Product {

    private String name;

    private Long productId;

    private Integer weight;

    private LocalDate createDate;

    private List<Review> reviews;
}
