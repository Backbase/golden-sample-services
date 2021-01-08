# Liquibase

https://www.liquibase.org/get-started/best-practices

## Guidelines

- changelogs organized by release, `db.changelog-master.xml` includes the changelog for the releases in the correct order. 
`db.changelog-master.xml` is the changelog you pass to all Liquibase calls.

- Datatypes: https://dba-presents.com/index.php/liquibase/216-liquibase-3-6-x-data-types-mapping-table
- Naming NVARCHAR for SQL Server
 
      - property:
          name: varcharDataType
          value: NVARCHAR
          dbms: mssql
      - property:
          name: varcharDataType
          value: VARCHAR
          dbms: mysql
      - property:
          name: varcharDataType
          value: VARCHAR2"	
- CSV		
- SQL Files, we need folder structure convention (generated-sql, manual-sql, changelogs)
- Sometimes we need to write plain SQL and add to change log: example partitioning etc	

        <changeSet author="StackOverflow" id="C0FE77AA-5517-11E7-8CAD-224C16886A7C">
            <createTable tableName="test">
                <column name="id" type="INT">
                    <constraints nullable="false"/>
                </column>
                <column name="NAME" type="VARCHAR(30)"/>
                <column name="borned" type="TIMESTAMP"/>
            </createTable>
            <modifySql dbms="postgresql">
                <append value=" PARTITION BY RANGE (UNIX_TIMESTAMP(borned)) (
        PARTITION p0 VALUES LESS THAN (UNIX_TIMESTAMP('2000-01-01 00:00:00')),
        PARTITION p1 VALUES LESS THAN (MAXVALUE))"/>
            </modifySql>
        </changeSet>
        
- ID and AUTHOR guidelines: ID = version+sequence e.g: 2_20_0_001 Author: backbase
- Comments: must have, can be used for documentation
- Patching	
- Sequence per table, naming conventions for the sequences	
- Context naming convention: create-ddl? version+sequence?
- Labels?
- Packaging of sql scripts, folder conventions: no H2
