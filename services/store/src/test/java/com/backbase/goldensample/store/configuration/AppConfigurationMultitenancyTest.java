package com.backbase.goldensample.store.configuration;

import static org.assertj.core.api.Assertions.assertThat;

import com.backbase.buildingblocks.multitenancy.Tenant;
import com.backbase.buildingblocks.multitenancy.TenantContext;
import com.backbase.goldensample.product.api.client.v1.ProductServiceApi;
import com.backbase.goldensample.review.api.client.v1.ReviewServiceApi;
import com.backbase.goldensample.store.Application;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = {Application.class})
@AutoConfigureMockMvc
@ActiveProfiles({"it","m10y"})
public class AppConfigurationMultitenancyTest {

    @Autowired
    private ProductServiceApi productServiceApi;

    @Autowired
    private ReviewServiceApi reviewServiceApi;

    @Autowired
    private com.backbase.goldensample.review.api.client.v2.ReviewServiceApi reviewServiceApiV2;

    @BeforeEach
    public void clearTenant() {
        TenantContext.clear();
    }

    @Test
    void defaultApiConfiguration() {
        assertThat(productServiceApi.getApiClient().getBasePath()).isEqualTo("http://localhost:9915");
        assertThat(reviewServiceApi.getApiClient().getBasePath()).isEqualTo("http://localhost:9916");
        assertThat(reviewServiceApiV2.getApiClient().getBasePath()).isEqualTo("http://localhost:9916");
    }

    @Test
    void apiConfiguration_tenant_rebrandShop() {
        switchTenant("rebrand_shop");

        assertThat(productServiceApi.getApiClient().getBasePath()).isEqualTo("http://rs-product:8815");
        assertThat(reviewServiceApi.getApiClient().getBasePath()).isEqualTo("http://rs-review:8816");
        assertThat(reviewServiceApiV2.getApiClient().getBasePath()).isEqualTo("http://rs-review:8816");
    }

    private static void switchTenant(String tenantId) {
        TenantContext.clear();
        Tenant tenant = new Tenant();
        tenant.setId(tenantId);
        TenantContext.setTenant(tenant);
    }

}
