package com.backbase.goldensample.review.listener;

import com.backbase.buildingblocks.backend.communication.event.EnvelopedEvent;
import com.backbase.buildingblocks.backend.communication.event.handler.EventHandler;
import com.backbase.goldensample.review.mapper.EventMapper;
import com.backbase.goldensample.review.persistence.ReviewEntity;
import com.backbase.goldensample.review.service.ReviewService;
import com.backbase.product.event.spec.v1.ProductCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ProductCreateEventHandler implements EventHandler<ProductCreatedEvent> {

    private final ReviewService reviewService;
    private final EventMapper eventMapper;

    @Autowired
    public ProductCreateEventHandler(ReviewService reviewService, EventMapper eventMapper) {
        this.reviewService = reviewService;
        this.eventMapper = eventMapper;
    }

    @Override
    public void handle(EnvelopedEvent<ProductCreatedEvent> envelopedEvent) {
        ProductCreatedEvent event = envelopedEvent.getEvent();
        ReviewEntity entity = eventMapper.eventToEntity(event);

        log.debug("productCreateEvent - received event with productId: {}", event.getProductId());

        reviewService.createReview(entity);
    }
}
