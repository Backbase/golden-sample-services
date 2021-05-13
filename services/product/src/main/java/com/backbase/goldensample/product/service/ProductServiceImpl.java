package com.backbase.goldensample.product.service;

import com.backbase.buildingblocks.presentation.errors.NotFoundException;
import com.backbase.goldensample.product.mapper.ProductMapper;
import com.backbase.goldensample.product.persistence.ProductEntity;
import com.backbase.goldensample.product.persistence.ProductRepository;
import com.backbase.product.api.service.v1.model.Product;
import com.backbase.product.api.service.v1.model.ProductId;
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
