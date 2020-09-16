package com.backbase.goldensample.product.api.service.v1;

import com.backbase.goldensample.product.mapper.ProductMapper;
import com.backbase.goldensample.product.mapper.ProductVersionMapper;
import com.backbase.goldensample.product.service.ProductService;
import com.backbase.product.api.service.v1.ProductServiceApi;
import com.backbase.product.api.service.v1.model.Product;
import com.backbase.product.api.service.v1.model.ProductId;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Class <code>ProductController</code> is the implementation of the main Product Endpoint API definition.
 *
 * @see ProductServiceApi
 */
@RestController
@RequestMapping
@Slf4j
public class ProductServiceApiV1Controller implements ProductServiceApi {

    /**
     * Product service business logic interface.
     */
    private final ProductService prodService;
    private final ProductVersionMapper mapper;

    @Autowired
    public ProductServiceApiV1Controller(ProductService prodService, ProductVersionMapper mapper) {
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
    public ResponseEntity<List<Product>> getProducts() {
        log.debug("Get list of products");
        return ResponseEntity.ok(mapper.v2ListToV1List(prodService.getAllProducts()));
    }

    @Override
    public ResponseEntity<Product> getProductById(Long productId) {
        log.debug("Get product by id {}", productId);
        return ResponseEntity.ok(mapper.v2ToV1(prodService.getProduct(productId, 0, 0)));
    }

    @Override
    public ResponseEntity<ProductId> postProduct(@Valid Product product) {
        log.debug("Create a product {}", product);
        com.backbase.product.api.service.v2.model.Product productWithId = prodService.createProduct(mapper.v1ToV2(product));
        ProductId productId = new ProductId();
        productId.setId(productWithId.getProductId());
        log.debug("Product {} created", productId);
        return ResponseEntity.ok(productId);
    }

    @Override
    public ResponseEntity<Void> putProduct(@Valid Product product) {
        log.debug("Update a product {}", product);
        com.backbase.product.api.service.v2.model.Product productWithId = prodService.updateProduct(mapper.v1ToV2(product));
        log.debug("product with id {} updated", productWithId.getProductId());
        return ResponseEntity.noContent().build();
    }

}
