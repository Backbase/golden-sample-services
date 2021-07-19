package com.backbase.goldensample.review.listener;

import com.backbase.buildingblocks.backend.communication.event.EnvelopedEvent;
import com.backbase.buildingblocks.backend.communication.event.handler.EventHandler;
import com.backbase.goldensample.review.service.ReviewService;
import com.backbase.product.event.spec.v1.ProductDeletedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ProductDeleteEventHandler implements EventHandler<ProductDeletedEvent> {

    private final ReviewService reviewService;

    @Autowired
    public ProductDeleteEventHandler(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @Override
    public void handle(EnvelopedEvent<ProductDeletedEvent> envelopedEvent) {
        ProductDeletedEvent event = envelopedEvent.getEvent();

        log.debug("productDeleteEvent - received event with productId: {}", event.getProductId());

        reviewService.deleteReviews(Long.parseLong(event.getProductId()));
    }
}
