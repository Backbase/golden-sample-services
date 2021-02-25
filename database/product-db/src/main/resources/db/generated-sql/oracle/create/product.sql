
CREATE SEQUENCE seq_product START WITH 1 INCREMENT BY 5;

CREATE TABLE product (id NUMBER(38, 0) NOT NULL, create_date TIMESTAMP NOT NULL, name VARCHAR2(255), weight NUMBER(5), CONSTRAINT PK_PRODUCT PRIMARY KEY (id, create_date));

COMMENT ON TABLE product IS 'Table to store the Products from our store';

COMMENT ON COLUMN product.create_date IS 'The date when the product was created';

COMMENT ON COLUMN product.name IS 'The name of the product';

COMMENT ON COLUMN product.weight IS 'The weight of the product in kgs';

CREATE INDEX ix_product_create_date ON product(create_date DESC);

ALTER TABLE product ADD additions CLOB;

