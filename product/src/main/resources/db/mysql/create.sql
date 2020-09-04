--  *********************************************************************
--  Update Database Script
--  *********************************************************************
--  Change Log: /Users/andres/Development/golden-example/backbase-golden-example/product/src/main/resources/db/db.changelog-master.yaml
--  Ran at: 04/09/2020, 09:21
--  Against: root@offline:mysql?outputLiquibaseSql=none&changeLogFile=/Users/andres/Development/golden-example/backbase-golden-example/product/src/main/resources/db/mysql/databasechangelog.csv
--  Liquibase version: 3.10.2
--  *********************************************************************

--  Changeset /Users/andres/Development/golden-example/backbase-golden-example/product/src/main/resources/db/changelog/000-create.yaml::create-hibernate-sequence-table::backbase
CREATE TABLE hibernate_sequence (next_val BIGINT NULL);

INSERT INTO hibernate_sequence (next_val) VALUES ('1');

--  Changeset /Users/andres/Development/golden-example/backbase-golden-example/product/src/main/resources/db/changelog/000-create.yaml::create product table::backbase
CREATE TABLE product (id BIGINT NOT NULL, create_date datetime NULL, name VARCHAR(255) NULL, weight INT NULL, CONSTRAINT PK_PRODUCT PRIMARY KEY (id));

