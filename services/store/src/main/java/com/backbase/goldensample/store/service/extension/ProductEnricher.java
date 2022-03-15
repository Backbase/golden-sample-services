package com.backbase.goldensample.store.service.extension;

import com.backbase.goldensample.store.domain.Product;

/**
 * Allow enriching the product (aggregation) using a service extension.
 *
 * <p>The pattern used here is not always necessary. Sometimes maybe a simpler if-statement can be enough. In this
 * case, there is simple, naive default behaviour that can be overridden using a web-hook extension by implementing a
 * service to expose a <code>/service-api/v1/web-hooks/product-composites/enrich</code> endpoint as defined in the
 * <code>store-integration-enricher-api</code> and configuring the store service to invoke it.
 */
public interface ProductEnricher {

    /**
     * Invoked before returning a product with its reviews to the client.
     *
     * @param product The product with its reviews to be enriched/rewritten/etc.
     */
    void enrichProduct(Product product);

}
