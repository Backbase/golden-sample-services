
CREATE INDEX ix_product_create_date ON product(create_date DESC);

ALTER TABLE customer ADD address VARCHAR(255) NULL;

ALTER TABLE customer ADD COLUMN external_id_upper VARCHAR(36) AS (UPPER(EXTERNAL_ID));

CREATE INDEX ix_customer_external_id_upper ON customer(external_id_upper);

INSERT INTO product (name, create_date) VALUES ('Savings Account', str_to_date('01/01/2021','%d/%m/%Y'));

COMMIT;

