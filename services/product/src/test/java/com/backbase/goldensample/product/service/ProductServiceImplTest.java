package com.backbase.goldensample.product.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.backbase.buildingblocks.backend.communication.event.EnvelopedEvent;
import com.backbase.buildingblocks.backend.communication.event.proxy.EventBus;
import com.backbase.goldensample.product.mapper.ProductMapper;
import com.backbase.goldensample.product.persistence.ProductEntity;
import com.backbase.goldensample.product.persistence.ProductRepository;
import com.backbase.product.api.service.v1.model.Product;
import com.backbase.product.event.spec.v1.ProductCreatedEvent;
import com.backbase.product.event.spec.v1.ProductDeletedEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.hamcrest.core.IsInstanceOf;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private EventBus eventBus;

    @Spy
    private ProductMapper mapper = Mappers.getMapper(ProductMapper.class);

    @InjectMocks
    private ProductServiceImpl productService;


    private static final LocalDate TODAY = LocalDate.of(2020, 1, 28);

    private final Product product = new Product().productId(1L).name("Product").weight(20).createDate(TODAY);
    private final ProductEntity productEntity = new ProductEntity(1L, "Product", 20, TODAY, Collections.singletonMap("popularity","29%"));


    @Test
    void getAllProductsTest() {
        List<ProductEntity> list = new ArrayList<>();
        ProductEntity productOne = new ProductEntity(1L, "Product", 20, TODAY, null);
        ProductEntity productTwo = new ProductEntity(2L, "Product1", 21, TODAY, null);
        ProductEntity productThree = new ProductEntity(3L, "Product2", 22, TODAY, null);

        list.add(productOne);
        list.add(productTwo);
        list.add(productThree);

        when(productRepository.findAll()).thenReturn(list);

        List<ProductEntity> empList = productService.getAllProducts();

        assertEquals(3, empList.size());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void getProductByIdTest() {
        when(productRepository.findById(1L)).thenReturn(java.util.Optional.of(productEntity));

        ProductEntity product = productService.getProduct(1);

        assertAll(
            () -> assertEquals("Product", product.getName()),
            () -> assertEquals(20, product.getWeight()),
            () -> assertEquals(TODAY, product.getCreateDate()));
    }

    @Test
    void getProductByIdWithDelayTest() {
        when(productRepository.findById(1L)).thenReturn(java.util.Optional.of(productEntity));

        ProductEntity product = productService.getProduct(1);

        assertAll(
            () -> assertEquals("Product", product.getName()),
            () -> assertEquals(20, product.getWeight()),
            () -> assertEquals(TODAY, product.getCreateDate()));
    }

    @Test
    void getProductByIdWithErrorTest() {
        assertThrows(RuntimeException.class, () -> productService.getProduct(1));
    }

    @Test
    void createProductTest() {
        when(productRepository.save(any())).thenReturn(productEntity);
        productService.createProduct(product);
        verify(productRepository, times(1)).save(any(ProductEntity.class));
    }

    @Test
    void updateProductTest() {
        productService.updateProduct(product);
        verify(productRepository, times(1)).save(any(ProductEntity.class));
    }

    @Test
    void deleteProductTest() {
        when(productRepository.findById(1L)).thenReturn(java.util.Optional.of(productEntity));
        productService.deleteProduct(product.getProductId());
        verify(productRepository, times(1)).delete(any(ProductEntity.class));
    }

    @Test
    public void testEmitProductCreateEvent() {
        when(productRepository.save(any(ProductEntity.class))).thenReturn(productEntity);
        productService.createProduct(product);

        ArgumentCaptor<EnvelopedEvent> captor = ArgumentCaptor.forClass(EnvelopedEvent.class);
        verify(eventBus, times(1)).emitEvent(captor.capture());

        EnvelopedEvent envelopedEvent = captor.getValue();
        Object emittedEvent = envelopedEvent.getEvent();
        assertThat(emittedEvent, IsInstanceOf.instanceOf(ProductCreatedEvent.class));
        ProductCreatedEvent event = (ProductCreatedEvent) emittedEvent;
        assertAll(
            () -> assertEquals("Product", event.getName()),
            () -> assertEquals(20, product.getWeight()));
    }

    @Test
    public void testEmitProductDeleteEvent() {
        when(productRepository.findById(any(long.class))).thenReturn(java.util.Optional.of(productEntity));

        productService.deleteProduct(product.getProductId());

        ArgumentCaptor<EnvelopedEvent> captor = ArgumentCaptor.forClass(EnvelopedEvent.class);
        verify(eventBus, times(1)).emitEvent(captor.capture());

        EnvelopedEvent envelopedEvent = captor.getValue();
        Object emittedEvent = envelopedEvent.getEvent();
        assertThat(emittedEvent, IsInstanceOf.instanceOf(ProductDeletedEvent.class));
        ProductDeletedEvent event = (ProductDeletedEvent)emittedEvent;
        assertAll(
            () -> assertEquals("Product", event.getName()),
            () -> assertEquals(20, product.getWeight()));
    }

}
