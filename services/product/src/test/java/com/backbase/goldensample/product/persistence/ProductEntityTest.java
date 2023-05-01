package com.backbase.goldensample.product.persistence;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;
class ProductEntityTest {

    /**
     * Validates the equality of this Entity class in different states and with different data. Due to the definition of
     * our Entity, 3 suppress warnings are required in order to validate it as explained in <a
     * href="https://jqno.nl/equalsverifier/manual/jpa-entities/">here</a>.
     */
    @Test
    void validateEntityEqualityAmongStates() {
        EqualsVerifier.forClass(ProductEntity.class)
                .suppress(Warning.IDENTICAL_COPY_FOR_VERSIONED_ENTITY)
                .suppress(Warning.STRICT_HASHCODE)
                .suppress(Warning.SURROGATE_KEY)
                .verify();
    }


}