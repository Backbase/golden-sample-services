package com.backbase.goldensample.review.dto;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDTO {

    private Long id;
    private Long productId;
    private String author;
    private String subject;
    private String content;
    private Integer stars;
    private Map<String, String> additions;

}
