## Liquibase

In this example we will generate liquibase changelog from existing database.

### Steps
1. Start docker-compose with spring.liquibase.enabled=false and spring.jpa.hibernate.ddl-auto=create.
2. Create liquibase.properties and add liquibase-maven-plugin.
3. Generate liquibase changelog.

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

### Generate a ChangeLog From an Existing Database
mvn clean install -Pliquibase

Update db.changelog-master.yaml
```
databaseChangeLog:
  - include:
      file: "changelog/000-create.yaml"
      relativeToChangelogFile: true
```