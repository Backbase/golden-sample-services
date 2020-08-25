package com.backbase.goldensample.review.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Review {
  private int productId;
  private int reviewId;
  private String author;
  private String subject;
  private String content;
}
