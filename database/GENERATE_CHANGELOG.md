# Liquibase

## Generate a ChangeLog From an Existing Database

In this example we will generate liquibase changelog from existing database.

### Steps
1. Start docker-compose with `spring.liquibase.enabled=false` and `spring.jpa.hibernate.ddl-auto=create`.
2. Create liquibase.properties and add liquibase-maven-plugin.
3. Generate liquibase changelog.
4. Remove docker volume and enable liquibase.

### Configuration

#### liquibase.properties
```
url=jdbc:mysql://localhost:3306/product?useSSL=false&serverTimezone=UTC&createDatabaseIfNotExist=true
username=root
password=backbase
driver=com.mysql.jdbc.Driver
```

#### Plugin Configuration
```
<profiles>
    <profile>
      <id>liquibase</id>
      <build>
        <plugins>
          <plugin>
            <groupId>org.liquibase</groupId>
            <artifactId>liquibase-maven-plugin</artifactId>
            <version>3.10.2</version>
            <configuration>
              <changeLogFile>${basedir}/src/main/resources/db/db.changelog-master.yaml</changeLogFile>
              <propertyFile>${basedir}/src/main/resources/liquibase.properties</propertyFile>
              <outputChangeLogFile>${basedir}/src/main/resources/db/changelog/000-create.yaml</outputChangeLogFile>
            </configuration>
            <executions>
              <execution>
                <goals>
                  <goal>generateChangeLog</goal>
                </goals>
                <phase>generate-resources</phase>
              </execution>
            </executions>
          </plugin>
        </plugins>
      </build>
    </profile>
  </profiles>
```

### Execute the maven profile
    mvn clean install -Pliquibase

Update db.changelog-master.yaml
```
databaseChangeLog:
  - include:
      file: "changelog/000-create.yaml"
      relativeToChangelogFile: true
```

### Remove Docker MySQL volume
docker volume rm golden-sample-services_demo_mysql_data

### Update docker-compose
```
x-database-variables: &database-variables
  spring.liquibase.enabled: 'true'
  spring.liquibase.change-log: "classpath:/db/db.changelog-master.yaml"
```

### Verify Changelog
```
jamesv@bb-system-1085 golden-sample-services % docker exec -it demo_mysql bash
root@fafac5bb823d:/# mysql -u root -p
Enter password: 
Welcome to the MySQL monitor.  Commands end with ; or \g.
Your MySQL connection id is 24
Server version: 5.7.18 MySQL Community Server (GPL)

Copyright (c) 2000, 2017, Oracle and/or its affiliates. All rights reserved.

Oracle is a registered trademark of Oracle Corporation and/or its
affiliates. Other names may be trademarks of their respective
owners.

Type 'help;' or '\h' for help. Type '\c' to clear the current input statement.

mysql> use product
Reading table information for completion of table and column names
You can turn off this feature to get a quicker startup with -A

Database changed
mysql> select * from DATABASECHANGELOG;
+-----------------+----------------------+-----------------------------------------+---------------------+---------------+----------+------------------------------------+------------------------------------------+----------+------+-----------+----------+--------+---------------+
| ID              | AUTHOR               | FILENAME                                | DATEEXECUTED        | ORDEREXECUTED | EXECTYPE | MD5SUM                             | DESCRIPTION                              | COMMENTS | TAG  | LIQUIBASE | CONTEXTS | LABELS | DEPLOYMENT_ID |
+-----------------+----------------------+-----------------------------------------+---------------------+---------------+----------+------------------------------------+------------------------------------------+----------+------+-----------+----------+--------+---------------+
| 1598858603756-1 | backbase (generated) | classpath:/db/changelog/000-create.yaml | 2020-08-31 07:47:49 |             1 | EXECUTED | 8:ada307e15ac7a48de0a2bfc635955a12 | createTable tableName=hibernate_sequence |          | NULL | 3.8.9     | NULL     | NULL   | 8860068941    |
| 1598858603756-2 | backbase (generated) | classpath:/db/changelog/000-create.yaml | 2020-08-31 07:47:49 |             2 | EXECUTED | 8:86b1e98ab69b2b684dc445b82edb06e2 | createTable tableName=product            |          | NULL | 3.8.9     | NULL     | NULL   | 8860068941    |
+-----------------+----------------------+-----------------------------------------+---------------------+---------------+----------+------------------------------------+------------------------------------------+----------+------+-----------+----------+--------+---------------+
2 rows in set (0.00 sec)
```

