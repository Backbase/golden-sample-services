# TestContainers

## Drawbacks of In-Memory Databases for Tests 
With in-memory databases, you are testing against a different database than your production database. This significantly reduces the meaning and reliability of your tests. A green h2-based test doesn’t mean that your application will also work against the real-world database. Let me give a very simple example:

- *groups* is a reserved keyword in MySQL 8. If you have a table called Group you will not detect that with our H2 testing.
- Since MySQL 5.7.6, *virtual* is a reserved keyword and can’t be used as an unquoted column name if by chance, 
we had a Hibernate entity with a property virtual any request touching this entity will fail. 
Unfortunately, if we execute our tests against an H2 database, so the problem will not show up until we deployed in an environment.

But that’s why we are actually doing tests, right? To find those bugs. And we can only find them if we are as close to the (database) reality as possible.

Another even bigger issue is the different feature set of in-memory databases compared to the real database. From time to time, 
you need to use vendor-specific features. Those features often allow you to be more efficient, accurate, elegant or 
maintainable - or to do certain things at all. But they are usually not supported by in-memory databases:

- For instance, we wanted to use MySQL’s unix_timestamp() function to be independent of the different time zones where our 
MySQL database servers are running. We want to prevent subtle inaccuracies. 
But this function doesn’t exist in H2, so we couldn’t use them.
- Another example are Triggers in Oracle database, which also doesn’t exist in H2 natively. 
There are only Java-based triggers. So we had to rewrite the trigger logic in Java.

## Integration Tests in Production-like Environments
Especially the first kind of drawback (find bugs that only occur when executing against the real database) can be 
tackled by proper integration tests in a production-like environment/VM. 
That’s true and those end-to-end tests are really important. 
So you may need and have that kind of tests anyway. 
But we have to be aware of the following points:

- If you are lucky, the integration tests will show those database-specific errors. It depends on the coverage of the integration tests.
- You only have a single layer of security, because your first test layer, the h2-based tests of your build, won’t find them.
- Moreover, those errors should show up as early as possible in the build and delivery pipeline. We want to see those 
errors in the test phase of the project build.
- Setting up a proper production-like environment and a delivery pipeline is required. This is non-trivial: **money & time**
- Integration tests are only black-box tests and coarse-grained. They are slower and require more bootstrapping.
- The more external services and resources are used by your application, the more fragile your integration tests can become.


## Alternative: A Docker Container For The Database
Stop using H2 and use a dockerized version of your real database instead. Docker makes creating a real-world database so easy. Due to the standard management layer that Docker provides, we can easily utilize any database we want for our tests. But mind that you have to install Docker on both the developer’s machine and all CI servers. But for me, the increased safety and uncompromising implementation are definitely worth the effort.

Basically, there are two approaches:

