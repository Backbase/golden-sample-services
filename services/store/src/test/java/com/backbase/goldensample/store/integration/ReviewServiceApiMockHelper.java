package com.backbase.goldensample.store.integration;

import com.backbase.goldensample.review.api.client.ApiClient;
import com.backbase.goldensample.review.api.client.v2.ReviewServiceApi;
import org.mockito.Mockito;

import static org.mockito.Mockito.when;

class ReviewServiceApiMockHelper {

    static void initMock(ReviewServiceApi reviewServiceApi) {
        // api client is accessed to log its basepath
        ApiClient apiClient = Mockito.mock(ApiClient.class);
        when(apiClient.getBasePath()).thenReturn("basePath");
        when(reviewServiceApi.getApiClient()).thenReturn(apiClient);
    }

}
