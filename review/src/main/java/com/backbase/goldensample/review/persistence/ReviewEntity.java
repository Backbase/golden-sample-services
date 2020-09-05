package com.backbase.goldensample.review.persistence;

import com.backbase.goldensample.review.config.IdentityStrategyOverrideConfiguration;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

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

    /**
     * <p>Primary key is generated with {@link GenerationType#SEQUENCE} for every database except MySql which does not
     * support sequences.</p> <p>For MySql the generation strategy is overridden to be {@link
     * GenerationType#IDENTITY}.</p>
     *
     * @see IdentityStrategyOverrideConfiguration
     */
    @Id
    @GenericGenerator(
        name = "sequenceGenerator",
        strategy = "enhanced-sequence",
        parameters = {
            @org.hibernate.annotations.Parameter(
                name = "optimizer",
                value = "pooled-lo"
            ),
            @org.hibernate.annotations.Parameter(
                name = "initial_value",
                value = "1"
            ),
            @org.hibernate.annotations.Parameter(
                name = "increment_size",
                value = "5"
            ),
            @org.hibernate.annotations.Parameter(
                name = "sequence_name",
                value = "hibernate_sequence"
            )
        }
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "sequenceGenerator"
    )
    private Long id;

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
