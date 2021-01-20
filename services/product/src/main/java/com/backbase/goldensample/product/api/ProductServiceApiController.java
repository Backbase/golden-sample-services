package com.backbase.goldensample.product.api;

import com.backbase.goldensample.product.service.ProductService;
import com.backbase.product.api.service.v2.ProductServiceApi;
import com.backbase.product.api.service.v2.model.Product;
import com.backbase.product.api.service.v2.model.ProductId;
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
public class ProductServiceApiController implements ProductServiceApi {

    /**
     * Product service business logic interface.
     */
    private final ProductService prodService;

    @Autowired
    public ProductServiceApiController(final ProductService prodService) {
        this.prodService = prodService;
    }

    @Override
    public ResponseEntity<Void> deleteProduct(final Long productId) {
        if (log.isDebugEnabled()) {
            log.debug("Delete product id {}", productId);
        }
        prodService.deleteProduct(productId);
        if (log.isDebugEnabled()) {
            log.debug("Product id {} deleted", productId);
        }
        return ResponseEntity
            .noContent()
            .build();
    }

    @Override
    public ResponseEntity<List<Product>> getProducts() {
        if (log.isDebugEnabled()) {
            log.debug("Get list of products");
        }
        return ResponseEntity.ok(prodService.getAllProducts());
    }

    @Override
    public ResponseEntity<Product> getProductById(final Long productId) {
        if (log.isDebugEnabled()) {
            log.debug("Get product by id {}", productId);
        }
        return ResponseEntity.ok(prodService.getProduct(productId, 0, 0));
    }

    @Override
    public ResponseEntity<ProductId> postProduct(@Valid final Product product) {
        if (log.isDebugEnabled()) {
            log.debug("Create a product {}", product);
        }
        final Product productWithId = prodService.createProduct(product);
        final ProductId productId = new ProductId();
        productId.setId(productWithId.getProductId());
        if (log.isDebugEnabled()) {
            log.debug("Product {} created", productId);
        }
        return ResponseEntity.ok(productId);
    }

    @Override
    public ResponseEntity<Void> putProduct(@Valid final Product product) {
        if (log.isDebugEnabled()) {
            log.debug("Update a product {}", product);
        }
        final Product productWithId = prodService.updateProduct(product);
        if (log.isDebugEnabled()) {
            log.debug("product with id {} updated", productWithId.getProductId());
        }
        return ResponseEntity
            .noContent()
            .build();
    }

    @Override
    public ResponseEntity<Void> putProductById(final Long productId, @Valid final Product product) {
        if (log.isDebugEnabled()) {
            log.debug("Update a product {} with values {}", productId, product);
        }
        product.setProductId(productId);
        final Product productWithId = prodService.updateProduct(product);
        if (log.isDebugEnabled()) {
            log.debug("product with id {} updated", productWithId.getProductId());
        }
        return ResponseEntity
            .noContent()
            .build();
    }
}
