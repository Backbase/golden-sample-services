package com.backbase.goldensample.product.api;

import com.backbase.goldensample.product.mapper.ProductIntegrationMapper;
import com.backbase.goldensample.product.service.ProductService;
import com.backbase.product.api.integration.v2.ProductIntegrationApi;
import com.backbase.product.api.integration.v2.model.Product;
import com.backbase.product.api.integration.v2.model.ProductId;
import javax.validation.Valid;
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
    private final ProductIntegrationMapper mapper;

    @Autowired
    public ProductIntegrationApiController(ProductService prodService, ProductIntegrationMapper mapper) {
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
        return ResponseEntity.ok(mapper.fromApiToIntegration(prodService.getProduct(productId, 0, 0)));
    }

    @Override
    public ResponseEntity<ProductId> postProduct(@Valid Product product) {
        log.debug("Create a product {}", product);
        com.backbase.product.api.service.v2.model.Product productWithId = prodService.createProduct(mapper.fromIntegrationToApi(product));
        ProductId productId = new ProductId();
        productId.setId(productWithId.getProductId());
        log.debug("Product {} created", productId);
        return ResponseEntity.ok(productId);
    }

    @Override
    public ResponseEntity<Void> putProduct(@Valid Product product) {
        log.debug("Update a product {}", product);
        com.backbase.product.api.service.v2.model.Product productWithId = prodService.updateProduct(mapper.fromIntegrationToApi(product));
        log.debug("product with {} updated.", productWithId.getProductId());
        return ResponseEntity.noContent().build();
    }
}
