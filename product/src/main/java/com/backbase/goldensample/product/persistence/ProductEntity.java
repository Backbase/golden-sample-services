package com.backbase.goldensample.product.persistence;

import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "product")
@Getter
@Setter
@Builder
@AllArgsConstructor
public class ProductEntity {

    /*
     * IMPORTANT:
     * Set toString, equals, and hashCode as described in these
     * documents:
     * - https://vladmihalcea.com/the-best-way-to-implement-equals-hashcode-and-tostring-with-jpa-and-hibernate/
     * - https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
     * - https://vladmihalcea.com/hibernate-facts-equals-and-hashcode/
     */

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

    private String name;

    private Integer weight;

  private LocalDate createDate;

    public ProductEntity() {
    }

  public ProductEntity(String name, Integer weight, LocalDate createDate) {
    this.name = name;
    this.weight = weight;
    this.createDate = createDate;
  }

}
