package com.backbase.goldensample.store.api;

import static org.springframework.http.ResponseEntity.created;

import com.backbase.buildingblocks.presentation.errors.NotFoundException;
import com.backbase.goldensample.product.api.client.v2.model.Product;
import com.backbase.goldensample.product.api.client.v2.model.ProductId;
import com.backbase.goldensample.review.api.client.v2.model.Review;
import com.backbase.goldensample.review.api.client.v2.model.ReviewId;
import com.backbase.goldensample.store.api.service.v2.ProductCompositeClientApi;
import com.backbase.goldensample.store.api.service.v2.model.ProductAggregate;
import com.backbase.goldensample.store.api.service.v2.model.ReviewSummary;
import com.backbase.goldensample.store.service.ProductCompositeServiceImpl;
import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

@RestController
@Slf4j
public class StoreClientApiController implements ProductCompositeClientApi {

    private final ProductCompositeServiceImpl productCompositeService;

    private final NativeWebRequest nativeWebRequest;

    @Autowired
    public StoreClientApiController(final ProductCompositeServiceImpl productCompositeService,
        final NativeWebRequest nativeWebRequest) {
        this.productCompositeService = productCompositeService;
        this.nativeWebRequest = nativeWebRequest;
    }

    @Override
    public ResponseEntity<ProductAggregate> getProductById(final Long productId) {
        if (log.isDebugEnabled()) {
            log.debug("getCompositeProduct: lookup a product aggregate for productId: {}", productId);
        }
        return Optional
            .ofNullable(productCompositeService.getProduct(productId))
            .map(product -> {
                log.debug("getCompositeProduct: aggregate entity found for productId: {}", productId);
                return createProductAggregate()
                    .apply(Tuples.of(product, productCompositeService.getReviews(productId)));
            })
            .map(ResponseEntity::ok)
            .orElseThrow(() -> new NotFoundException("No product found for productId: " + productId));
    }

    @Override
    public ResponseEntity<ProductAggregate> postProduct(@Valid final ProductAggregate productAggregate) {
        log.debug("createCompositeProduct: creates a new composite entity for productId: {}",
            productAggregate.getProductId());

        final Tuple2<Product, List<Review>> product = createProductFromAggregate()
            .andThen(createReviewList())
            .apply(productAggregate);

        log.debug("createCompositeProduct: composite entities created for productId: {}",
            productAggregate.getProductId());

        ServletUriComponentsBuilder.fromCurrentRequest()
            .pathSegment(product
                .getT1()
                .getProductId().toString())
            .build()
            .toUri()
            .getPath();

        return created(
            URI.create(nativeWebRequest.getNativeRequest(HttpServletRequest.class).getRequestURI() + "/" + product
                .getT1()
                .getProductId()))
            .body(createProductAggregate().apply(product));
    }

    private Function<ProductAggregate, Tuple2<Product, ProductAggregate>> createProductFromAggregate() {
        return productAggregate -> {
            final Product product = new Product()
                .name(productAggregate.getName())
                .weight(productAggregate.getWeight())
                .createDate(Optional
                    .ofNullable(productAggregate.getCreateDate())
                    .orElseGet(LocalDate::now));
            final ProductId productId = productCompositeService.createProduct(product);
            product.productId(productId.getId());
            return Tuples.of(product, productAggregate);
        };
    }

    private Function<Tuple2<Product, ProductAggregate>, Tuple2<Product, List<Review>>> createReviewList() {
        return tuple -> Tuples.of(tuple.getT1(), Optional
            .ofNullable(tuple
                .getT2()
                .getReviews())
            .map(revws -> revws
                .stream()
                .map(review -> {
                    final Review newReview =
                        new Review()
                            .productId(tuple
                                .getT1()
                                .getProductId())
                            .author(review.getAuthor())
                            .subject(review.getSubject())
                            .content(review.getContent());
                    final ReviewId reviewId = productCompositeService.createReview(newReview);
                    newReview.reviewId(reviewId.getId());
                    return newReview;
                })
                .collect(Collectors.toList())
            )
            .orElseGet(ArrayList::new));
    }

    private Function<Tuple2<Product, List<Review>>, ProductAggregate> createProductAggregate() {
        return tuple -> {
            // 1. Setup product info
            final long productId = Optional
                .ofNullable(tuple
                    .getT1()
                    .getProductId())
                .orElseThrow(() -> new RuntimeException(""));
            final String name = tuple
                .getT1()
                .getName();
            final int weight = Optional
                .ofNullable(tuple
                    .getT1()
                    .getWeight())
                .orElseThrow(() -> new RuntimeException(""));
            final LocalDate createDate = tuple
                .getT1()
                .getCreateDate();

            // 2. Copy summary review info, if available
            final List<ReviewSummary> reviewSummaries = tuple
                .getT2()
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
        };
    }
}
