package com.backbase.goldensample.product.service;

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
    private final Random        randomNumberGenerator = new Random();

    @Autowired
    public ProductServiceImpl(final ProductRepository repository, final ProductMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Product createProduct(final Product body) {
        if (log.isDebugEnabled()) {
            log.debug("Service creating product {}", body);
        }
        return mapper.entityToApi(repository
            .save(mapper.apiToEntity(body)));
    }

    @Override
    public Product updateProduct(final Product body) {
        if (log.isDebugEnabled()) {
            log.debug("Service updating product {}", body);
        }
        return mapper.entityToApi(repository
            .save(mapper.apiToEntity(body)));
    }

    @Override
    public Product getProduct(final long productId, final int delay, final int faultPercent) {
        if (delay > 0) {
            if (log.isDebugEnabled()) {
                log.debug("Simulating delay of {}", delay);
            }
            simulateDelay(delay);
        }

        if (faultPercent > 0) {
            if (log.isDebugEnabled()) {
                log.debug("Introducing error in a %{}", faultPercent);
            }
            throwErrorIfBadLuck(faultPercent);
        }

        return repository
            .findById(productId)
            .map(mapper::entityToApi)
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
    public void deleteProduct(final long productId) {

        if (log.isDebugEnabled()) {
            log.debug("deleteProduct: tries to delete an entity with productId: {}", productId);
        }

        repository
            .findById(productId)
            .ifPresent(repository::delete);
    }

    private void simulateDelay(final int delay) {
        if (log.isDebugEnabled()) {
            log.debug("Sleeping for {} seconds...", delay);
        }
        try {
            Thread.sleep(Duration
                .ofSeconds(delay)
                .toMillis());
        }
        catch (final InterruptedException ignored) {
        }
        if (log.isDebugEnabled()) {
            log.debug("Moving on...");
        }
    }

    private void throwErrorIfBadLuck(final int faultPercent) {
        final int randomThreshold = getRandomNumber(1, 100);
        if (faultPercent < randomThreshold) {
            if (log.isDebugEnabled()) {
                log.debug("We got lucky, no error occurred, {} < {}", faultPercent, randomThreshold);
            }
        } else {
            if (log.isDebugEnabled()) {
                log.debug("Bad luck, an error occurred, {} >= {}", faultPercent, randomThreshold);
            }
            throw new RuntimeException("Something went wrong...");
        }
    }

    private int getRandomNumber(final int min, final int max) {

        if (max < min) {
            if (log.isWarnEnabled()) {
                log.warn("Max value {} show be greater than min {}", max, min);
            }
            throw new RuntimeException("Max must be greater than min");
        }

        return randomNumberGenerator.nextInt((max - min) + 1) + min;
    }
}
