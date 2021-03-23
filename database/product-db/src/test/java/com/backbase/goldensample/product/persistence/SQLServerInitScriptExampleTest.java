package com.backbase.goldensample.product.persistence;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.MSSQLServerContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

@Testcontainers
public class SQLServerInitScriptExampleTest {

    DockerImageName MSSQL_SERVER_IMAGE = DockerImageName.parse("mcr.microsoft.com/mssql/server:2017-CU12");

    @Test
    @DisplayName("Test to verify the create SQL scripts works")
    public void testCreateSQLScript() throws SQLException {

        String localResourcesPath = "db/generated-sql/mssql/create/";
        String dbInitScriptFileName = "initMssql.sh";
        String sqlInitScriptFileName = "product.sql";
        MountableFile dbInitScript = MountableFile.forClasspathResource(dbInitScriptFileName);
        MountableFile sqlInitScript = MountableFile.forClasspathResource(localResourcesPath + sqlInitScriptFileName);
        String containerResourcesPath = "/home/";
        MSSQLServerContainer<?> container = new MSSQLServerContainer<>(MSSQL_SERVER_IMAGE)
            .withCopyFileToContainer(dbInitScript, containerResourcesPath)
            .withCopyFileToContainer(sqlInitScript, containerResourcesPath);
        container.start();
        try {
            container.execInContainer("chmod", "+x", containerResourcesPath + dbInitScriptFileName);
            container.execInContainer(containerResourcesPath + dbInitScriptFileName);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        ResultSet resultSet = performQuery(container, "SELECT * FROM product");
//            We could do other verifications about the content of some tables etc
//            String firstColumnValue = resultSet.getString(1);
//            assertEquals("Value from init script should equal real value", "hello world", firstColumnValue);
    }


    @Test
    @DisplayName("Test to verify the update SQL scripts from previous versions works")
    void testUpdateScript() throws SQLException {
        String createResourcesPath = "db/generated-sql/mssql/create/";
        String dataResourcesPath = "mssql-data-product.sql";
        String updateResourcesPath = "db/generated-sql/mssql/upgrade_1_0_0_to_1_1_0/product.sql";
        String dbInitScriptFileName = "initAndUpdateMssql.sh";
        String sqlInitScriptFileName = "product-1.1.0.sql";
        MountableFile dbInitScript = MountableFile.forClasspathResource(dbInitScriptFileName);
        MountableFile sqlInitScript = MountableFile.forClasspathResource(createResourcesPath + sqlInitScriptFileName);
        MountableFile sqlDataScript = MountableFile.forClasspathResource(dataResourcesPath);
        MountableFile sqlUpgradeScript = MountableFile.forClasspathResource(updateResourcesPath);
        String containerResourcesPath = "/home/";
        MSSQLServerContainer<?> container = new MSSQLServerContainer<>(MSSQL_SERVER_IMAGE)
            .withCopyFileToContainer(dbInitScript, containerResourcesPath)
            .withCopyFileToContainer(sqlInitScript, containerResourcesPath)
            .withCopyFileToContainer(sqlDataScript, containerResourcesPath)
            .withCopyFileToContainer(sqlUpgradeScript, containerResourcesPath);
        container.start();
        try {
            container.execInContainer("chmod", "+x", containerResourcesPath + dbInitScriptFileName);
            container.execInContainer(containerResourcesPath + dbInitScriptFileName);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        ResultSet resultSet = performQuery(container, "SELECT * FROM product");
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
