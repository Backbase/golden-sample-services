package com.backbase.goldensample.review.listener;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;

import com.backbase.buildingblocks.backend.communication.event.EnvelopedEvent;
import com.backbase.buildingblocks.backend.communication.event.proxy.EventBus;
import com.backbase.goldensample.review.Application;
import com.backbase.goldensample.review.persistence.ReviewEntity;
import com.backbase.goldensample.review.persistence.ReviewRepository;
import com.backbase.product.event.spec.v1.ProductCreatedEvent;
import com.backbase.product.event.spec.v1.ProductDeletedEvent;
import java.util.Collections;
import java.util.List;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = {Application.class})
@AutoConfigureMockMvc
@ActiveProfiles("it")
@Transactional(propagation = NOT_SUPPORTED)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class EventHandlerIT {

    @Autowired
    private ReviewRepository repository;

    @Autowired
    EventBus eventBus;

    @Autowired
    ProductCreateEventHandler createEventHandler;

    @Autowired
    ProductDeleteEventHandler deleteEventHandlerEventHandler;

    @BeforeEach
    public void setup() {
        repository.deleteAll();
        ReviewEntity entity = new ReviewEntity(404L, "Joe Bloggs", "this item", "not bad", 5,
            Collections.singletonMap("verified", "true"));
        repository.save(entity);
    }

    @Test
    public void createProductEventConsumingTest() {

        eventBus.emitEvent(createProductCreatedEvent("1", "product1"));

        // check event received
        Awaitility.await().atMost(10, SECONDS).until(() -> !repository.findByProductId(1).isEmpty());
        List<ReviewEntity> reviews = repository.findByProductId(1);
        assertThat(reviews).hasSize(1);
        assertThat(reviews.get(0).getSubject()).isEqualTo("product1");
        assertThat(reviews.get(0).getAuthor()).isEqualTo("Store Admin");
    }

    @Test
    public void deleteProductEventConsumingTest() {

        List<ReviewEntity> reviews = repository.findByProductId(404);
        assertThat(reviews).hasSize(1);

        eventBus.emitEvent(createProductDeletedEvent("404"));

        // check event received
        Awaitility.await().atMost(10, SECONDS).until(() -> repository.findByProductId(404).isEmpty());
        reviews = repository.findByProductId(1);
        assertThat(reviews).isEmpty();
    }


    private EnvelopedEvent<ProductCreatedEvent> createProductCreatedEvent(String productId, String name) {
        EnvelopedEvent<ProductCreatedEvent> event = new EnvelopedEvent<>();
        ProductCreatedEvent createdEvent = new ProductCreatedEvent();
        createdEvent.setProductId(productId);
        createdEvent.setName(name);
        event.setEvent(createdEvent);
        return event;
    }

    private EnvelopedEvent<ProductDeletedEvent> createProductDeletedEvent(String productId) {
        EnvelopedEvent<ProductDeletedEvent> event = new EnvelopedEvent<>();
        ProductDeletedEvent deletedEvent = new ProductDeletedEvent();
        deletedEvent.setProductId(productId);
        event.setEvent(deletedEvent);
        return event;
    }

}
