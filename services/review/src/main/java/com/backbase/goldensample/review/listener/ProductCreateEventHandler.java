package com.backbase.goldensample.review.listener;

import com.backbase.buildingblocks.backend.communication.event.EnvelopedEvent;
import com.backbase.buildingblocks.backend.communication.event.handler.EventHandler;
import com.backbase.goldensample.review.v2.api.ReviewServiceApiController;
import com.backbase.product.event.spec.v1.ProductCreatedEvent;
import com.backbase.reviews.api.service.v2.model.Review;
import org.springframework.stereotype.Component;

@Component
public class ProductCreateEventHandler implements EventHandler<ProductCreatedEvent> {

    ReviewServiceApiController reviewService;

    @Override
    public void handle(EnvelopedEvent<ProductCreatedEvent> envelopedEvent) {
        ProductCreatedEvent event = envelopedEvent.getEvent();
        com.backbase.reviews.api.service.v2.model.Review reviewEntity = new Review();
        reviewEntity.setProductId(Long.valueOf(event.getProductId()));
        reviewEntity.setAuthor("anonymous");
        reviewEntity.setStars(5);
        reviewEntity.setSubject(event.getName());
        reviewEntity.setContent("Great!");
        reviewService.postReview(reviewEntity);
    }
}
