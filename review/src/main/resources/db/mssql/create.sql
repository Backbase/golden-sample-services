-- *********************************************************************
-- Update Database Script
-- *********************************************************************
-- Change Log: /Users/andres/Development/golden-example/backbase-golden-example/review/src/main/resources/db/db.changelog-master.yaml
-- Ran at: 05/09/2020, 12:57
-- Against: root@offline:mssql?outputLiquibaseSql=none&changeLogFile=/Users/andres/Development/golden-example/backbase-golden-example/review/src/main/resources/db/mssql/databasechangelog.csv
-- Liquibase version: 3.10.2
-- *********************************************************************

-- Changeset /Users/andres/Development/golden-example/backbase-golden-example/review/src/main/resources/db/changelog/000-create.yaml::create-review-table::backbase
CREATE TABLE review (id bigint NOT NULL, author varchar(50), content varchar(255), product_id bigint, subject varchar(255), CONSTRAINT PK_REVIEW PRIMARY KEY (id))
GO

-- Changeset /Users/andres/Development/golden-example/backbase-golden-example/review/src/main/resources/db/changelog/000-create.yaml::create-review-index::backbase (generated)
ALTER TABLE review ADD CONSTRAINT review_unique_idx UNIQUE (product_id, id)
GO

-- Changeset /Users/andres/Development/golden-example/backbase-golden-example/review/src/main/resources/db/changelog/000-create.yaml::create_hibernate_sequence::backbase
CREATE SEQUENCE hibernate_sequence START WITH 1 INCREMENT BY 5
GO

