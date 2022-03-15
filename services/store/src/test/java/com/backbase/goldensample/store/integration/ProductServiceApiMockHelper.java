package com.backbase.goldensample.store.integration;

import com.backbase.goldensample.product.api.client.ApiClient;
import com.backbase.goldensample.product.api.client.v1.ProductServiceApi;
import org.mockito.Mockito;

import static org.mockito.Mockito.when;

class ProductServiceApiMockHelper {

    static void initMock(ProductServiceApi productServiceApi) {
        // api client is accessed to log its basepath
        ApiClient apiClient = Mockito.mock(ApiClient.class);
        when(apiClient.getBasePath()).thenReturn("basePath");
        when(productServiceApi.getApiClient()).thenReturn(apiClient);
    }

}
