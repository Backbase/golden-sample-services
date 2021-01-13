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
- CSV or SQL Files for data, we need folder structure convention (generated-sql, manual-sql, changelogs)
- Sometimes we need to write plain SQL and add to change log: example partitioning etc	

        <changeSet author="backbase" id="2_20_0_001">
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
        
- ID and AUTHOR guidelines: 
    - ID = version+sequence e.g: 2_20_0_001 
    - Author: backbase
- Comments: must have, can be used for documentation
- Patching
- Sequence per table, naming conventions for the sequences explained in Confluence guidelines
- Context naming convention: 
    for the creation changesets: initial 
    for the upgrade ones: upgrade_X_Y_Z_to_X1_Y1_Z1 e.g: upgrade_1_0_0_to_1_1_0
- Labels: can be used for the same purpose as the contexts [Contexts vs labels](https://www.liquibase.org/blog/contexts-vs-labels)
- Packaging of sql scripts, follow the folder conventions and guidelines, e.g: no H2
