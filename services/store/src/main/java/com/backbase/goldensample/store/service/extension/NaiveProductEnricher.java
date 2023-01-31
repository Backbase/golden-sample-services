package com.backbase.goldensample.store.service.extension;

import com.backbase.goldensample.store.domain.Product;
import com.backbase.goldensample.store.domain.Review;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * This class implements the default ("out of the box" :beer:) behaviour that can be overriden using the web-hook
 * extension. There might not always be "default" behaviour. Would there not be default enrichment in this case,
 * a NoopProductEnricher would have been created that would implement enrichProduct as
 * <pre>{@code
 *     @Override
 *     public void enrichProduct(Product product) {
 *         // Do nothing.
 *     }
 * }</pre>
 */
@Component
@ConditionalOnProperty(
        value = "backbase.store.extensions.product-enricher.enabled",
        havingValue = "false",
        matchIfMissing = true)
public class NaiveProductEnricher implements ProductEnricher {

    // According to Google, these are the bad words.
    private List<String> badWords = List.of("damn", "jerk", "ugly", "stupid", "fart knocker");

    @Override
    public void enrichProduct(Product product) {
        product.getReviews().forEach(this::censor);
    }

    private void censor(Review review) {
        badWords.forEach(word -> review.setContent(StringUtils.replace(review.getContent(), word, "***")));
    }
}
