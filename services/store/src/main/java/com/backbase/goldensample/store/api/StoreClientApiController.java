package com.backbase.goldensample.store.api;

import com.backbase.buildingblocks.presentation.errors.NotFoundException;
import com.backbase.goldensample.store.api.service.v1.ProductCompositeClientApi;
import com.backbase.goldensample.store.api.service.v1.model.ProductAggregate;
import com.backbase.goldensample.store.domain.Product;
import com.backbase.goldensample.store.mapper.StoreMapper;
import com.backbase.goldensample.store.service.ProductCompositeService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class StoreClientApiController implements ProductCompositeClientApi {

    private final ProductCompositeService productCompositeService;
    private final StoreMapper storeMapper;

    @Override
    public ResponseEntity<ProductAggregate> getProductById(Long productId) {
        log.debug("getCompositeProduct: lookup a product aggregate for productId: {}", productId);

        return productCompositeService.retrieveProductWithReviews(productId)
            .map(p -> ResponseEntity.ok(storeMapper.map(p)))
            .orElseThrow(() -> new NotFoundException("No product found for productId: " + productId));
    }

    @Override
    public ResponseEntity<ProductAggregate> postProduct(@Valid ProductAggregate productAggregate) {

        // ensure the client does not specify the id.
        productAggregate.setProductId(null);
        // LOL
        log.debug("createCompositeProduct: creates a new composite entity for productId: {}",
            productAggregate.getProductId());

        Product product = productCompositeService.createProductWithReviews(storeMapper.map(productAggregate));
        log.debug("createCompositeProduct: composite entities created for productId: {}",
            product.getProductId());

        return ResponseEntity.ok(storeMapper.map(product));
    }

}
