
CREATE INDEX ix_product_create_date ON product(create_date DESC);

ALTER TABLE customer ADD address VARCHAR2(255);

ALTER TABLE customer ADD external_id_upper VARCHAR2(36) AS (UPPER(external_id));

CREATE INDEX ix_customer_external_id_upper ON customer(external_id_upper);

INSERT INTO product(id, name, create_date) VALUES (seq_product.nextval, 'Savings Account', to_date('01/01/2021','DD/MM/YYYY'));

COMMIT;

