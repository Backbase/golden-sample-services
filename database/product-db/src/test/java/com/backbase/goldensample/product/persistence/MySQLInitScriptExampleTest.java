package com.backbase.goldensample.product.persistence;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.ContainerLaunchException;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.ext.ScriptUtils;
import org.testcontainers.jdbc.JdbcDatabaseDelegate;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
public class MySQLInitScriptExampleTest {

    @Test
    @DisplayName("Test to verify the create SQL scripts works")
    public void testCreateSQLScript() throws SQLException {
        try (MySQLContainer<?> container = new MySQLContainer<>(DockerImageName.parse("mysql:5.7"))
            .withInitScript("db/generated-sql/mysql/create/product.sql")
            .withUsername("test")
            .withPassword("test")
            .withEnv("MYSQL_ROOT_HOST", "%")
            .withExposedPorts(3306)) {
            container.start();

            ResultSet resultSet = performQuery(container, "SELECT * FROM product");
//            We could do other verifications about the content of some tables etc
//            String firstColumnValue = resultSet.getString(1);
//            assertEquals("Value from init script should equal real value", "hello world", firstColumnValue);
        }
    }


    @Test
    @DisplayName("Test to verify the update SQL scripts from previous versions works")
    void testUpdateScript() throws SQLException {
        try (MySQLContainer<?> container = new MySQLContainer<>(DockerImageName.parse("mysql:5.7"))
            .withInitScript("db/generated-sql/mysql/create/product-1.0.0.sql")
            .withUsername("test")
            .withPassword("test")
            .withEnv("MYSQL_ROOT_HOST", "%")
            .withExposedPorts(3306)) {
            container.start();

            var containerDelegate = new JdbcDatabaseDelegate(container, "");
            ScriptUtils.runInitScript(containerDelegate, "product-1k-rows.sql");
            ScriptUtils.runInitScript(containerDelegate, "db/generated-sql/mysql/upgrade_1_0_0_to_1_1_0/product.sql");

            ResultSet resultSet = performQuery(container, "SELECT * FROM product");
        }
    }

    @Test
    @DisplayName("Test to verify a wrong create SQL scripts fails")
    void testWrongCreateScriptsFails() throws SQLException {
        Assertions.assertThrows(ContainerLaunchException.class, () -> {
            try (MySQLContainer<?> container = new MySQLContainer<>(DockerImageName.parse("mysql:5.7"))
                .withInitScript("product-fail.sql")
                .withUsername("test")
                .withPassword("test")
                .withEnv("MYSQL_ROOT_HOST", "%")
                .withExposedPorts(3306)) {
                container.start();
            }
        });
    }

    protected ResultSet performQuery(JdbcDatabaseContainer<?> container, String sql) throws SQLException {
        DataSource ds = getDataSource(container);
        Statement statement = ds.getConnection().createStatement();
        statement.execute(sql);
        ResultSet resultSet = statement.getResultSet();

        resultSet.next();
        return resultSet;
    }

    protected DataSource getDataSource(JdbcDatabaseContainer<?> container) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(container.getJdbcUrl());
        hikariConfig.setUsername(container.getUsername());
        hikariConfig.setPassword(container.getPassword());
        hikariConfig.setDriverClassName(container.getDriverClassName());

        return new HikariDataSource(hikariConfig);
    }

}
