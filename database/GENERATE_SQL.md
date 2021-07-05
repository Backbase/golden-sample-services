# Liquibase

## Generate SQL scripts from a changelog

### Plugin Configuration
```
    <profile>
      <id>generateSQL</id>
      <build>
        <plugins>
          <plugin>
            <groupId>org.liquibase</groupId>
            <artifactId>liquibase-maven-plugin</artifactId>
            <version>3.10.2</version>
            <executions>
              <execution>
                <id>mysql</id>
                <configuration>
                  <changeLogFile>${basedir}/src/main/resources/db/db.changelog-master.yaml</changeLogFile>
                  <url>offline:mysql?outputLiquibaseSql=none&amp;changeLogFile=${basedir}/src/main/resources/db/mysql/databasechangelog.csv</url>
                  <username>root</username>
                  <password>backbase</password>
                  <migrationSqlOutputFile>${basedir}/src/main/resources/db/mysql/create.sql</migrationSqlOutputFile>
                </configuration>
                <goals>
                  <goal>updateSQL</goal>
                </goals>
                <phase>process-resources</phase>
              </execution>
              <execution>
                <id>mssql</id>
                <configuration>
                  <changeLogFile>${basedir}/src/main/resources/db/db.changelog-master.yaml</changeLogFile>
                  <url>offline:mssql?outputLiquibaseSql=none&amp;changeLogFile=${basedir}/src/main/resources/db/mssql/databasechangelog.csv</url>
                  <username>root</username>
                  <password>backbase</password>
                  <migrationSqlOutputFile>${basedir}/src/main/resources/db/mssql/create.sql</migrationSqlOutputFile>
                </configuration>
                <goals>
                  <goal>updateSQL</goal>
                </goals>
                <phase>process-resources</phase>
              </execution>
              <execution>
                <id>oracle</id>
                <configuration>
                  <changeLogFile>${basedir}/src/main/resources/db/db.changelog-master.yaml</changeLogFile>
                  <url>offline:oracle?outputLiquibaseSql=none&amp;changeLogFile=${basedir}/src/main/resources/db/oracle/databasechangelog.csv</url>
                  <username>root</username>
                  <password>backbase</password>
                  <migrationSqlOutputFile>${basedir}/src/main/resources/db/oracle/create.sql</migrationSqlOutputFile>
                </configuration>
                <goals>
                  <goal>updateSQL</goal>
                </goals>
                <phase>process-resources</phase>
              </execution>
            </executions>
          </plugin>
        </plugins>
      </build>
    </profile>
```

To use it and generate the sql:

    mvn clean compile -PgenerateSQL

### Liquibase Without a Database Connection
There are many, many processes and requirements companies have for managing their database schemas. 
Some allow the application to directly manage them on startup, some require SQL scripts be executed by hand.
In Backbase we provide SQL scripts for the different databases we support and to generate those sql scripts we use Liquibase. 
Liquibase has always supported an *“updateSQL”* mode which does not update the database but instead outputs what would be run. 
This allows developers and DBAs to know exactly what will be ran and even make modifications as needed before actually 
executing the script.

Before version 3.2, however, Liquibase required an active database connection for updateSQL. 
It used that connection to determine the SQL dialect to use and to query the DATABASECHANGELOG table to learn what changeSets 
have already been executed.

### Controlling updateSql SQL Syntax
With version 3.2, Liquibase added a new “offline” mode. Instead of specifying a jdbc url such as `jdbc:mysql://localhost:3306/product` you 
can use `offline:mysql` or `offline:mssql` which lets Liquibase know what dialect to use. 
For finer dialect control, you can specify parameters like `offline:mysql?version=3.4&caseSensitive=false`

#### Available dialect parameters:

* **version**: Standard X.Y.Z version of the database
* **productName**: String description of the database, like the JDBC driver would return
* **catalog**: String containing the name of the default top-level container (‘database’ in some databases ‘schema’ in others)
* **caseSensitive**: Boolean value specifying if the database is case sensitive or not

#### Tracking History With CSV
These parameters let Liquibase know what SQL to generate for each changeSet, but without an active database connection
you cannot rely on the DATABASECHANGELOG table to track what changeSets have already been ran. 
Instead, offline mode uses a CSV file which mimics the structure of the DATABASECHANGELOG table.

By default, Liquibase will use a file called “databasechangelog.csv” in the working directory, but it can be specified 
with a “changeLogFile” parameter such as `offline:mssql?changeLogFile=path/to/file.csv`

It is up to you to ensure that the contents of the csv file match what is in the database. Running *updateSQL* 
automatically appends to the CSV file under the assumption that you will apply the SQL to the database. 
Since the csv file matches a particular database, it isn’t something you normally would store or share under version 
control because every database can (and probably will) be in a different state. 
If you do store the files in a central location, you will probably want to at least have a separate file for each database.

By default, the SQL generated by updateSql in offline mode will still contain the standard DATABASECHANGELOG insert 
statements, so each database that you apply the SQL to will still have a correct DATABASECHANGELOG table. 
This means that you can switch between a direct-connection update and offline updateSQL as needed. 
It also means that you can also extract the current contents of the DATABASECHANGELOG table to a CSV file and use that 
as the file passed to the offline connection to ensure you have the right contents in the file.

If you do not want the DATABASECHANGELOG table SQL included in updateSQL output, there is an “outputLiquibaseSql” 
parameter which can be passed in your offline url.

#### Possible outputLiquibaseSql values:

* **“none” will** output no DATABASECHANGELOG statements
* **“data_only”** will output only INSERT INTO DATABASECHANGELOG statements
* **“all”** will output CREATE TABLE DATABASECHANGELOG if the csv file does not exist as well as 
INSERT statements (default value)

#### Offline Snapshots
The new 3.4.0 release of Liquibase expands The new 3.4.0 release of Liquibase expands offline support with a new 
“snapshot” parameter which can be passed to the offline url pointing to a saved database structure. 
Liquibase will use the snapshot anywhere it would have normally needed to read the current database state. 
This allows you to use preconditions and perform diff and diffChangeLog operations without an active connection and 
even between snapshots of the same database from different points in time.

To create a snapshot of your live databases, use the -snapshotFormat=json parameter on the “snapshot” command.

#### Command line example:

    $ liquibase --url=jdbc:mysql://localhost/lbcat snapshot --snapshotFormat=json > snapshot.json
or

    $ liquibase --url=jdbc:mysql://localhost/lbcat --outputFile=path/to/output.json snapshot --snapshotFormat=json
NOTE: currently only “json” is supported as a snapshotFormat.

You can then use that file with your offline url and any snapshot operations will use it as the database state.

    liquibase –url=jdbc:mysql://localhost/lbcat –referenceUrl=offline:mysql?snapshot=path/to/snapshot.json diff will compare the stored snapshot with the current database state
    liquibase –url=offline:mysql?snapshot=path/to/snapshot.json diff –referenceUrl=offline:mysql?snapshot=path/to/older-snapshot.json diff will compare two snapshots
    liquibase –url=offline:mysql?snapshot=path/to/snapshot.json generateChangeLog will generate a changelog based on what is in the snapshot
    liquibase –url=jdbc:mysql://localhost/lbcat –referenceUrl=offline:mysql?snapshot=path/to/snapshot.json diffChangeLog will generate a changelog based on what is new in the real database compared to what is in the snapshot.

More info: https://docs.liquibase.com/commands/community/updatesql.html?Highlight=updateSQL
