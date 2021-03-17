-- Create savings account product in product table

INSERT INTO product (name, create_date) VALUES ('Savings Account', str_to_date('01/01/2021','%d/%m/%Y'));

COMMIT;