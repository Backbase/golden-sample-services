package com.backbase.goldensample.product.config;

import javax.persistence.GenerationType;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.validation.annotation.Validated;

/**
 * <p>This class is required to conditionally override primary key generation strategy for batch entities.</p>
 * <p>By default batch entities are configured to use {@link GenerationType#SEQUENCE} which works for Oracle and MSSQL
 * out of the box.</p>
 * <p>When batch entities are used for MySql (JDBC URL is checked) we override the primary key generation strategy to
 * be {@link GenerationType#IDENTITY} because MySql does not support sequences.</p>
 * <p>It is possible to disable automatic (based on JDBC URL check) override by explicitly setting the property
 * {@code backbase.entities-identity-strategy-override} to {@code true} or {@code false}.</p>
 * <br>
 * <p>Liquibase change log generates sequences or auto increment columns according to database type.</p>
 *
 */
@Validated
@Configuration
@PropertySource("classpath:db/mapping/mysql_entities_identity_strategy_override.yml")
@ConditionalOnExpression("'${backbase.entities-identity-strategy-override}' eq 'true' "
    + "|| ("
    + "'${backbase.entities-identity-strategy-override}' ne 'false' "
    + "&& '${spring.datasource.url}'.contains('mysql')"
    + ")")
public class IdentityStrategyOverrideConfiguration {
}
