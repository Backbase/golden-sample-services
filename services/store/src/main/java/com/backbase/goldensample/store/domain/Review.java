package com.backbase.goldensample.store.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Review {

    private Long reviewId;

    private Long productId;

    private String author;

    private String content;

    private String subject;

    /*
     * Added in v2 of review api.
     */
    private Integer stars;

}
