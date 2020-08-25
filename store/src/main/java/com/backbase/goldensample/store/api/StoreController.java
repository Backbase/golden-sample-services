package com.backbase.goldensample.store.api;

import com.backbase.buildingblocks.presentation.errors.NotFoundException;
import com.backbase.goldensample.product.api.client.v2.model.Product;
import com.backbase.goldensample.review.api.client.v2.model.Review;
import com.backbase.goldensample.store.api.service.v2.ProductCompositeClientImplApi;
import com.backbase.goldensample.store.api.service.v2.model.ProductAggregate;
import com.backbase.goldensample.store.api.service.v2.model.ReviewSummary;
import com.backbase.goldensample.store.service.ProductCompositeService;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StoreController implements ProductCompositeClientImplApi {

    private static final Logger LOG = LoggerFactory.getLogger(StoreController.class);

    private ProductCompositeService integration;

    @Autowired
    public StoreController(ProductCompositeService integration) {
        this.integration = integration;
    }


    @Override
    public ResponseEntity<ProductAggregate> getProductUsingGET(Long productId) {
        LOG.debug("getCompositeProduct: lookup a product aggregate for productId: {}", productId);

        Product product = integration.getProduct(productId);
        if (product == null) throw new NotFoundException("No product found for productId: " + productId);

        List<Review> reviews = integration.getReviews(productId);

        LOG.debug("getCompositeProduct: aggregate entity found for productId: {}", productId);

        return ResponseEntity.ok(createProductAggregate(product, reviews));
    }

    private ProductAggregate createProductAggregate(Product product, List<Review> reviews) {

        // 1. Setup product info
        long productId = product.getProductId();
        String name = product.getName();
        int weight = product.getWeight();

        // 2. Copy summary review info, if available
        List<ReviewSummary> reviewSummaries = (reviews == null)  ? null :
            reviews.stream()
                .map(r -> new ReviewSummary().reviewId(r.getReviewId()).author(r.getAuthor()).subject(r.getSubject()).content(r.getContent()))
                .collect(Collectors.toList());

        return new ProductAggregate().productId(productId).name(name).weight(weight).reviews(reviewSummaries);
    }
}
