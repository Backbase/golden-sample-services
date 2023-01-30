package com.backbase.goldensample.product.db;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DbConfigIT {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void mysqlDbShouldHaveProperConfiguration() {
        assumeTrue(getDbName().equalsIgnoreCase("mysql"));

        jdbcTemplate.query("SELECT @@character_set_database, @@collation_database", rs -> {
            String characterSetDatabase = rs.getString(1);
            String collationDatabase = rs.getString(2);

            assertAll(
                () -> assertThat(characterSetDatabase, is("utf8mb4")),
                () -> assertThat(collationDatabase, is("utf8mb4_unicode_ci"))
            );
        });
    }

    @Test
    void mssqlShouldHaveProperConfiguration() {
        assumeTrue(getDbName().equalsIgnoreCase("Microsoft SQL Server"));

        jdbcTemplate.query(
            "SELECT collation_name FROM sys.databases WHERE name = DB_NAME()",
            rs -> {
                String collation = rs.getString(1);
                assertThat("Collation should be set to Latin1_General_CS_AS_KS", collation,
                    is("Latin1_General_CS_AS_KS"));
            });
    }

    private String getDbName() {
        return this.jdbcTemplate.execute(
            (ConnectionCallback<String>) connection -> connection.getMetaData().getDatabaseProductName());
    }
}
