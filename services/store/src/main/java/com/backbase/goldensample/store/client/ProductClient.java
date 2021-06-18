package com.backbase.goldensample.store.client;

import com.backbase.goldensample.store.domain.Product;
import java.util.Optional;

/**
 * Adapts the internal domain to the model of the Product api.
 */
public interface ProductClient {

    /**
     * Return a product by its id.
     *
     * @param productId
     * @return
     */
    Optional<Product> getProductById(long productId);

    /**
     * Create a new product.
     *
     * @param product
     * @return
     */
    long postProduct(Product product);

}
