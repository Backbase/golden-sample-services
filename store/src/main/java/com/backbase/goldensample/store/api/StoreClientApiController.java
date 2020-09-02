package com.backbase.goldensample.store.api;

import com.backbase.buildingblocks.presentation.errors.NotFoundException;
import com.backbase.goldensample.product.api.client.v2.model.Product;
import com.backbase.goldensample.product.api.client.v2.model.ProductId;
import com.backbase.goldensample.review.api.client.v2.model.Review;
import com.backbase.goldensample.review.api.client.v2.model.ReviewId;
import com.backbase.goldensample.store.api.service.v2.ProductCompositeClientImplApi;
import com.backbase.goldensample.store.api.service.v2.model.ProductAggregate;
import com.backbase.goldensample.store.api.service.v2.model.ReviewSummary;
import com.backbase.goldensample.store.service.ProductCompositeService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StoreClientApiController implements ProductCompositeClientImplApi {

    private static final Logger LOG = LoggerFactory.getLogger(StoreClientApiController.class);

    private ProductCompositeService productCompositeService;

    @Autowired
    public StoreClientApiController(ProductCompositeService productCompositeService) {
        this.productCompositeService = productCompositeService;
    }


    @Override
    public ResponseEntity<ProductAggregate> getProductUsingGET(Long productId) {
        LOG.debug("getCompositeProduct: lookup a product aggregate for productId: {}", productId);

        Product product = productCompositeService.getProduct(productId);
        if (product == null) {
            throw new NotFoundException("No product found for productId: " + productId);
        }

        List<Review> reviews = productCompositeService.getReviews(productId);

        LOG.debug("getCompositeProduct: aggregate entity found for productId: {}", productId);

        return ResponseEntity.ok(createProductAggregate(product, reviews));
    }

    @Override
    public ResponseEntity<ProductAggregate> postProduct(@Valid ProductAggregate productAggregate) {

        LOG.debug("createCompositeProduct: creates a new composite entity for productId: {}",
            productAggregate.getProductId());

        Product product = new Product().name(productAggregate.getName()).weight(productAggregate.getWeight());
        ProductId productId = productCompositeService.createProduct(product);
        product.productId(productId.getId());

        List<Review> reviews = new ArrayList<>();
        if (productAggregate.getReviews() != null) {
            productAggregate.getReviews().forEach(r -> {
                Review review =
                    new Review().productId(productId.getId()).author(r.getAuthor()).subject(r.getSubject())
                        .content(r.getContent());
                ReviewId reviewId = productCompositeService.createReview(review);
                review.reviewId(reviewId.getId());
                reviews.add(review);
            });
        }
        LOG.debug("createCompositeProduct: composite entities created for productId: {}",
            productAggregate.getProductId());

        return ResponseEntity.ok(createProductAggregate(product, reviews));

    }

    private ProductAggregate createProductAggregate(Product product, List<Review> reviews) {

        // 1. Setup product info
        long productId = product.getProductId();
        String name = product.getName();
        int weight = product.getWeight();
        Date createDate = product.getCreateDate();

        // 2. Copy summary review info, if available
        List<ReviewSummary> reviewSummaries = (reviews == null) ? null :
            reviews.stream()
                .map(r -> new ReviewSummary().reviewId(r.getReviewId()).author(r.getAuthor()).subject(r.getSubject())
                    .content(r.getContent()))
                .collect(Collectors.toList());

        return new ProductAggregate().productId(productId).name(name).weight(weight).reviews(reviewSummaries)
            .createDate(createDate);
    }
}
