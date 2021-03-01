package com.backbase.goldensample.product.persistence;

import com.backbase.goldensample.product.config.IdentityStrategyOverrideConfiguration;
import com.backbase.goldensample.product.mapper.MapToJsonConverter;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "product")
@Getter
@Setter
@Builder
@AllArgsConstructor
public class ProductEntity {

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

    @Lob
    @Column(name = "additions", columnDefinition = "CLOB")
    @Convert(converter = MapToJsonConverter.class)
    private Map<String, String> additions = new HashMap<>();

    //No-Op
    public ProductEntity() {
    }

    public ProductEntity(String name, Integer weight, LocalDate createDate, Map<String, String> additions) {
        this.name = name;
        this.weight = weight;
        this.createDate = createDate;
        this.additions = additions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProductEntity that = (ProductEntity) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("id", id)
            .append("name", name)
            .append("weight", weight)
            .append("createDate", createDate)
            .append("additions", additions)
            .toString();
    }
}
