package com.backbase.goldensample.store.api;

import com.backbase.buildingblocks.presentation.errors.NotFoundException;
import com.backbase.goldensample.product.api.client.v2.model.Product;
import com.backbase.goldensample.product.api.client.v2.model.ProductId;
import com.backbase.goldensample.review.api.client.v2.model.Review;
import com.backbase.goldensample.review.api.client.v2.model.ReviewId;
import com.backbase.goldensample.store.api.service.v2.ProductCompositeClientApi;
import com.backbase.goldensample.store.api.service.v2.model.ProductAggregate;
import com.backbase.goldensample.store.api.service.v2.model.ReviewSummary;
import com.backbase.goldensample.store.service.ProductCompositeService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class StoreClientApiController implements ProductCompositeClientApi {

    private final ProductCompositeService productCompositeService;

    @Autowired
    public StoreClientApiController(final ProductCompositeService productCompositeService) {
        this.productCompositeService = productCompositeService;
    }

    @Override
    public ResponseEntity<ProductAggregate> getProductById(final Long productId) {
        if (log.isDebugEnabled()) {
            log.debug("getCompositeProduct: lookup a product aggregate for productId: {}", productId);
        }

        final Product product = productCompositeService.getProduct(productId);
        if (product == null) {
            if (log.isDebugEnabled()) {
                log.debug("No product was found for id {}", productId);
            }
            throw new NotFoundException("No product found for productId: " + productId);
        }

        final List<Review> reviews = productCompositeService.getReviews(productId);

        if (log.isDebugEnabled()) {
            log.debug("getCompositeProduct: aggregate entity found for productId: {}", productId);
        }

        return ResponseEntity.ok(createProductAggregate(product, reviews));
    }

    @Override
    public ResponseEntity<ProductAggregate> postProduct(@Valid final ProductAggregate productAggregate) {

        if (log.isDebugEnabled()) {
            log.debug("createCompositeProduct: creates a new composite entity for productId: {}",
                productAggregate.getProductId());
        }

        final Product product = new Product()
            .name(productAggregate.getName())
            .weight(productAggregate.getWeight())
            .createDate(productAggregate.getCreateDate());
        final ProductId productId = productCompositeService.createProduct(product);
        product.productId(productId.getId());

        final List<Review> reviews = new ArrayList<>();
        if (productAggregate.getReviews() != null) {
            productAggregate
                .getReviews()
                .forEach(r -> {
                    final Review review =
                        new Review()
                            .productId(productId.getId())
                            .author(r.getAuthor())
                            .subject(r.getSubject())
                            .content(r.getContent());
                    final ReviewId reviewId = productCompositeService.createReview(review);
                    review.reviewId(reviewId.getId());
                    reviews.add(review);
                });
        }
        if (log.isDebugEnabled()) {
            log.debug("createCompositeProduct: composite entities created for productId: {}",
                productAggregate.getProductId());
        }

        return ResponseEntity.ok(createProductAggregate(product, reviews));

    }

    private ProductAggregate createProductAggregate(final Product product, final List<Review> reviews) {

        // 1. Setup product info
        final long productId = product.getProductId();
        final String name = product.getName();
        final int weight = product.getWeight();
        final LocalDate createDate = product.getCreateDate();

        // 2. Copy summary review info, if available
        final List<ReviewSummary> reviewSummaries = (reviews == null) ? null :
            reviews
                .stream()
                .map(r -> new ReviewSummary()
                    .reviewId(r.getReviewId())
                    .author(r.getAuthor())
                    .subject(r.getSubject())
                    .content(r.getContent()))
                .collect(Collectors.toList());

        return new ProductAggregate()
            .productId(productId)
            .name(name)
            .weight(weight)
            .reviews(reviewSummaries)
            .createDate(createDate);
    }
}
