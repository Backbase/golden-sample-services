package com.backbase.goldensample.store.api;

import com.backbase.buildingblocks.presentation.errors.NotFoundException;
import com.backbase.goldensample.product.api.client.v2.model.Product;
import com.backbase.goldensample.product.api.client.v2.model.ProductId;
import com.backbase.goldensample.review.api.client.v2.model.Review;
import com.backbase.goldensample.review.api.client.v2.model.ReviewId;
import com.backbase.goldensample.store.api.service.v2.ProductCompositeClientApi;
import com.backbase.goldensample.store.api.service.v2.model.ProductAggregate;
import com.backbase.goldensample.store.api.service.v2.model.ReviewSummary;
import com.backbase.goldensample.store.config.StoreViewConfig;
import com.backbase.goldensample.store.service.ProductCompositeService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class StoreClientApiController implements ProductCompositeClientApi {

    private ProductCompositeService productCompositeService;

    @Autowired
    private StoreViewConfig storeViewConfig;

    @Autowired
    public StoreClientApiController(ProductCompositeService productCompositeService) {
        this.productCompositeService = productCompositeService;
    }

    @Override
    public ResponseEntity<ProductAggregate> getProductById(Long productId) {
        log.debug("getCompositeProduct: lookup a product aggregate for productId: {}", productId);

        Product product = productCompositeService.getProduct(productId);
        if (product == null) {
            log.debug("No product was found for id {}", productId);
            throw new NotFoundException("No product found for productId: " + productId);
        }

        List<Review> reviews = productCompositeService.getReviews(productId);

        log.debug("getCompositeProduct: aggregate entity found for productId: {}", productId);

        return createResponse(product, reviews);
    }


    @Override
    public ResponseEntity<ProductAggregate> postProduct(@Valid ProductAggregate productAggregate) {

        log.debug("createCompositeProduct: creates a new composite entity for productId: {}",
            productAggregate.getProductId());

        Product product = new Product().name(productAggregate.getName()).weight(productAggregate.getWeight()).createDate(productAggregate.getCreateDate()).additions(productAggregate.getAdditions());
        ProductId productId = productCompositeService.createProduct(product);
        product.productId(productId.getId());

        List<Review> reviews = new ArrayList<>();
        if (productAggregate.getReviews() != null) {
            productAggregate.getReviews().forEach(r -> {
                Review review =
                    new Review().productId(productId.getId()).author(r.getAuthor()).subject(r.getSubject())
                        .content(r.getContent()).additions(r.getAdditions());
                ReviewId reviewId = productCompositeService.createReview(review);
                review.reviewId(reviewId.getId());
                reviews.add(review);
            });
        }
        log.debug("createCompositeProduct: composite entities created for productId: {}",
            productAggregate.getProductId());

        return createResponse(product, reviews);
    }

    private ResponseEntity<ProductAggregate> createResponse(Product product, List<Review> reviews) {

        return ResponseEntity.ok()
            .header(StoreViewConfig.STORE_THEME_RESPONSE_HEADER_NAME, storeViewConfig.getTheme())
            .body(createProductAggregate(product, reviews));
    }

    private ProductAggregate createProductAggregate(Product product, List<Review> reviews) {

        // 1. Setup product info
        long productId = product.getProductId();
        String name = product.getName();
        int weight = product.getWeight();
        LocalDate createDate = product.getCreateDate();
        Map<String, String> additions = product.getAdditions();

        // 2. Copy summary review info, if available
        List<ReviewSummary> reviewSummaries = (reviews == null) ? null :
            reviews.stream()
                .map(r -> new ReviewSummary().reviewId(r.getReviewId()).author(r.getAuthor()).subject(r.getSubject())
                    .content(r.getContent()).additions(r.getAdditions()))
                .collect(Collectors.toList());

        return new ProductAggregate().productId(productId).name(name).weight(weight).reviews(reviewSummaries)
            .createDate(createDate).additions(additions);
    }

}
