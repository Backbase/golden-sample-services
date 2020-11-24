-- *********************************************************************
-- Update Database Script
-- *********************************************************************
-- Change Log: /Users/andres/Development/golden-example/backbase-golden-example/services/review/src/main/resources/db/db.changelog-master.yaml
-- Ran at: 24/11/2020, 18:25
-- Against: root@offline:oracle?outputLiquibaseSql=none&changeLogFile=/Users/andres/Development/golden-example/backbase-golden-example/services/review/src/main/resources/db/sql/oracle/databasechangelog.csv
-- Liquibase version: 3.10.2
-- *********************************************************************

-- Changeset /Users/andres/Development/golden-example/backbase-golden-example/services/review/src/main/resources/db/changelog/000-create.yaml::create-review-table::backbase
CREATE TABLE review (id NUMBER(38, 0) NOT NULL, author VARCHAR2(50), content VARCHAR2(255), product_id NUMBER(38, 0), subject VARCHAR2(255), CONSTRAINT PK_REVIEW PRIMARY KEY (id));

-- Changeset /Users/andres/Development/golden-example/backbase-golden-example/services/review/src/main/resources/db/changelog/000-create.yaml::create-review-index::backbase (generated)
ALTER TABLE review ADD CONSTRAINT review_unique_idx UNIQUE (product_id, id);

-- Changeset /Users/andres/Development/golden-example/backbase-golden-example/services/review/src/main/resources/db/changelog/000-create.yaml::create_hibernate_sequence::backbase
CREATE SEQUENCE hibernate_sequence START WITH 1 INCREMENT BY 5;

