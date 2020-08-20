package com.backbase.goldensample.review.persistence;

import static javax.persistence.GenerationType.IDENTITY;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Entity
@Table(
        name = "review",
    indexes = {
      @Index(name = "review_unique_idx", unique = true, columnList = "productId, id")
    })
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewEntity {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  @Version private int version;

  @NonNull
  private Long productId;
  @NonNull
  @NotBlank
  @Size(min = 6, max = 50)
  private String author;
  @NonNull
  @NotBlank
  private String subject;
  @NonNull
  @NotBlank
  private String content;
}
