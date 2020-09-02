package com.backbase.goldensample.product.service;

import com.backbase.buildingblocks.presentation.errors.NotFoundException;
import com.backbase.goldensample.product.mapper.ProductMapper;
import com.backbase.goldensample.product.persistence.ProductRepository;
import com.backbase.product.api.service.v2.model.Product;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;

    private final ProductMapper mapper;
    private final Random randomNumberGenerator = new Random();

    @Autowired
    public ProductServiceImpl(ProductRepository repository, ProductMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Product createProduct(Product body) {
        return mapper.entityToApi(repository
            .save(mapper.apiToEntity(body)));
    }

    @Override
    public Product updateProduct(Product body) {
        return mapper.entityToApi(repository
            .save(mapper.apiToEntity(body)));
    }

    @Override
    public Product getProduct(long productId, int delay, int faultPercent) throws NotFoundException {
        if (delay > 0) {
            simulateDelay(delay);
        }

        if (faultPercent > 0) {
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
    public void deleteProduct(long productId) throws NotFoundException {

        log.debug("deleteProduct: tries to delete an entity with productId: {}", productId);

        repository.findById(productId).ifPresent(product -> repository.delete(product));
    }

    // TODO could be added to a utility class to be used by all core services implementations.
    private void isValidProductId(long productId) {
        if (productId < 1) {
            throw new RuntimeException("Invalid productId: " + productId);
        }
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
            throw new RuntimeException("Max must be greater than min");
        }

        return randomNumberGenerator.nextInt((max - min) + 1) + min;
    }
}
