package com.backbase.goldensample.product;

import com.fasterxml.jackson.databind.module.SimpleModule;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
@Slf4j
public class Application extends SpringBootServletInitializer {

    @Bean
    public SimpleModule addStrictBooleanDeserializer() {
        return new SimpleModule().addDeserializer(Boolean.class, new StrictBooleanDeserializer());
    }

    public static void main(final String[] args) {
        log.debug("Starting application with parameters {}", Arrays.toString(args));
        SpringApplication.run(Application.class, args);
    }

}
