package com.backbase.goldensample.review.eventhandlers;

import com.backbase.buildingblocks.backend.communication.event.EnvelopedEvent;
import com.backbase.buildingblocks.backend.communication.event.handler.EventHandler;
import com.backbase.goldensample.review.persistence.ReviewEntity;
import com.backbase.goldensample.review.service.ReviewServiceImpl;
import com.backbase.product.event.spec.v1.ProductCreatedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductCreateEventHandler implements EventHandler<ProductCreatedEvent> {

    ReviewServiceImpl reviewService;

    @Autowired
    public ProductCreateEventHandler(ReviewServiceImpl reviewService) {
        this.reviewService = reviewService;
    }

    @Override
    public void handle(EnvelopedEvent<ProductCreatedEvent> envelopedEvent) {
        ProductCreatedEvent event = envelopedEvent.getEvent();
        ReviewEntity reviewEntity = new ReviewEntity();
        reviewEntity.setProductId(Long.valueOf(event.getProductId()));
        reviewEntity.setAuthor("anonymous");
        reviewEntity.setStars(5);
        reviewEntity.setSubject(event.getName());
        reviewEntity.setContent("Great!");
        reviewService.createReview(reviewEntity);
    }
}
