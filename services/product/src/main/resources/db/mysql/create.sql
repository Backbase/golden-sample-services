--  *********************************************************************
--  Update Database Script
--  *********************************************************************
--  Change Log: /Users/andres/Development/golden-example/backbase-golden-example/services/product/src/main/resources/db/db.changelog-master.yaml
--  Ran at: 12/09/2020, 23:25
--  Against: root@offline:mysql?outputLiquibaseSql=none&changeLogFile=/Users/andres/Development/golden-example/backbase-golden-example/services/product/src/main/resources/db/mysql/databasechangelog.csv
--  Liquibase version: 3.10.2
--  *********************************************************************

--  Changeset /Users/andres/Development/golden-example/backbase-golden-example/services/product/src/main/resources/db/changelog/000-create.yaml::create-product-table::backbase
CREATE TABLE product (id BIGINT AUTO_INCREMENT NOT NULL, create_date datetime NULL, name VARCHAR(255) NULL, weight INT NULL, CONSTRAINT PK_PRODUCT PRIMARY KEY (id));

