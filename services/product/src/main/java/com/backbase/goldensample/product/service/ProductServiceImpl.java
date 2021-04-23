package com.backbase.goldensample.product.service;

import com.backbase.buildingblocks.presentation.errors.NotFoundException;
import com.backbase.goldensample.product.persistence.ProductEntity;
import com.backbase.goldensample.product.persistence.ProductRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;

    @Autowired
    public ProductServiceImpl(ProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public ProductEntity createProduct(ProductEntity body) {
        log.debug("Service creating product {}", body);
        return repository.save(body);
    }

    @Override
    public ProductEntity updateProduct(ProductEntity body) {
        log.debug("Service updating product {}", body);
        return repository.save(body);
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
