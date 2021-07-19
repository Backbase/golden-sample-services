package com.backbase.goldensample.review.listener;

import com.backbase.buildingblocks.backend.communication.event.EnvelopedEvent;
import com.backbase.buildingblocks.backend.communication.event.handler.EventHandler;
import com.backbase.goldensample.review.dto.ReviewDTO;
import com.backbase.goldensample.review.mapper.EntityMapper;
import com.backbase.goldensample.review.service.ReviewService;
import com.backbase.goldensample.review.service.ThirdPartyReviewAggregatorService;
import com.backbase.product.event.spec.v1.ProductCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ProductCreateEventHandler implements EventHandler<ProductCreatedEvent> {

    private final ReviewService reviewService;
    private final ThirdPartyReviewAggregatorService thirdPartyReviewService;
    private final EntityMapper mapper;

    @Autowired
    public ProductCreateEventHandler(ReviewService reviewService,
        ThirdPartyReviewAggregatorService thirdPartyReviewService,
        EntityMapper mapper) {
        this.reviewService = reviewService;
        this.thirdPartyReviewService = thirdPartyReviewService;
        this.mapper = mapper;
    }

    @Override
    public void handle(EnvelopedEvent<ProductCreatedEvent> envelopedEvent) {
        ProductCreatedEvent event = envelopedEvent.getEvent();
        ReviewDTO dto = thirdPartyReviewService.getAverageReview(event.getName());
        dto.setProductId(Long.parseLong(event.getProductId()));

        log.debug("productCreateEvent - received event with productId: {}", event.getProductId());

        reviewService.createReview(dto);
    }
}
