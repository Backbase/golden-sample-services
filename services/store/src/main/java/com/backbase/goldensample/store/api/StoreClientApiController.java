package com.backbase.goldensample.store.api;

import com.backbase.buildingblocks.presentation.errors.NotFoundException;
import com.backbase.goldensample.store.api.service.v1.ProductCompositeClientApi;
import com.backbase.goldensample.store.api.service.v1.model.ProductAggregate;
import com.backbase.goldensample.store.api.service.v1.model.Stars;
import com.backbase.goldensample.store.config.StoreViewConfiguration;
import com.backbase.goldensample.store.domain.Product;
import com.backbase.goldensample.store.mapper.StoreMapper;
import com.backbase.goldensample.store.service.ProductCompositeService;
import java.util.Optional;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class StoreClientApiController implements ProductCompositeClientApi {

    private final ProductCompositeService productCompositeService;
    private final StoreViewConfiguration storeViewConfiguration;
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

    @RequestMapping(
            value = "/client-api/v1/product-composite/{productId}/stars",
            produces = {"application/json"},
            method = RequestMethod.GET)
    @Override
    public ResponseEntity<Stars> getStarsForProductById(Long productId) {
        return ResponseEntity.ok(new Stars().stars(4));
    }

    @RequestMapping(
            value = "/client-api/v1/product-composite/{productId}/stars",
            produces = {"text/plain"},
            method = RequestMethod.GET)
    public ResponseEntity<String> getStarsForProductByIdAsString(@PathVariable("productId") Long productId) {
        return ResponseEntity.ok("4");
    }

    private BodyBuilder withThemeHeader(BodyBuilder responseEntity) {
        return responseEntity.header(StoreViewConfiguration.STORE_THEME_RESPONSE_HEADER_NAME, storeViewConfiguration.getTheme());
    }

}
