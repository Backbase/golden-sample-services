package com.backbase.goldensample.product.api;

import com.backbase.goldensample.product.mapper.ProductMapper;
import com.backbase.goldensample.product.service.ProductService;
import com.backbase.product.api.integration.v1.ProductIntegrationApi;
import com.backbase.product.api.service.v1.model.Product;
import com.backbase.product.api.service.v1.model.ProductId;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Class <code>ProductController</code> is the implementation of the main Product Endpoint API definition.
 *
 * @see ProductIntegrationApi
 */
@RestController
@RequestMapping
@Slf4j
public class ProductIntegrationApiController implements ProductIntegrationApi {
    /**
     * Product service business logic interface.
     */
    private final ProductService prodService;
    private final ProductMapper mapper;

    @Autowired
    public ProductIntegrationApiController(ProductService prodService, ProductMapper mapper) {
        this.prodService = prodService;
        this.mapper = mapper;
    }


    @Override
    public ResponseEntity<Void> deleteProduct(Long productId) {
        log.debug("Delete product id {}", productId);
        prodService.deleteProduct(productId);
        log.debug("Product id {} deleted", productId);
        return ResponseEntity.noContent().build();
    }


    @Override
    public ResponseEntity<Product> getProductById(Long productId) {
        log.debug("Get product by id {}", productId);
        Product product = mapper.entityToApi(prodService.getProduct(productId));
        return ResponseEntity.ok(product);
    }

    @Override
    public ResponseEntity<ProductId> postProduct(@Valid Product product) {
        log.debug("Create a product {}", product);
        ProductId productId = prodService.createProduct(product);
        log.debug("Product {} created", productId);
        return ResponseEntity.ok(productId);
    }

    @Override
    public ResponseEntity<Void> putProduct(@Valid Product product) {
        log.debug("Update a product {}", product);
        prodService.updateProduct(product);
        log.debug("product with {} updated.", product.getProductId());
        return ResponseEntity.noContent().build();
    }
}
