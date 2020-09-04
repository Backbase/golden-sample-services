-- *********************************************************************
-- Update Database Script
-- *********************************************************************
-- Change Log: /Users/andres/Development/golden-example/backbase-golden-example/product/src/main/resources/db/db.changelog-master.yaml
-- Ran at: 04/09/2020, 09:21
-- Against: root@offline:mssql?outputLiquibaseSql=none&changeLogFile=/Users/andres/Development/golden-example/backbase-golden-example/product/src/main/resources/db/mssql/databasechangelog.csv
-- Liquibase version: 3.10.2
-- *********************************************************************

-- Changeset /Users/andres/Development/golden-example/backbase-golden-example/product/src/main/resources/db/changelog/000-create.yaml::create-hibernate-sequence::backbase
CREATE SEQUENCE hibernate_sequence START WITH 1 INCREMENT BY 5
GO

-- Changeset /Users/andres/Development/golden-example/backbase-golden-example/product/src/main/resources/db/changelog/000-create.yaml::create product table::backbase
CREATE TABLE product (id bigint NOT NULL, create_date datetime, name varchar(255), weight int, CONSTRAINT PK_PRODUCT PRIMARY KEY (id))
GO

