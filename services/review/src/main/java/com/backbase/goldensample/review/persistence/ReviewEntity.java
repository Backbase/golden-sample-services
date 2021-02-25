package com.backbase.goldensample.review.persistence;

import com.backbase.goldensample.review.config.IdentityStrategyOverrideConfiguration;
import com.backbase.goldensample.review.mapper.MapToJsonConverter;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(
    name = "review",
    indexes = {
        @Index(name = "review_unique_idx", unique = true, columnList = "productId, id")
    })
@Getter
@Setter
@Builder
@AllArgsConstructor
public class ReviewEntity {

    /*
     * IMPORTANT:
     * Set toString, equals, and hashCode as described in these
     * documents:
     * - https://vladmihalcea.com/the-best-way-to-implement-equals-hashcode-and-tostring-with-jpa-and-hibernate/
     * - https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
     * - https://vladmihalcea.com/hibernate-facts-equals-and-hashcode/
     */

    /**
     *
     *
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
    private Integer stars;

    @Lob
    @Column(name = "additions", columnDefinition = "CLOB")
    @Convert(converter = MapToJsonConverter.class)
    private Map<String, String> additions = new HashMap<>();

    //No-Op
    public ReviewEntity() {
    }

    public ReviewEntity(Long productId, String author, String subject, String content, Integer stars, Map<String, String> additions) {
        this.productId = productId;
        this.author = author;
        this.subject = subject;
        this.content = content;
        this.stars = stars;
        this.additions = additions;
    }
}