- Manage the database container in the test code (using [TestContainers](https://www.testcontainers.org/))
- Manage the database container by the build (using Maven or Gradle)

### Managed by the Build
Integrate the container management into the build. Just start the database container, run the tests and stop it afterwards. Doable using, for example, fabric8 maven plugin (https://github.com/fabric8io/docker-maven-plugin) for controlling docker containers.

### Managed by the Test
Use TestContainers and manage the database in your test code.
Let's review this approach more in deep and what steps we need to apply:

#### TestContainers dependency is managed by spring boot
    
#### Add the database module you want to test, e.g. MySQL, Oracle, MSSQL, MariaDB:

    <dependency>
        <groupId>org.testcontainers</groupId>
        <artifactId>mysql</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.testcontainers</groupId>
        <artifactId>oracle-xe</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.testcontainers</groupId>
        <artifactId>mssqlserver</artifactId>
        <scope>test</scope>
    </dependency>


> Adding this TestContainers library JAR will not automatically add a database driver JAR to your project. You should ensure that your project also has a suitable database driver as a dependency.

#### Add the database drivers:

    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>5.1.47</version>
        <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>com.microsoft.sqlserver</groupId>
      <artifactId>mssql-jdbc</artifactId>
      <version>7.4.1.jre8</version>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>org.mariadb.jdbc</groupId>
      <artifactId>mariadb-java-client</artifactId>
      <version>2.6.2</version>
      <scope>test</scope>
    </dependency>
    <!-- https://mvnrepository.com/artifact/com.oracle/ojdbc7 -->
    <dependency>
        <groupId>com.oracle</groupId>
        <artifactId>ojdbc6</artifactId>
        <version>11.2.0.3</version>
        <scope>test</scope>
    </dependency>


#### We can use Test Containers to obtain a temporary database in one of two ways:

- **Using a specially modified JDBC URL**: after making a very simple modification to your system's JDBC URL string, 
Testcontainers will provide a disposable stand-in database that can be used without requiring modification to your application code. (This is our approach)
- **JUnit @Rule/@ClassRule**: This mode starts a database inside a container before your tests and tears it down afterwards. 

##### Database containers launched via JDBC URL scheme
As long as we have TestContainers and the appropriate JDBC driver on your classpath, you can simply modify regular JDBC connection URLs to get a fresh containerized instance of the database each time your application starts up. No code changes

- TC needs to be on your application's classpath at runtime for this to work
- For Spring Boot (Before version 2.3.0) you need to specify the driver manually
`spring.datasource.driver-class-name=org.testcontainers.jdbc.ContainerDatabaseDriver`
    
Original URL: `jdbc:mysql:8.0.36://somehostname:someport/databasename`

Insert `tc:` after `jdbc:` as follows. Note that the hostname, port and database name will be ignored; you can leave these as-is or set them to any value.

TestContainers URL with a specific version: `jdbc:tc:mysql:8.0.36:///databasename`

For multiple databases testing we can use different profiles:

    # application-mysql.yaml
    spring:
      datasource:
        url: jdbc:tc:mysql:8.0.36://localhost:3306/dbs
        username: root
        password: root
        driver-class-name: org.testcontainers.jdbc.ContainerDatabaseDriver
    
    # application-oracle.yaml
    spring:
      datasource:
        url: jdbc:tc:oracle:thin:@localhost:1521/ORCLCDB

    # application-mssql.yaml
    spring:
      datasource:
        url: jdbc:tc:sqlserver://localhost;instance=SQLEXPRESS;databaseName=high_performance_java_persistence

##### Using a classpath init script
Testcontainers can run an init script after the database container is started, but before your code is given a connection to it. The script must be on the classpath, and is referenced as follows:

`jdbc:tc:mysql:8.0.36:///databasename?TC_INITSCRIPT=somepath/init_mysql.sql`

This is useful if you have a fixed script for setting up database schema, etc.

##### Using an init script from a file
If the init script path is prefixed file:, it will be loaded from a file (relative to the working directory, which will usually be the project root).

`jdbc:tc:mysql:8.0.36:///databasename?TC_INITSCRIPT=file:src/main/resources/init_mysql.sql`

##### Using an init function
Instead of running a fixed script for DB setup, it may be useful to call a Java function that you define. This is intended to allow you to trigger database schema migration tools. To do this, add TC_INITFUNCTION to the URL as follows, passing a full path to the class name and method:

`jdbc:tc:mysql:8.0.36:///databasename?TC_INITFUNCTION=org.testcontainers.jdbc.JDBCDriverTest::sampleInitFunction`

The init function must be a public static method which takes a java.sql.Connection as its only parameter, e.g.

    public class JDBCDriverTest {
        public static void sampleInitFunction(Connection connection) throws SQLException {
            // e.g. run schema setup or Flyway/liquibase/etc DB migrations here...
        }
        ...
        
##### Running container in daemon mode
By default database container is being stopped as soon as last connection is closed. There are cases when you might need to start container and keep it running till you stop it explicitly or JVM is shutdown. To do this, add TC_DAEMON parameter to the URL as follows:

`jdbc:tc:mysql:8.0.36:///databasename?TC_DAEMON=true`

With this parameter database container will keep running even when there're no open connections.

##### Running container with tmpfs options
Container can have tmpfs mounts for storing data in host memory. This is useful if you want to speed up your database tests. Be aware that the data will be lost when the container stops.

To pass this option to the container, add TC_TMPFS parameter to the URL as follows:

`jdbc:tc:postgresql:9.6.8:///databasename?TC_TMPFS=/testtmpfs:rw`

If you need more than one option, separate them by comma (e.g. TC_TMPFS=key:value,key1:value1&other_parameters=foo).

For more information about tmpfs mount, see the [official Docker documentation](https://docs.docker.com/storage/tmpfs/).

More info: https://www.testcontainers.org/modules/databases/jdbc/

##### Running MySql container overriding my.cnf settings

For MySQL databases, it is possible to override configuration settings using resources on the classpath. 
Assuming `db/mysql_conf_override` is a directory on the classpath containing `.cnf` files, the following URL can be used:

`jdbc:tc:mysql:8.0.36:///databasename?TC_MY_CNF=db/mysql_conf_override`

##### Database container objects
In case you can't use the URL support, or need to fine-tune the container, you can instantiate it yourself.
Add a `@Rule` or `@ClassRule` to your test class, e.g.:

    @ClassRule
    public static MySQLContainer mysqlSQLContainer = new MySQLContainer("mysql:8.0");
     
    static class Initializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + mysqlSQLContainer.getJdbcUrl(),
                    "spring.datasource.username=" + mysqlSQLContainer.getUsername(),
                    "spring.datasource.password=" + mysqlSQLContainer.getPassword()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }

Now, in your test code (or a suitable setup method), you can obtain details necessary to connect to this database:

- *mysql.getJdbcUrl()* provides a JDBC URL your code can connect to
- *mysql.getUsername()* provides the username your code should pass to the driver
- *mysql.getPassword()* provides the password your code should pass to the driver

> Note that if you use `@Rule`, you will be given an isolated container for each test method. If you use `@ClassRule`, 
you will get an isolated container for all the methods in the test class.

