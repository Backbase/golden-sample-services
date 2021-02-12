package com.backbase.goldensample.store.service;

import com.backbase.goldensample.store.domain.Product;

public interface ProductClient {

    /**
     * Return a product by its id, null when not found.
     *
     * @param productId
     * @return
     */
    Product getProductById(long productId);

    long postProduct(Product product);

}
