package com.backbase.goldensample.review.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.validation.annotation.Validated;

@Validated
@Configuration
@EnableJpaRepositories("com.backbase.goldensample.review.persistence")
@EntityScan("com.backbase.goldensample.review.persistence")
public class AppConfiguration {
}
