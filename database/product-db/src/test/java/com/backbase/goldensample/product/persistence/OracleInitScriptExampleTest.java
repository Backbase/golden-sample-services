package com.backbase.goldensample.product.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.OracleContainer;
import org.testcontainers.ext.ScriptUtils;
import org.testcontainers.jdbc.JdbcDatabaseDelegate;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class OracleInitScriptExampleTest {

    @Test
    @DisplayName("Test to verify the create SQL scripts works")
    public void testCreateSQLScript() throws SQLException {
        try (OracleContainer container = new OracleContainer("diemobiliar/minimized-oraclexe-image:18.4.0-xe")
            .withInitScript("db/generated-sql/oracle/create/product.sql")
            .withUsername("AOO_TESTS")
            .withPassword("AOO_TESTS");) {
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
        try (OracleContainer container = new OracleContainer("diemobiliar/minimized-oraclexe-image:18.4.0-xe")
            .withInitScript("db/generated-sql/oracle/create/product-1.1.0.sql")
            .withUsername("AOO_TESTS")
            .withPassword("AOO_TESTS");) {
            container.start();

            var containerDelegate = new JdbcDatabaseDelegate(container, "");
            ScriptUtils.runInitScript(containerDelegate, "oracle-data-product.sql");
            ScriptUtils.runInitScript(containerDelegate, "db/generated-sql/oracle/upgrade_1_0_0_to_1_1_0/product.sql");

            ResultSet resultSet = performQuery(container, "SELECT * FROM product");

            String firstColumnValue = resultSet.getString(3);
            assertEquals("product", firstColumnValue, "Value from init script should equal real value");
        }
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
