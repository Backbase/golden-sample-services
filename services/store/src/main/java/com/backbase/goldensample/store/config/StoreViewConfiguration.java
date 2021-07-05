package com.backbase.goldensample.store.config;

import com.backbase.buildingblocks.context.ContextScoped;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Validated
@Configuration
@ConfigurationProperties("backbase.store")
@ContextScoped
public class StoreViewConfiguration {

    public static final String STORE_THEME_RESPONSE_HEADER_NAME = "x-store-theme";

    private String theme;

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }
}
