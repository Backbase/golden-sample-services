package com.backbase.goldensample.store.service.extension;

import com.backbase.goldensample.store.domain.Product;
import com.backbase.goldensample.store.domain.Review;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NaiveProductEnricherTest {

    private NaiveProductEnricher enricher = new NaiveProductEnricher();

    @Test
    void shouldReplaceBadWords() {
        String badReview = "this damn jerk is an ugly stupid fart knocker";
        Product p = new Product();
        Review r = new Review();
        r.setContent(badReview);
        p.setReviews(List.of(r));
        enricher.enrichProduct(p);

        assertEquals("this *** *** is an *** *** ***", r.getContent());
    }

}