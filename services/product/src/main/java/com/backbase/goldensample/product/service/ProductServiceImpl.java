package com.backbase.goldensample.product.service;

import com.backbase.buildingblocks.backend.communication.context.OriginatorContextUtil;
import com.backbase.buildingblocks.backend.communication.event.EnvelopedEvent;
import com.backbase.buildingblocks.backend.communication.event.proxy.EventBus;
import com.backbase.buildingblocks.presentation.errors.NotFoundException;
import com.backbase.goldensample.product.mapper.ProductMapper;
import com.backbase.goldensample.product.persistence.ProductEntity;
import com.backbase.goldensample.product.persistence.ProductRepository;
import com.backbase.product.api.service.v1.model.Product;
import com.backbase.product.api.service.v1.model.ProductId;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final OriginatorContextUtil originatorContextUtil;
    @Autowired
    public ProductServiceImpl(ProductRepository repository, ProductMapper mapper, EventBus eventBus, OriginatorContextUtil originatorContextUtil) {
        this.repository = repository;
        this.mapper = mapper;
        this.eventBus = eventBus;
        this.originatorContextUtil = originatorContextUtil;
    }
    @Override
    public ProductId createProduct(Product body) {
        ProductEntity entity = mapper.apiToEntity(body);
        log.debug("Service creating product {}", entity);
        ProductEntity entityWithId = repository.save(entity);
        ProductId productId = new ProductId();
        productId.setId(entityWithId.getId());
        return productId;
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

        repository.findById(productId).ifPresent(repository::delete);
    }

}
