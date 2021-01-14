
CREATE SEQUENCE seq_product START WITH 1 INCREMENT BY 5;

CREATE TABLE product (id NUMBER(38, 0) NOT NULL, create_date TIMESTAMP NOT NULL, name VARCHAR2(255), weight NUMBER(5), CONSTRAINT PK_PRODUCT PRIMARY KEY (id, create_date));

CREATE INDEX ix_product_create_date ON product(create_date DESC);
