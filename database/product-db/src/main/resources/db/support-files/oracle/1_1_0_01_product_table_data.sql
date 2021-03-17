-- Create savings account product in product table

INSERT INTO product(id, name, create_date) VALUES (seq_product.nextval, 'Savings Account', to_date('01/01/2021','DD/MM/YYYY'));

COMMIT;