package com.backbase.goldensample.store.integration;

import com.backbase.goldensample.review.api.client.ApiClient;
import com.backbase.goldensample.review.api.client.v2.ReviewServiceApi;
import org.mockito.Mockito;

import static org.mockito.Mockito.when;

class ReviewServiceApiMockHelper {

    static void initMock(ReviewServiceApi reviewServiceApi) {
        // People are logging
        ApiClient apiClient = Mockito.mock(ApiClient.class);
        when(apiClient.getBasePath()).thenReturn("basePath");
        when(reviewServiceApi.getApiClient()).thenReturn(apiClient);
    }

}
