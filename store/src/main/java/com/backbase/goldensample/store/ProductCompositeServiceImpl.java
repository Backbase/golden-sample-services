package com.backbase.goldensample.store;

import com.backbase.goldensample.store.api.service.v2.ProductCompositeClientImplApi;
import com.backbase.goldensample.store.api.service.v2.model.ProductAggregate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductCompositeServiceImpl implements ProductCompositeClientImplApi {

    private static final Logger LOG = LoggerFactory.getLogger(ProductCompositeServiceImpl.class);

    private ProductCompositeIntegration integration;

    @Autowired
    public ProductCompositeServiceImpl(ProductCompositeIntegration integration) {
        this.integration = integration;
    }


    @Override
    public ResponseEntity<ProductAggregate> getProductUsingGET(Integer productId) {
        LOG.debug("getCompositeProduct: lookup a product aggregate for productId: {}", productId);

//        Product product = integration.getProduct(productId);
//        if (product == null) throw new NotFoundException("No product found for productId: " + productId);
//
//        List<Recommendation> recommendations = integration.getRecommendations(productId);
//
//        List<Review> reviews = integration.getReviews(productId);
//
//        LOG.debug("getCompositeProduct: aggregate entity found for productId: {}", productId);
//
//        return createProductAggregate(product, recommendations, reviews, serviceUtil.getServiceAddress());

        return ResponseEntity.ok(new ProductAggregate());
    }

}
