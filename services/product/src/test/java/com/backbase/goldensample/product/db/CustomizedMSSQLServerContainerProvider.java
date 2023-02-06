package com.backbase.goldensample.product.db;

import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.JdbcDatabaseContainerProvider;
import org.testcontainers.utility.DockerImageName;

/**
 * This is custom SQL Server configuration provider for testcontainers. Added in order to make sure integration tests
 * are performed on the SQL Server configuration recommended by Backbase.
 *
 * @see <a
 * href="https://community.backbase.com/documentation/Retail-Apps-Universal/latest/deploy_services_as_jars">Deploy your
 * services as JAR files</a>
 */
public class CustomizedMSSQLServerContainerProvider extends JdbcDatabaseContainerProvider {

    @Override
    public boolean supports(String databaseType) {
        return databaseType.equals("customsqlserver");
    }

    @Override
    public JdbcDatabaseContainer newInstance() {
        return newInstance(CustomizedMSSQLServerContainer.DEFAULT_TAG);
    }

    @Override
    public JdbcDatabaseContainer newInstance(String tag) {
        return new CustomizedMSSQLServerContainer(
            DockerImageName.parse(CustomizedMSSQLServerContainer.IMAGE).withTag(tag));
    }

}
