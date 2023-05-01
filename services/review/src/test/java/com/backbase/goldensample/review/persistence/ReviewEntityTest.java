package com.backbase.goldensample.review.persistence;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import java.util.Collections;
import org.junit.jupiter.api.Test;

class ReviewEntityTest {

    @Test
    void testMandatoryFields() {

        assertAll(
            () -> assertThrows(NullPointerException.class,
                () -> new ReviewEntity(1L, null, "author", "subject", "content", 5, Collections.singletonMap("verified","true"))),
            () -> assertThrows(NullPointerException.class, () -> new ReviewEntity(1L, 1L, null, "subject", "content", 5, Collections.singletonMap("verified","true"))),
            () -> assertThrows(NullPointerException.class, () -> new ReviewEntity(1L, 1L, "author", null, "content", 5, Collections.singletonMap("verified","true"))),
            () -> assertThrows(NullPointerException.class, () -> new ReviewEntity(1L, 1L, "author", "subject", null, 5, Collections.singletonMap("verified","true"))),
            () -> assertThrows(NullPointerException.class, () -> new ReviewEntity().setProductId(null)),
            () -> assertThrows(NullPointerException.class, () -> new ReviewEntity().setAuthor(null)),
            () -> assertThrows(NullPointerException.class, () -> new ReviewEntity().setSubject(null)),
            () -> assertThrows(NullPointerException.class, () -> new ReviewEntity().setContent(null)),
            () -> assertThrows(NullPointerException.class, () -> ReviewEntity.builder().productId(null)),
            () -> assertThrows(NullPointerException.class, () -> ReviewEntity.builder().author(null)),
            () -> assertThrows(NullPointerException.class, () -> ReviewEntity.builder().subject(null)),
            () -> assertThrows(NullPointerException.class, () -> ReviewEntity.builder().content(null))
            );
    }

    @Test
    void testSetMandatoryFields() {
        ReviewEntity reviewEntity = new ReviewEntity();
        reviewEntity.setProductId(1L);
        reviewEntity.setAuthor("author");
        reviewEntity.setSubject("subject");
        reviewEntity.setContent("content");
        reviewEntity.setStars(4);

        assertAll(
            () -> assertEquals(1L, reviewEntity.getProductId()),
            () -> assertEquals("author", reviewEntity.getAuthor()),
            () -> assertEquals("subject", reviewEntity.getSubject()),
            () -> assertEquals("content", reviewEntity.getContent()),
            () -> assertEquals(4, reviewEntity.getStars()));
    }

    /**
     * Validates the equality of this Entity class in different states and with different data. Due to the definition of
     * our Entity, 2 suppress warnings are required in order to validate it as explained in <a
     * href="https://jqno.nl/equalsverifier/manual/jpa-entities/">here</a>.
     */
    @Test
    void validateEntityEqualityAmongStates() {
        EqualsVerifier.forClass(ReviewEntity.class)
                .suppress(Warning.IDENTICAL_COPY_FOR_VERSIONED_ENTITY)
                .suppress(Warning.STRICT_HASHCODE)
                .suppress(Warning.SURROGATE_KEY)
                .verify();
    }

}
