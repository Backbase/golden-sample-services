--  *********************************************************************
--  Update Database Script
--  *********************************************************************
--  Change Log: /Users/andres/Development/golden-example/backbase-golden-example/services/review/src/main/resources/db/db.changelog-master.yaml
--  Ran at: 24/11/2020, 18:25
--  Against: root@offline:mysql?outputLiquibaseSql=none&changeLogFile=/Users/andres/Development/golden-example/backbase-golden-example/services/review/src/main/resources/db/sql/mysql/databasechangelog.csv
--  Liquibase version: 3.10.2
--  *********************************************************************

--  Changeset /Users/andres/Development/golden-example/backbase-golden-example/services/review/src/main/resources/db/changelog/000-create.yaml::create-review-table::backbase
CREATE TABLE review (id BIGINT AUTO_INCREMENT NOT NULL, author VARCHAR(50) NULL, content VARCHAR(255) NULL, product_id BIGINT NULL, subject VARCHAR(255) NULL, CONSTRAINT PK_REVIEW PRIMARY KEY (id));

--  Changeset /Users/andres/Development/golden-example/backbase-golden-example/services/review/src/main/resources/db/changelog/000-create.yaml::create-review-index::backbase (generated)
ALTER TABLE review ADD CONSTRAINT review_unique_idx UNIQUE (product_id, id);

