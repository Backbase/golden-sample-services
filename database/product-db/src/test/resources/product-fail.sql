
CREATE TABLE product (id BIGINT AUTO_INCREMENT NOT NULL, create_date datetime NOT NULL, name VARCHAR(255) NULL COMMENT 'The name of the product', weight SMALLINT NULL COMMENT 'The weight of the product in kgs', CONSTRAINT PK_PRODUCT PRIMARY KEY (id, create_date)) COMMENT='Table to store the Products from our store'PARTITION BY RANGE COLUMNS (create_date) ( PARTITION p0 VALUES LESS THAN ('2019-01-01'),                     PARTITION p1 VALUES LESS THAN ('2020-01-01'), PARTITION p2 VALUES LESS THAN ('2021-01-01'));

CREATE TABLE product (id BIGINT AUTO_INCREMENT NOT NULL, create_date datetime NOT NULL, name VARCHAR(255) NULL COMMENT 'The name of the product', weight SMALLINT NULL COMMENT 'The weight of the product in kgs', CONSTRAINT PK_PRODUCT PRIMARY KEY (id, create_date)) COMMENT='Table to store the Products from our store'PARTITION BY RANGE COLUMNS (create_date) ( PARTITION p0 VALUES LESS THAN ('2019-01-01'),                     PARTITION p1 VALUES LESS THAN ('2020-01-01'), PARTITION p2 VALUES LESS THAN ('2021-01-01'));

ALTER TABLE product COMMENT = 'Table to store the Products from our store'PARTITION BY RANGE COLUMNS (create_date) ( PARTITION p0 VALUES LESS THAN ('2019-01-01'),                     PARTITION p1 VALUES LESS THAN ('2020-01-01'), PARTITION p2 VALUES LESS THAN ('2021-01-01'));

CREATE INDEX ix_product_create_date ON product(create_date DESC);

ALTER TABLE product ADD additions LONGTEXT NULL;

