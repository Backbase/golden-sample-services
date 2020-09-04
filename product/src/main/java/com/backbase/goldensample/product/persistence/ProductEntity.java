package com.backbase.goldensample.product.persistence;

import java.time.Instant;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

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

  @Id
  @GeneratedValue
  private Long id;

  private String name;

  private Integer weight;

  private Instant createDate;

  public ProductEntity() {
  }

  public ProductEntity(String name, Integer weight, Instant createDate) {
    this.name = name;
    this.weight = weight;
    this.createDate = createDate;
  }
}
