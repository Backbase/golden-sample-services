package com.backbase.goldensample.review.persistence;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


import java.util.Collections;
import org.junit.jupiter.api.Test;

class ReviewEntityTest {

    @Test
    void testMandatoryFields() {

        assertAll(
            () -> assertThrows(NullPointerException.class,
                () -> new ReviewEntity(1L, null, "author", "subject", "content", Collections.singletonMap("verified","true"))),
            () -> assertThrows(NullPointerException.class, () -> new ReviewEntity(1L, 1L, null, "subject", "content", Collections.singletonMap("verified","true"))),
            () -> assertThrows(NullPointerException.class, () -> new ReviewEntity(1L, 1L, "author", null, "content", Collections.singletonMap("verified","true"))),
            () -> assertThrows(NullPointerException.class, () -> new ReviewEntity(1L, 1L, "author", "subject", null, Collections.singletonMap("verified","true"))),
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

        assertAll(
            () -> assertEquals(1L, reviewEntity.getProductId()),
            () -> assertEquals("author", reviewEntity.getAuthor()),
            () -> assertEquals("subject", reviewEntity.getSubject()),
            () -> assertEquals("content", reviewEntity.getContent()));
    }

}
