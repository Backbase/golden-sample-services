package com.backbase.goldensample.product.service;

import com.backbase.buildingblocks.backend.communication.context.OriginatorContextUtil;
import com.backbase.buildingblocks.backend.communication.event.EnvelopedEvent;
import com.backbase.buildingblocks.backend.communication.event.proxy.EventBus;
import com.backbase.buildingblocks.presentation.errors.NotFoundException;
import com.backbase.goldensample.product.mapper.ProductMapper;
import com.backbase.goldensample.product.persistence.ProductRepository;
import com.backbase.product.api.service.v2.model.Product;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;

    private final ProductMapper mapper;
    private final Random randomNumberGenerator = new Random();

    private final EventBus eventBus;
    private final OriginatorContextUtil originatorContextUtil;

    @Autowired
    public ProductServiceImpl(ProductRepository repository, ProductMapper mapper, EventBus eventBus, OriginatorContextUtil originatorContextUtil) {
        this.repository = repository;
        this.mapper = mapper;
        this.eventBus = eventBus;
        this.originatorContextUtil = originatorContextUtil;
    }

    @Override
    public Product createProduct(Product body) {
        log.debug("Service creating product {}", body);
        Product product = mapper.entityToApi(repository
            .save(mapper.apiToEntity(body)));
        com.backbase.product.event.spec.v1.ProductCreatedEvent event = new com.backbase.product.event.spec.v1.ProductCreatedEvent();
        event.setName(product.getName());
        event.setProductId(product.getProductId().toString());
        event.setWeight(product.getWeight().toString());
        event.setCreateDateAsLocalDate(product.getCreateDate());
        EnvelopedEvent<com.backbase.product.event.spec.v1.ProductCreatedEvent> envelopedEvent = new EnvelopedEvent<>();
        envelopedEvent.setEvent(event);
        eventBus.emitEvent(envelopedEvent);

        return product;
    }

    @Override
    public Product updateProduct(Product body) {
        log.debug("Service updating product {}", body);
        return mapper.entityToApi(repository
            .save(mapper.apiToEntity(body)));
    }

    @Override
    public Product getProduct(long productId, int delay, int faultPercent) {
        if (delay > 0) {
            log.debug("Simulating delay of {}", delay);
            simulateDelay(delay);
        }

        if (faultPercent > 0) {
            log.debug("Introducing error in a %{}", faultPercent);
            throwErrorIfBadLuck(faultPercent);
        }

        return repository.findById(productId).map(mapper::entityToApi)
            .orElseThrow(() -> new NotFoundException(String.format("Item is not found with id : '%s'", productId)));
    }

    @Override
    public List<Product> getAllProducts() {
        //TODO add pagination
        return mapper.entityListToApiList(repository.findAll());
    }

    /*
     Implementation is idempotent, that is,
     it will not report any failure if the entity is not found Always 200
    */
    @Override
    public void deleteProduct(long productId) {

        log.debug("deleteProduct: tries to delete an entity with productId: {}", productId);

        repository.findById(productId).ifPresent(repository::delete);
    }

    private void simulateDelay(int delay) {
        log.debug("Sleeping for {} seconds...", delay);
        try {
            Thread.sleep(Duration.ofSeconds(delay).toMillis());
        } catch (InterruptedException ignored) {
        }
        log.debug("Moving on...");
    }

    private void throwErrorIfBadLuck(int faultPercent) {
        int randomThreshold = getRandomNumber(1, 100);
        if (faultPercent < randomThreshold) {
            log.debug("We got lucky, no error occurred, {} < {}", faultPercent, randomThreshold);
        } else {
            log.debug("Bad luck, an error occurred, {} >= {}", faultPercent, randomThreshold);
            throw new RuntimeException("Something went wrong...");
        }
    }

    private int getRandomNumber(int min, int max) {

        if (max < min) {
            log.warn("Max value {} show be greater than min {}", max, min);
            throw new RuntimeException("Max must be greater than min");
        }

        return randomNumberGenerator.nextInt((max - min) + 1) + min;
    }
}
