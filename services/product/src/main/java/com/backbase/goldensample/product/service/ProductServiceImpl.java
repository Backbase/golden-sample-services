package com.backbase.goldensample.product.service;

import com.backbase.buildingblocks.presentation.errors.NotFoundException;
import com.backbase.goldensample.product.mapper.ProductMapper;
import com.backbase.goldensample.product.persistence.ProductRepository;
import com.backbase.product.api.service.v2.model.Product;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Predicate;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

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
        if (GREATER_THAN_ZERO.test(delay)) {
            if (log.isDebugEnabled()) {
                log.debug("Simulating delay of {}", delay);
            }
            simulateDelay().accept(delay);
        }

        if (GREATER_THAN_ZERO.test(faultPercent)) {
            if (log.isDebugEnabled()) {
                log.debug("Introducing error in a %{}", faultPercent);
            }
            errorInCaseOfBadLuck().accept(faultPercent);
        }

        return repository
            .findById(productId)
            .map(mapper::entityToApi)
            .orElseThrow(() -> new NotFoundException(String.format("Item is not found with id : '%s'", productId)));
    }

    private static final Predicate<Integer> GREATER_THAN_ZERO = value -> value > 0;

    @Override
    public List<Product> getAllProducts() {
        //TODO add pagination
        return mapper.entityListToApiList(repository.findAll());
    }

    // paginated version
    @Override public List<Product> getAllProducts(
        final int page, final int size, final UriComponentsBuilder uriBuilder, final HttpServletResponse response) {
        
        return null;
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

    private Consumer<Integer> simulateDelay() {
        return delay -> {
            final CountDownLatch waiter = new CountDownLatch(1);
            if (log.isDebugEnabled()) {
                log.debug("Sleeping for {} seconds...", delay);
            }
            try {
                waiter.await(delay, TimeUnit.SECONDS);
            }
            catch (final InterruptedException ignored) {
            }
            if (log.isDebugEnabled()) {
                log.debug("Moving on...");
            }
        };
    }

    private Consumer<Integer> errorInCaseOfBadLuck() {
        return faultPercent -> {
            final int randomThreshold = randomNumber().apply(1, 100);
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
        };
    }

    private BiFunction<Integer, Integer, Integer> randomNumber() {
        return (min, max) -> {
            if (MAX_SMALLER_THAN_MIN.test(min, max)) {
                if (log.isDebugEnabled()) {
                    log.warn("Max value {} show be greater than min {}", max, min);
                }
                throw new RuntimeException("Max must be greater than min");
            }
            return randomNumberGenerator.nextInt((max - min) + 1) + min;
        };
    }

    private static final BiPredicate<Integer, Integer> MAX_SMALLER_THAN_MIN = (min, max) -> max < min;
}
