
CREATE TABLE product (id BIGINT AUTO_INCREMENT NOT NULL, create_date datetime NULL, name VARCHAR(255) NULL, weight SMALLINT NULL, CONSTRAINT PK_PRODUCT PRIMARY KEY (id));

CREATE INDEX idx_create_date ON product(create_date DESC);

