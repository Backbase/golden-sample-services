package com.backbase.goldensample.review.eventhandlers;

import com.backbase.buildingblocks.backend.communication.event.EnvelopedEvent;
import com.backbase.buildingblocks.backend.communication.event.handler.EventHandler;
import com.backbase.goldensample.review.service.ReviewServiceImpl;
import com.backbase.product.event.spec.v1.ProductDeletedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductDeleteEventHandler implements EventHandler<ProductDeletedEvent> {
    ReviewServiceImpl reviewService;

    @Autowired
    public ProductDeleteEventHandler(ReviewServiceImpl reviewService) {
        this.reviewService = reviewService;
    }
    @Override
    public void handle(EnvelopedEvent<ProductDeletedEvent> envelopedEvent) {
        ProductDeletedEvent event = envelopedEvent.getEvent();
        reviewService.deleteReviews(Long.parseLong(event.getProductId()));

    }
}
