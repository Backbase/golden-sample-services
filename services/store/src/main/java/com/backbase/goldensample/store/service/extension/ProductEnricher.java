package com.backbase.goldensample.store.service.extension;

import com.backbase.goldensample.store.domain.Product;

/**
 * Allow enriching the product (aggregation) using a service extension.
 *
 * <p>The pattern used here is not always necessary. Sometimes a simple if-statement can be enough. In this case,
 * there is simple, naive default behaviour that can be overriden using a web-hook extension.
 */
public interface ProductEnricher {

    void enrichProduct(Product product);

}
