package com.backbase.goldensample.store.service.extension;

import com.backbase.goldensample.store.domain.Product;
import com.backbase.goldensample.store.domain.Review;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Does a very naive implementation of censoring reviews for bad language
 */
@Component
@ConditionalOnProperty(matchIfMissing = true, value = "backbase.store.extensions.product-enricher.client.service-id")
public class NaiveProductEnricher implements ProductEnricher {

    // According to Google, these are the bad words.
    private List<String> badWords = List.of("damn", "jerk", "ugly", "stupid", "fart knocker");

    @Override
    public void enrichProduct(Product product) {
        product.getReviews().stream().forEach(this::censor);
    }

    private void censor(Review review) {
        badWords.forEach(word -> {
            review.setContent(StringUtils.replace(review.getContent(), word, "***"));
        });
    }
}
