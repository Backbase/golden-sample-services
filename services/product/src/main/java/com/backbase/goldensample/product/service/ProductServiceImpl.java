package com.backbase.goldensample.product.service;

import com.backbase.buildingblocks.backend.communication.event.EnvelopedEvent;
import com.backbase.buildingblocks.backend.communication.event.proxy.EventBus;
import com.backbase.buildingblocks.presentation.errors.NotFoundException;
import com.backbase.goldensample.product.mapper.ProductMapper;
import com.backbase.goldensample.product.persistence.ProductEntity;
import com.backbase.goldensample.product.persistence.ProductRepository;
import com.backbase.product.api.service.v1.model.Product;
import com.backbase.product.api.service.v1.model.ProductId;
import com.backbase.product.event.spec.v1.ProductCreatedEvent;
import com.backbase.product.event.spec.v1.ProductDeletedEvent;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@AllArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductMapper mapper;
    private final ProductRepository repository;
    private final EventBus eventBus;

    @Override
    public ProductId createProduct(Product body) {
        ProductEntity entity = mapper.apiToEntity(body);
        log.debug("Service creating product {}", entity);
        ProductEntity entityWithId = repository.save(entity);
        ProductId productId = new ProductId();
        productId.setId(entityWithId.getId());

        emitCreatedEvent(entityWithId);

        return productId;
    }

    @Override
    public void updateProduct(Product body) {
        ProductEntity entity = mapper.apiToEntity(body);
        log.debug("Service updating product {}", entity);
        repository.save(entity);
    }

    @Override
    public ProductEntity getProduct(long productId) {
        log.debug("Get the Product with Id {}", productId);
        return repository.findById(productId)
            .orElseThrow(() -> new NotFoundException(String.format("Item is not found with id : '%s'", productId)));
    }

    @Override
    public List<ProductEntity> getAllProducts() {
        //TODO add pagination
        List<ProductEntity> productEntityList = repository.findAll();
        log.debug("Get all the Products, total: {}", productEntityList.size());
        return productEntityList;

    }

    /*
     Implementation is idempotent, that is,
     it will not report any failure if the entity is not found Always 200
    */
    @Override
    public void deleteProduct(long productId) {

        log.debug("deleteProduct: tries to delete an entity with productId: {}", productId);

        repository.findById(productId).ifPresent(productEntity -> {
            emitDeletedEvent(productEntity);

            repository.delete(productEntity);
        });
    }

    /**
     * Create product created event and add it to the queue.
     *
     * @param productEntity product
     */
    private void emitCreatedEvent(ProductEntity productEntity) {
        ProductCreatedEvent event = mapper.entityToCreatedEvent(productEntity);
        EnvelopedEvent<ProductCreatedEvent> envelopedEvent = new EnvelopedEvent<>();
        envelopedEvent.setEvent(event);
        eventBus.emitEvent(envelopedEvent);
    }

    /**
     * Create product deleted event and add it to the queue.
     *
     * @param productEntity product
     */
    private void emitDeletedEvent(ProductEntity productEntity) {
        ProductDeletedEvent event = mapper.entityToDeletedEvent(productEntity);
        event.setDeleteDateAsLocalDate(LocalDate.now());
        EnvelopedEvent<ProductDeletedEvent> envelopedEvent = new EnvelopedEvent<>();
        envelopedEvent.setEvent(event);
        eventBus.emitEvent(envelopedEvent);
    }

}
