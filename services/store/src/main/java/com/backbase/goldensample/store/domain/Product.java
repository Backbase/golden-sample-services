package com.backbase.goldensample.store.domain;

import static java.util.Collections.emptyList;
import static org.apache.commons.compress.utils.Lists.newArrayList;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.compress.utils.Lists;

@Getter
@Setter
public class Product {

    private String name;

    private Long productId;

    private Integer weight;

    private LocalDate createDate;

    private Map<String, String> additions;

    private List<Review> reviews = newArrayList();
}
