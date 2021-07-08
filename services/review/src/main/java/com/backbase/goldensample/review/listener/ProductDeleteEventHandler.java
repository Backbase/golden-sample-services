package com.backbase.goldensample.review.listener;

import com.backbase.buildingblocks.backend.communication.event.EnvelopedEvent;
import com.backbase.buildingblocks.backend.communication.event.handler.EventHandler;
import com.backbase.goldensample.review.v2.api.ReviewServiceApiController;
import com.backbase.product.event.spec.v1.ProductDeletedEvent;
import org.springframework.stereotype.Component;

@Component
public class ProductDeleteEventHandler implements EventHandler<ProductDeletedEvent> {

    ReviewServiceApiController reviewService;

    @Override
    public void handle(EnvelopedEvent<ProductDeletedEvent> envelopedEvent) {
        ProductDeletedEvent event = envelopedEvent.getEvent();
        reviewService.deleteReviewsByProductId(Long.parseLong(event.getProductId()));
    }
}
