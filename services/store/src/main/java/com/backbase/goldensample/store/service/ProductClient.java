package com.backbase.goldensample.store.service;

import com.backbase.goldensample.store.domain.Product;

/**
 * Adapts the internal domain to the model of the Product api.
 */
public interface ProductClient {

    /**
     * Return a product by its id, null when not found.
     *
     * @param productId
     * @return
     */
    Product getProductById(long productId);

    /**
     * Create a new product.
     *
     * @param product
     * @return
     */
    long postProduct(Product product);

}
