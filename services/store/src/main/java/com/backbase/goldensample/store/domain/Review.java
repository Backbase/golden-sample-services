package com.backbase.goldensample.store.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class Review {

    private Long reviewId;

    private Long productId;

    private String author;

    private String content;

    private String subject;

    private Map<String, String> additions;

    /*
     * Added in v2 of review api.
     */
    private Integer stars;

}
