package com.backbase.goldensample.store.api;

import com.backbase.buildingblocks.presentation.errors.NotFoundException;
import com.backbase.goldensample.store.api.service.v1.ProductCompositeClientApi;
import com.backbase.goldensample.store.api.service.v1.model.ProductAggregate;
import com.backbase.goldensample.store.config.StoreViewConfig;
import com.backbase.goldensample.store.domain.Product;
import com.backbase.goldensample.store.mapper.StoreMapper;
import com.backbase.goldensample.store.service.ProductCompositeService;
import java.util.Optional;
import javax.validation.Valid;
import javax.ws.rs.HEAD;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class StoreClientApiController implements ProductCompositeClientApi {

    private ProductCompositeService productCompositeService;
    private StoreViewConfig storeViewConfig;
    private final StoreMapper storeMapper;

    @Override
    public ResponseEntity<ProductAggregate> getProductById(Long productId) {
        log.debug("getCompositeProduct: lookup a product aggregate for productId: {}", productId);

        Optional<Product> product = productCompositeService.retrieveProductWithReviews(productId);
        if (product.isEmpty()) {
            log.debug("No product was found for id {}", productId);
            throw new NotFoundException("No product found for productId: " + productId);
        }

        return withThemeHeader(ResponseEntity.ok()).body(storeMapper.map(product.get()));
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

        return withThemeHeader(ResponseEntity.ok()).body(storeMapper.map(product));
    }

    private BodyBuilder withThemeHeader(BodyBuilder responseEntity) {
        return responseEntity.header(StoreViewConfig.STORE_THEME_RESPONSE_HEADER_NAME, storeViewConfig.getTheme());
    }

}
